package com.magma.virtuhire.data.mock

import com.magma.virtuhire.domain.model.InterviewChat

interface MockInterviewDataSource {
    suspend fun startInterview(): InterviewChat
    suspend fun sendMessage(): InterviewChat
}