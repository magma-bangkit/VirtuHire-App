package com.magma.virtuhire.ui.screen.interview

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.magma.virtuhire.data.remote.response.ChatType
import com.magma.virtuhire.data.remote.response.JobDetailResponse
import com.magma.virtuhire.domain.model.Author
import com.magma.virtuhire.domain.model.InterviewChat
import com.magma.virtuhire.ui.components.common.ButtonSize
import com.magma.virtuhire.ui.components.common.ButtonVariant
import com.magma.virtuhire.ui.components.common.DotsTyping
import com.magma.virtuhire.ui.components.common.ErrorDialog
import com.magma.virtuhire.ui.components.common.InputField
import com.magma.virtuhire.ui.components.common.LoadingDialog
import com.magma.virtuhire.ui.components.common.PrimaryButton
import com.magma.virtuhire.ui.components.common.PrimaryIconButton
import com.magma.virtuhire.ui.screen.history_detail.ConfirmationDialog
import com.magma.virtuhire.ui.theme.Primary100
import com.magma.virtuhire.ui.theme.Primary200
import com.magma.virtuhire.ui.theme.Primary50
import com.magma.virtuhire.ui.theme.Primary500
import com.magma.virtuhire.ui.theme.Primary700
import com.magma.virtuhire.ui.theme.PrimaryGray100
import com.magma.virtuhire.ui.theme.PrimaryGray1000
import com.magma.virtuhire.ui.theme.PrimaryGray1200
import com.magma.virtuhire.ui.theme.PrimaryGray300
import kotlinx.coroutines.launch

@Composable
fun InterviewScreen(
    jobId: String,
    navigateBack: () -> Unit,
    interviewViewModel: InterviewViewModel = hiltViewModel()
) {
    val state = interviewViewModel.interviewState.value
    val jobState = interviewViewModel.jobState.value
    val newMessage = remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        interviewViewModel.startInterview(jobId)
    }

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var isErrorDialogExpanded by remember { mutableStateOf(false) }
    var isLoadingDialogExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(state.isEnding, state.isLoading) {
        if (state.isError) {
            isErrorDialogExpanded = true
        }
        isLoadingDialogExpanded = state.isLoading && state.isEnding

    }
    BackHandler(
        enabled = !showConfirmationDialog,
        onBack = {
            showConfirmationDialog = true
        }
    )
    ErrorDialog(
        isDialogOpen = isErrorDialogExpanded,
        onDialogClose = { isErrorDialogExpanded = false },
        errorMessage = state.errorMessage ?: ""
    )

    LoadingDialog(isDialogOpen = isLoadingDialogExpanded) {
        isLoadingDialogExpanded = false
    }

    ConfirmationDialog(isConfirmDialogOpen = showConfirmationDialog, onConfirmClick = {
        scope.launch {
            interviewViewModel.saveInterview()
        }
        showConfirmationDialog = false
        navigateBack()
    }, onConfirmDialogClose = {
        showConfirmationDialog = false
    })

    InterviewContent(
        state = state,
        jobState = jobState,
        newMessage = newMessage.value,
        onNewMessageChange = { message -> newMessage.value = message },
        onSendClick = { message ->
            scope.launch {
                interviewViewModel.replyInterview(message)
            }
            newMessage.value = ""
        },
        onEndInterview = {
            scope.launch {
                interviewViewModel.endInterview()
            }
        },
        onBackClick = {
            showConfirmationDialog = true
        }
    )
}

@Composable
fun InterviewContent(
    state: InterviewState,
    jobState: JobDetailResponse?,
    newMessage: String,
    onNewMessageChange: (String) -> Unit,
    onSendClick: (message: String) -> Unit,
    onEndInterview: () -> Unit,
    onBackClick: () -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.interview) {
        if (state.interview.isNotEmpty()) {
            listState.animateScrollToItem(state.interview.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryGray300)
    ) {
        InterviewScreenTopSection(onBackClick = { onBackClick() })
        if (jobState != null) {
            JobOverviewCard(
                name = jobState.company.name,
                title = jobState.title,
                logo = jobState.company.logo,
                onEndInterview = { onEndInterview() }
            )
        } else {
            JobOverviewCardSkeleton()
        }
        Box(
            modifier = Modifier
                .weight(0.9f)
        ) {
            LazyColumn(
                modifier = Modifier,
                contentPadding = PaddingValues(vertical = 10.dp),
                state = listState
            ) {
                items(state.interview) { item ->
                    ChatMessage(message = item.data.content, author = item.type)
                }
                if (state.isLoading) {
                    item {
                        ChatMessage(message = "Loading", author = "ai", forLoading = true)
                    }
                }
            }
        }
        if (state.isInterviewDone) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 5.dp,
                        shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
                        clip = true
                    )
                    .background(PrimaryGray100)
                    .padding(30.dp)
            ) {
                Text(text = "The interview is already ended", color = PrimaryGray1000)
            }
        } else {
            InputSection(
                newMessage = newMessage,
                onNewMessageChange = { onNewMessageChange(it) },
                onSendClick = { onSendClick(newMessage) }
            )
        }

    }
}

@Composable
fun ChatMessage(message: String, author: String, forLoading: Boolean = false) {
    val isUser = author == "human"
    val isFeedback = author == "reviewer"

    Row(
        horizontalArrangement = if (isUser) Arrangement.End else if (isFeedback) Arrangement.Center else Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 8.dp)
    ) {
        Column(
            horizontalAlignment = if (isUser) Alignment.End else if (isFeedback) Alignment.CenterHorizontally else Alignment.Start
        ) {
            Text(
                text = if (isUser) "You" else if (isFeedback) "Feedback" else "Interviewer",
                style = if (isFeedback) MaterialTheme.typography.labelMedium else MaterialTheme.typography.labelSmall,
                color = if (isFeedback) PrimaryGray1200 else PrimaryGray1000
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (forLoading) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = if (isUser) listOf(
                                    Primary500, Primary700
                                ) else listOf(
                                    PrimaryGray100, PrimaryGray100
                                ),
                                startY = 0.0f,
                                endY = 300.0f
                            ),
                            shape = RoundedCornerShape(
                                topEnd = 10.dp,
                                topStart = 10.dp,
                                bottomEnd = if (isUser) 0.dp else 10.dp,
                                bottomStart = if (isUser) 10.dp else 0.dp
                            )
                        )
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                )
                {
                    DotsTyping()
                }
            } else {
                Text(
                    text = message,
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = if (isUser) listOf(
                                    Primary500, Primary700
                                ) else if (isFeedback) listOf(Primary50, Primary50) else listOf(
                                    PrimaryGray100, PrimaryGray100
                                ),
                                startY = 0.0f,
                                endY = 300.0f
                            ),
                            shape = RoundedCornerShape(
                                topEnd = 10.dp,
                                topStart = 10.dp,
                                bottomEnd = if (isUser) 0.dp else 10.dp,
                                bottomStart = if (isUser || isFeedback) 10.dp else 0.dp
                            )
                        )
                        .border(
                            border = BorderStroke(
                                color = if (isFeedback) Primary200 else Color.Transparent,
                                width = if (isFeedback) 2.dp else 0.dp
                            ), shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    color = if (isUser) PrimaryGray100 else PrimaryGray1200
                )
            }

        }
    }
}

@Composable
fun InputSection(
    newMessage: String,
    onNewMessageChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
                clip = true
            )
            .background(PrimaryGray100)
            .padding(20.dp)
    ) {
        InputField(
            value = newMessage,
            onValueChange = onNewMessageChange,
            modifier = Modifier.weight(1f),
        )
        PrimaryIconButton(
            onClick = onSendClick,
            modifier = Modifier.height(55.dp),
        ) {
            Icon(Icons.Filled.Send, "Send")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterviewScreenTopSection(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        modifier = modifier
            .zIndex(25f)
            .background(PrimaryGray100)
            .padding(vertical = 15.dp),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = PrimaryGray100,
            titleContentColor = PrimaryGray1200,
            navigationIconContentColor = PrimaryGray1200,
            actionIconContentColor = PrimaryGray1200
        ),
        title = {
            Text(
                "Interview",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "Localized description"
                )
            }
        },
    )
}

@Composable
fun JobOverviewCardSkeleton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp),
                clip = true
            )
            .background(PrimaryGray100)
            .padding(15.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp),
            contentAlignment = Alignment.Center
        ) {
            DotsTyping()
        }

    }
}

@Composable
fun JobOverviewCard(name: String, title: String, logo: String, onEndInterview: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(20f)
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp),
                clip = true
            )
            .background(PrimaryGray100)
            .padding(15.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(PrimaryGray100)
        ) {
            AsyncImage(modifier = Modifier.size(64.dp), model = logo, contentDescription = "Image")
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                color = PrimaryGray1000
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                PrimaryButton(
                    text = "End Interview",
                    variant = ButtonVariant.Lighter,
                    onClick = { onEndInterview() },
                    size = ButtonSize.Normal,
                    modifier = Modifier.width(200.dp)
                )

            }
        }
    }
}

@Preview
@Composable
fun ChatContentPreview() {
    val interviewState = InterviewState(
//        interview = listOf(
//            InterviewChat("Hi, How are you?", Author.Interviewer),
//            InterviewChat("I'm fine", Author.User)
//        ),
//        isLoading = false,
//        isError = false,
//        errorMessage = null
    )
//    InterviewContent(
//        interviewState,
//        null,
//        newMessage = "",
//        onSendClick = { _ -> },
//        onNewMessageChange = { _ -> }
    JobOverviewCard(name = "Coba", title = "What is that", logo = "", onEndInterview = {})
}
