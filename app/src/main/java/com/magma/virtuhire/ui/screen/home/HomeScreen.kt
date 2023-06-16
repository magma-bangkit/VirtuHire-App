package com.magma.virtuhire.ui.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.magma.virtuhire.data.remote.response.JobCategoryResponse
import com.magma.virtuhire.data.remote.response.JobOpeningResponse
import com.magma.virtuhire.data.remote.response.SkillResponse
import com.magma.virtuhire.domain.model.JobOpening
import com.magma.virtuhire.ui.components.common.DotsTyping
import com.magma.virtuhire.ui.components.layout.bottombar.BottomBar
import com.magma.virtuhire.ui.theme.Primary100
import com.magma.virtuhire.ui.theme.Primary500
import com.magma.virtuhire.ui.theme.Primary700
import com.magma.virtuhire.ui.theme.PrimaryGray100
import com.magma.virtuhire.ui.theme.PrimaryGray1000
import com.magma.virtuhire.ui.theme.PrimaryGray300
import com.magma.virtuhire.ui.theme.PrimaryGray500
import com.magma.virtuhire.ui.theme.PrimaryGray700
import com.magma.virtuhire.ui.theme.VirtuHireTheme
import com.magma.virtuhire.utils.truncateText

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    navigateToDetail: (String) -> Unit,
    navigateToFind: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val state = homeViewModel.jobState.value
    val profileState = homeViewModel.profileState.value
    val recommendedJobsState = homeViewModel.recommendedJobState.value
    val fallbackJobsState = homeViewModel.fallbackJobState.value

    Box(modifier = Modifier.fillMaxSize()) {
        HomeContent(
            modifier = modifier,
            state = state,
            recommendedJobsState = recommendedJobsState,
            fallbackJobsState = fallbackJobsState,
            profileState = profileState,
            navigateToDetail = { navigateToDetail(it) },
            navigateToFind = { navigateToFind() })
        BottomBar(navController = navController, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    state: JobOpeningListState,
    recommendedJobsState: JobOpeningListState,
    fallbackJobsState: JobOpeningListState,
    profileState: ProfileState,
    navigateToDetail: (String) -> Unit,
    navigateToFind: () -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = PrimaryGray300)
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "virtuhire",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Primary500, Primary700
                            ),
                            startY = 0f,
                        )
                    ),
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .clickable {}
                        .background(PrimaryGray500)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Account",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }


        if (state.isLoading || recommendedJobsState.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(700.dp), contentAlignment = Alignment.Center
                ) {
                    DotsTyping()
                }
            }
        } else if (state.isError || recommendedJobsState.isError) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(700.dp), contentAlignment = Alignment.Center
                ) {
                    DotsTyping()
                }
            }
        } else {
            item {
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = "Hi, ${profileState.profile?.firstName ?: "User"}",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier =
                    modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(PrimaryGray100)
                        .border(
                            border = BorderStroke(color = PrimaryGray500, width = 2.dp),
                            shape = RoundedCornerShape(11.dp)
                        )
                        .clickable { navigateToFind() }
                        .padding(vertical = 20.dp, horizontal = 20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = PrimaryGray1000
                    )
                    Text(text = "Find a job", color = PrimaryGray1000)
                }
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    items(items = recommendedJobsState.jobs.ifEmpty {
                        fallbackJobsState.jobs
                    }, key = { it.id }) {
                        RecommendedJobCard(
                            job = it,
                            onJobCardClick = { id -> navigateToDetail(id) })
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "Recent Jobs",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = PrimaryGray1000
                    )

                    Text(
                        text = "See All", style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Primary500
                    )
                }
            }

            JobList(state, onJobCardClick = { navigateToDetail(it) })
        }

    }
}

fun LazyListScope.JobList(state: JobOpeningListState, onJobCardClick: (String) -> Unit) {
    items(items = state.jobs, key = { it.id }) { it ->
        JobCard(job = it, onJobCardClick = { id -> onJobCardClick(id) })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobCard(job: JobOpeningResponse, onJobCardClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(PrimaryGray100),
        onClick = { onJobCardClick(job.id) }

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
                        .width(50.dp)
                        .height(50.dp)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        model = job.company.logo,
                        contentDescription = "Job Logo"
                    )
                }
                Column {
                    Text(
                        text = job.company.name,
                        style = MaterialTheme.typography.labelLarge,
                        color = PrimaryGray1000
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = job.title, style = MaterialTheme.typography.titleLarge, maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            if (job.description != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = truncateText(job.description ?: "", 20), // Limit to 20 words
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal),
                    color = PrimaryGray1000,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .width(15.dp)
                            .height(15.dp),
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Location",
                        tint = PrimaryGray1000
                    )
                    Text(
                        text = job.city.name, style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Medium
                        ), color = PrimaryGray1000
                    )
                }
                Text(
                    text = "Rp${job.salaryFrom}",
                    style = MaterialTheme.typography.labelLarge,
                    color = Primary500
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendedJobCard(job: JobOpeningResponse, onJobCardClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .width(300.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(PrimaryGray100),
        onClick = { onJobCardClick(job.id) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .size(50.dp)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    model = job.company.logo,
                    contentDescription = "Job Logo"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column() {
                Text(
                    text = job.company.name,
                    style = MaterialTheme.typography.labelLarge,
                    color = PrimaryGray1000,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = job.title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                items(job.skillRequirements.take(5)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .background(Primary100)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = Primary700
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .width(15.dp)
                            .height(15.dp),
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Location",
                        tint = PrimaryGray1000
                    )
                    Text(
                        text = job.city.name,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = PrimaryGray1000
                    )
                }
                Text(
                    text = "Rp${job.salaryFrom}",
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

    VirtuHireTheme {
        RecommendedJobCard(job = jobOpeningResponse, onJobCardClick = {})
    }
}

