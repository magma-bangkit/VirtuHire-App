package com.magma.virtuhire.ui.screen.detail

import android.graphics.Paint.Align
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.magma.virtuhire.R
import com.magma.virtuhire.data.remote.response.JobDetailResponse
import com.magma.virtuhire.data.remote.response.JobType
import com.magma.virtuhire.ui.components.common.ButtonSize
import com.magma.virtuhire.ui.components.common.ButtonVariant
import com.magma.virtuhire.ui.components.common.DotsTyping
import com.magma.virtuhire.ui.components.common.PrimaryButton
import com.magma.virtuhire.ui.screen.home.HomeViewModel
import com.magma.virtuhire.ui.screen.home.JobOpeningListState
import com.magma.virtuhire.ui.theme.Primary500
import com.magma.virtuhire.ui.theme.Primary700
import com.magma.virtuhire.ui.theme.PrimaryGray100
import com.magma.virtuhire.ui.theme.PrimaryGray1000
import com.magma.virtuhire.ui.theme.PrimaryGray200
import com.magma.virtuhire.ui.theme.PrimaryGray300
import com.magma.virtuhire.ui.theme.PrimaryGray500
import com.magma.virtuhire.ui.theme.PrimaryGray700
import com.magma.virtuhire.ui.theme.VirtuHireTheme
import com.magma.virtuhire.utils.getDisplayText
import com.magma.virtuhire.utils.getDisplayTextFromString
import com.valentinilk.shimmer.shimmer

@Composable
fun DetailScreen(
    jobId: String,
    navigateBack: () -> Unit,
    navigateToInterview: () -> Unit,
    detailViewModel: DetailViewModel = hiltViewModel()
) {
    val state = detailViewModel.jobState.value

    LaunchedEffect(Unit) {
        detailViewModel.getJobById(jobId)
    }

    DetailContent(
        state = state,
        navigateBack = { navigateBack() },
        navigateToInterview = { navigateToInterview() }
    )
}

@Composable
fun DetailContent(
    modifier: Modifier = Modifier,
    state: JobOpeningDetailState,
    navigateBack: () -> Unit,
    navigateToInterview: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(),
            modifier = modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Primary500, Primary700
                        ),
                        startY = 0f,
                    )
                )
        ) {
            item {
                DetailScreenTopSection(onBackClick = { navigateBack() })
            }
            if (state.job != null) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .shadow(
                                elevation = 50.dp,
                                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                                clip = true
                            )
                            .background(color = PrimaryGray100)
                            .padding(20.dp)
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = state.job.title,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${state.job.company.name} - ${state.job.city.name}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = PrimaryGray1000,
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        Row(
                            modifier = Modifier
                                .height(IntrinsicSize.Min)
                                .fillMaxWidth()
                                .border(
                                    border = BorderStroke(width = 2.dp, color = PrimaryGray500),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(15.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(0.425f)
                                    .fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Call,
                                    contentDescription = "Icon",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .graphicsLayer(alpha = 0.99f)
                                        .drawWithCache {
                                            onDrawWithContent {
                                                drawContent()
                                                drawRect(
                                                    brush = Brush.verticalGradient(
                                                        colors = listOf(
                                                            Primary500, Primary700
                                                        ),
                                                        startY = 0f,
                                                    ), blendMode = BlendMode.SrcAtop
                                                )
                                            }
                                        },
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Job Type",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = PrimaryGray1000,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = getDisplayTextFromString(state.job.jobType),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Divider(
                                color = PrimaryGray500,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(2.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .weight(0.425f)
                                    .fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Icon",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .graphicsLayer(alpha = 0.99f)
                                        .drawWithCache {
                                            onDrawWithContent {
                                                drawContent()
                                                drawRect(
                                                    brush = Brush.verticalGradient(
                                                        colors = listOf(
                                                            Primary500, Primary700
                                                        ),
                                                        startY = 0f,
                                                    ), blendMode = BlendMode.SrcAtop
                                                )
                                            }
                                        },
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Salary",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = PrimaryGray1000,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = "Rp ${state.job.salaryTo}",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Requirements",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        BulletList(
                            state.job.requirements
                        )
                        Spacer(modifier = Modifier.height(15.dp))

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Responsibilities",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        BulletList(
                            state.job.responsibilities
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Company Information",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        CompanyInformation(
                            companyName = state.job.company.name,
                            companyDescription = state.job.company.description ?: "",
                            companyLogo = state.job.company.logo,
                            location = state.job.city.name
                        )
                    }
                }

            }

            if (state.isLoading) {
                item {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(800.dp)
                            .shadow(
                                elevation = 50.dp,
                                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                                clip = true
                            )
                            .background(color = PrimaryGray100)
                            .padding(20.dp)
                    ) {
                        DotsTyping()
                    }
                }

            }
        }

        // Apply Job and Start Interview button
        if (state.job != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent, PrimaryGray100
                            ),
                            startY = 0.0f,
                            endY = 75f
                        )
                    )
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = 25.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PrimaryButton(
                        onClick = { /* Handle button click */ },
                        text = "Apply Job",
                        size = ButtonSize.Normal,
                        variant = ButtonVariant.Light,
                        modifier = Modifier.weight(1f)
                    )
                    PrimaryButton(
                        onClick = { navigateToInterview() },
                        text = "Start Interview",
                        size = ButtonSize.Normal,
                        modifier = Modifier.weight(1f)
                    )
                }

            }
        }
    }

//    if (state.isError) {
//        Text(
//            text = state.errorMessage ?: "Something went wrong",
//            style = TextStyle(
//                color = Color.DarkGray,
//                fontSize = 40.sp,
//                textAlign = TextAlign.Center
//            )
//        )
//    }
//
//    if (state.isLoading) {
//        CircularProgressIndicator(
//            color = Color.DarkGray,
//        )
//    }
}

@Composable
fun BulletList(items: List<String>) {
    val bullet = "\u2022"
    val bulletColor = Color.Gray

    Column {
        items.forEach { item ->
            val listItem = buildAnnotatedString {
                pushStyle(
                    style = SpanStyle(
                        color = bulletColor,
                        fontWeight = FontWeight.Bold
                    )
                )
                appendInlineContent("bullet", bullet)
                append(" ")
                pop()
                append(item)
            }
            Text(
                text = listItem,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = PrimaryGray1000
            )
        }
    }
}

@Composable
fun CompanyInformation(
    companyName: String,
    companyDescription: String,
    companyLogo: String,
    location: String,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(10.dp)),
                model = companyLogo,
                contentDescription = "Logo"
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = companyName, style = MaterialTheme.typography.titleLarge)
            Text(
                text = location,
                style = MaterialTheme.typography.bodyLarge,
                color = PrimaryGray1000
            )
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        modifier = Modifier.padding(bottom = 50.dp),
        text = companyDescription,
        style = MaterialTheme.typography.bodyMedium,
        color = PrimaryGray1000
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenTopSection(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        modifier = modifier.padding(vertical = 15.dp),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = PrimaryGray100,
            navigationIconContentColor = PrimaryGray100,
            actionIconContentColor = PrimaryGray100
        ),
        title = {
            Text(
                "Job Detail",
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
        actions = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun DetailContentPreview() {
    VirtuHireTheme {
        DetailContent(state = JobOpeningDetailState(), navigateToInterview = {}, navigateBack = {})
    }
}