package com.magma.virtuhire.ui.screen.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.magma.virtuhire.data.remote.response.CityResponse
import com.magma.virtuhire.data.remote.response.Company
import com.magma.virtuhire.data.remote.response.Coordinate
import com.magma.virtuhire.data.remote.response.InterviewHistoryResponse
import com.magma.virtuhire.data.remote.response.JobCategoryResponse
import com.magma.virtuhire.data.remote.response.JobOpeningResponse
import com.magma.virtuhire.data.remote.response.SkillResponse
import com.magma.virtuhire.ui.components.common.DotsTyping
import com.magma.virtuhire.ui.components.layout.bottombar.BottomBar
import com.magma.virtuhire.ui.theme.Primary500
import com.magma.virtuhire.ui.theme.PrimaryGray100
import com.magma.virtuhire.ui.theme.PrimaryGray1000
import com.magma.virtuhire.ui.theme.PrimaryGray1200
import com.magma.virtuhire.ui.theme.PrimaryGray300
import com.magma.virtuhire.ui.theme.VirtuHireTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    navigateToDetail: (String) -> Unit,
    historyViewModel: HistoryViewModel = hiltViewModel()
) {
    val state = historyViewModel.interviewState.value
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = PrimaryGray300)) {
        HistoryContent(modifier = modifier, state = state, navigateToDetail = { navigateToDetail(it) })
        BottomBar(navController = navController, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun HistoryContent(
    modifier: Modifier = Modifier,
    state: InterviewHistoryListState,
    navigateToDetail: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        item {
            Text(
                modifier = Modifier.padding(20.dp),
                text = "History",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold
                ),
            )
        }
        if (state.isLoading) {
            item {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.height(800.dp)) {
                    DotsTyping()
                }
            }
        }
        JobList(state, onJobCardClick = { navigateToDetail(it) })
    }
}

fun LazyListScope.JobList(state: InterviewHistoryListState, onJobCardClick: (String) -> Unit) {
    items(items = state.interviews, key = { it.id }) { it ->
        JobCard(interview = it, onJobCardClick = { id -> onJobCardClick(id) })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobCard(interview: InterviewHistoryResponse, onJobCardClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(PrimaryGray100),
        onClick = { onJobCardClick(interview.id) }

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(PrimaryGray100)
                        .width(50.dp)
                        .height(50.dp)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        model = interview.job.company.logo,
                        contentDescription = "Job Logo"
                    )
                }
                Column {
                    Text(
                        text = "Mock Interview for",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = PrimaryGray1000
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = interview.job.company.name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = PrimaryGray1200
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = interview.job.title, style = MaterialTheme.typography.titleLarge, maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val dateTime = LocalDateTime.parse(interview.createdAt, formatter)
                val displayFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                Text(
                    text = "Conducted on ${dateTime.format(displayFormatter)}",
                    style = MaterialTheme.typography.labelLarge,
                    color = Primary500
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
    val jobOpeningResponse = JobOpeningResponse(
        id = "u5TxFf-d4nnAnZf3ErWlr",
        createdAt = "2023-06-11T18:56:36.475Z",
        updatedAt = "2023-06-11T18:56:36.475Z",
        title = "Warehouse Assistant",
        description = "Kami adalah Style Theory, platform fesyen melingkar terbesar di Asia Tenggara. Untuk setiap pecinta mode yang sadar nilai, miskin waktu, dan cerdas, kami adalah solusinya untuk budaya beli-lempar-dan-penyesalan saat ini, menawarkan cara yang lebih cerdas dan berkelanjutan untuk mengeksplorasi mode.",
        source = "https://glints.com/id/opportunities/jobs/8079650c-ca23-4c44-94b6-7bb8c7bec1c4",
        jobType = "FULL_TIME",
        salaryFrom = 5000000,
        salaryTo = 5500000,
        category = JobCategoryResponse(
            id = 7,
            name = "Supply Chain",
            createdAt = "2023-06-11T18:56:36.475Z",
            updatedAt = "2023-06-11T18:56:36.475Z",
        ),
        company = Company(
            id = "jNoGb_xfq6JEUwajKjFiN",
            createdAt = "2023-06-11T18:55:59.850Z",
            updatedAt = "2023-06-11T18:55:59.850Z",
            deletedAt = null,
            version = 1,
            name = "StyleTheory",
            description = "",
            logo = "https://storage.googleapis.com/virtuhire-public/media/company-logo/47wBHoiM6nmH6fuleIJAE.jpg"
        ),
        city = CityResponse(
            id = 195,
            name = "Kota Jakarta",
            coordinate = Coordinate(
                "Point", listOf(
                    96.1527,
                    4.4543
                )
            ),
            createdAt = "2023-06-11T18:56:36.475Z",
            updatedAt = "2023-06-11T18:56:36.475Z",
        ),
        skillRequirements = listOf(
            SkillResponse(
                id = 1319,
                name = "Smartphones",
                createdAt = "2023-06-11T18:56:36.475Z",
                updatedAt = "2023-06-11T18:56:36.475Z",
            ),
            SkillResponse(
                id = 1318,
                name = "Warehouse Staff",
                createdAt = "2023-06-11T18:56:36.475Z",
                updatedAt = "2023-06-11T18:56:36.475Z",
            ),
            SkillResponse(
                id = 1,
                name = "Administration",
                createdAt = "2023-06-11T18:56:36.475Z",
                updatedAt = "2023-06-11T18:56:36.475Z",
            ),
            SkillResponse(
                id = 132,
                name = "Computer",
                createdAt = "2023-06-11T18:56:36.475Z",
                updatedAt = "2023-06-11T18:56:36.475Z",
            )
        )
    )

    VirtuHireTheme {}
}

