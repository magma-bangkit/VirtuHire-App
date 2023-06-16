package com.magma.virtuhire.ui.screen.find

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.magma.virtuhire.data.remote.response.CityResponse
import com.magma.virtuhire.data.remote.response.Company
import com.magma.virtuhire.data.remote.response.Coordinate
import com.magma.virtuhire.data.remote.response.JobCategoryResponse
import com.magma.virtuhire.data.remote.response.JobOpeningResponse
import com.magma.virtuhire.data.remote.response.SkillResponse
import com.magma.virtuhire.ui.components.layout.bottombar.BottomBar
import com.magma.virtuhire.ui.theme.Primary100
import com.magma.virtuhire.ui.theme.Primary500
import com.magma.virtuhire.ui.theme.Primary700
import com.magma.virtuhire.ui.theme.PrimaryGray100
import com.magma.virtuhire.ui.theme.PrimaryGray1000
import com.magma.virtuhire.ui.theme.PrimaryGray300
import com.magma.virtuhire.ui.theme.PrimaryGray700
import com.magma.virtuhire.ui.theme.VirtuHireTheme
import com.magma.virtuhire.utils.truncateText
import androidx.paging.compose.items
import com.magma.virtuhire.ui.components.common.DotsTyping
import com.magma.virtuhire.ui.components.common.InputField
import com.magma.virtuhire.ui.theme.PrimaryGray1200
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun <T : Any> LazyPagingItems<T>.rememberLazyListState(): LazyListState {
    // After recreation, LazyPagingItems first return 0 items, then the cached items.
    // This behavior/issue is resetting the LazyListState scroll position.
    // Below is a workaround. More info: https://issuetracker.google.com/issues/177245496.
    return when (itemCount) {
        // Return a different LazyListState instance.
        0 -> remember(this) { LazyListState(0, 0) }
        // Return rememberLazyListState (normal case).
        else -> androidx.compose.foundation.lazy.rememberLazyListState()
    }
}


@Composable
fun FindScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    navigateToDetail: (String) -> Unit,
    navigateBack: () -> Unit,
    findViewModel: FindViewModel = hiltViewModel()
) {
    val job = findViewModel.jobsPaging.collectAsLazyPagingItems()
    val searchJob = findViewModel.jobState.value
    val listState = job.rememberLazyListState()
    val scope = rememberCoroutineScope()

    FindContent(
        modifier = modifier,
        job = job,
        searchJob = searchJob,
        navigateToDetail = { navigateToDetail(it) },
        navigateBack = { navigateBack() },
        listState = listState,
        onSearchQueryChange = {
            scope.launch {
                findViewModel.searchJobs(it)
            }
        },
        onSearchClicked = {
            scope.launch {
                findViewModel.searchJobs(it)
            }
        }
    )
}

@Composable
fun FindContent(
    modifier: Modifier = Modifier,
    job: LazyPagingItems<JobOpeningResponse>,
    searchJob: JobOpeningListState,
    navigateToDetail: (String) -> Unit,
    navigateBack: () -> Unit,
    listState: LazyListState,
    onSearchQueryChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .background(color = PrimaryGray300),
        state = listState
    ) {
        item {
            FindScreenTopSection(onBackClick = { navigateBack() })
        }

        item {
            InputField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                onDoneClicked = {
                    onSearchClicked(searchQuery)
                },
                label = "Search",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            )
        }

        if (searchQuery.isNotEmpty()) {
            items(searchJob.jobs) {
                JobCard(job = it, onJobCardClick = { id -> navigateToDetail(id) })
            }
        }

        JobList(job = job, onJobCardClick = { navigateToDetail(it) })
    }
}

fun LazyListScope.JobList(
    job: LazyPagingItems<JobOpeningResponse>,
    onJobCardClick: (String) -> Unit
) {
    items(items = job, key = { it.id }) {
        if (it != null) {
            JobCard(job = it, onJobCardClick = { id -> onJobCardClick(id) })
        }
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
                        .background(PrimaryGray100)
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
fun FindScreenTopSection(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        modifier = modifier.padding(vertical = 15.dp),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = PrimaryGray1200,
            navigationIconContentColor = PrimaryGray1200,
            actionIconContentColor = PrimaryGray1200,
        ),
        title = {
            Text(
                "Find",
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
    }
}

