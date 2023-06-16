package com.magma.virtuhire.ui.screen.find

import com.magma.virtuhire.data.remote.response.JobOpeningResponse

data class JobOpeningListState(
    val jobs: List<JobOpeningResponse> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null
)