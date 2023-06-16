package com.magma.virtuhire.ui.screen.history

import com.magma.virtuhire.data.remote.response.InterviewHistoryResponse

data class InterviewHistoryListState(
    val interviews: List<InterviewHistoryResponse> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null
)