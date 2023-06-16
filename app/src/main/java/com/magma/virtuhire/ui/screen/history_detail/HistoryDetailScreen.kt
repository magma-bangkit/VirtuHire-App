package com.magma.virtuhire.ui.screen.history_detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.magma.virtuhire.ui.components.common.ButtonSize
import com.magma.virtuhire.ui.components.common.ButtonVariant
import com.magma.virtuhire.ui.components.common.ErrorDialog
import com.magma.virtuhire.ui.components.common.LoadingDialog
import com.magma.virtuhire.ui.components.common.PrimaryButton
import com.magma.virtuhire.ui.screen.interview.ChatMessage
import com.magma.virtuhire.ui.screen.interview.InputSection
import com.magma.virtuhire.ui.screen.interview.InterviewScreenTopSection
import com.magma.virtuhire.ui.screen.interview.JobOverviewCard
import com.magma.virtuhire.ui.screen.interview.JobOverviewCardSkeleton
import com.magma.virtuhire.ui.theme.PrimaryGray100
import com.magma.virtuhire.ui.theme.PrimaryGray1000
import com.magma.virtuhire.ui.theme.PrimaryGray300
import kotlinx.coroutines.launch

@Composable
fun HistoryDetailScreen(
    sessionId: String,
    navigateBack: () -> Unit,
    historyDetailViewModel: HistoryDetailViewModel = hiltViewModel()
) {
    val state = historyDetailViewModel.interviewState.value
    val saveInterviewState = historyDetailViewModel.saveInterviewState.value
    val newMessage = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        historyDetailViewModel.getInterviewHistory(sessionId)
    }

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var isErrorDialogExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(saveInterviewState.isError) {
        if (saveInterviewState.isError) {
            isErrorDialogExpanded = true
        }
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
        errorMessage = saveInterviewState.errorMessage ?: ""
    )

    ConfirmationDialog(isConfirmDialogOpen = showConfirmationDialog, onConfirmClick = {
        scope.launch {
            historyDetailViewModel.saveInterview()
        }
        showConfirmationDialog = false
        navigateBack()

    }, onConfirmDialogClose = {
        showConfirmationDialog = false
    })

    HistoryDetailContent(
        state = state,
        newMessage = newMessage.value,
        onNewMessageChange = { message -> newMessage.value = message },
        onSendClick = { message ->
            scope.launch {
                historyDetailViewModel.replyInterview(message)
            }
            newMessage.value = ""
        },
        onEndInterview = {
            scope.launch {
                historyDetailViewModel.endInterview()
            }
        },
        onBackClick = {
            showConfirmationDialog = true
        }
    )
}

@Composable
fun HistoryDetailContent(
    state: InterviewHistoryDetailState,
    onEndInterview: () -> Unit, newMessage: String,
    onNewMessageChange: (String) -> Unit,
    onSendClick: (message: String) -> Unit,
    onBackClick: () -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.chatHistory) {
        if (state.chatHistory.isNotEmpty()) {
            listState.animateScrollToItem(
                ((state.interviews?.chatHistories?.size ?: 0) + state.chatHistory.size) - 1
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryGray300)
    ) {
        InterviewScreenTopSection(onBackClick = { onBackClick() })
        if (state.interviews != null) {
            JobOverviewCard(
                name = state.interviews.job.company.name,
                title = state.interviews.job.title,
                logo = state.interviews.job.company.logo,
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
                if (state.interviews != null) {
                    items(state.interviews.chatHistories) { item ->
                        ChatMessage(message = item.data.content, author = item.type)
                    }
                    // when user continues
                    items(state.chatHistory) { item ->
                        ChatMessage(message = item.data.content, author = item.type)
                    }
                }
                if (state.isLoading) {
                    item {
                        ChatMessage(message = "Loading", author = "ai", forLoading = true)
                    }
                }
            }
        }
        if (state.isInterviewDone || state.interviews?.isDone == true) {
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
fun ConfirmationDialog(
    modifier: Modifier = Modifier,
    isConfirmDialogOpen: Boolean,
    onConfirmDialogClose: () -> Unit,
    onConfirmClick: () -> Unit
) {
    if (isConfirmDialogOpen) {
        Dialog(
            onDismissRequest = { onConfirmDialogClose() },
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PrimaryGray100)
                        .padding(20.dp)
                ) {
                    androidx.compose.material.Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Confirmation",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    androidx.compose.material.Text(
                        text = "Are you sure you want to quit this interview? (You can continue later)",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        PrimaryButton(
                            size = ButtonSize.Normal,
                            variant = ButtonVariant.Light,
                            onClick = { onConfirmDialogClose() },
                            modifier = Modifier.weight(1f),
                            text = "No"
                        )
                        PrimaryButton(
                            size = ButtonSize.Normal,
                            onClick = {
                                onConfirmClick()
                            },
                            modifier = Modifier.weight(1f),
                            text = "Yes"
                        )
                    }
                }
            }
        }
    }
}