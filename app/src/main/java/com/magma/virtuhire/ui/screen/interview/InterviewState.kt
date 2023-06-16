package com.magma.virtuhire.ui.screen.interview

import com.magma.virtuhire.data.remote.response.ReplyInterviewResponse
import com.magma.virtuhire.domain.model.InterviewChat

data class InterviewState(
    val interview: List<ReplyInterviewResponse> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val isInterviewDone: Boolean = false,
    val isSaving: Boolean = false,
    val isEnding: Boolean = false,
)