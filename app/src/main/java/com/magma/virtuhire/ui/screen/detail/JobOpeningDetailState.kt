package com.magma.virtuhire.ui.screen.detail

import com.magma.virtuhire.data.remote.response.JobDetailResponse

data class JobOpeningDetailState(
    val job: JobDetailResponse? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null
)