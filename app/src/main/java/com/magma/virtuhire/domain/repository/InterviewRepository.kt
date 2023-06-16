package com.magma.virtuhire.domain.repository

import com.magma.virtuhire.data.remote.request.EndInterviewRequest
import com.magma.virtuhire.data.remote.request.ReplyInterviewRequest
import com.magma.virtuhire.data.remote.request.SaveInterviewRequest
import com.magma.virtuhire.data.remote.request.StartInterviewRequest
import com.magma.virtuhire.data.remote.response.EndInterviewResponse
import com.magma.virtuhire.data.remote.response.InterviewHistoryResponse
import com.magma.virtuhire.data.remote.response.ReplyInterviewResponse
import com.magma.virtuhire.data.remote.response.StartInterviewResponse
import com.magma.virtuhire.domain.model.InterviewChat
import com.magma.virtuhire.utils.Resource
import kotlinx.coroutines.flow.Flow

interface InterviewRepository {
    suspend fun getInterviewHistories(): Flow<Resource<List<InterviewHistoryResponse>>>
    suspend fun getInterviewHistory(id: String): Flow<Resource<InterviewHistoryResponse>>
    suspend fun saveInterview(saveInterviewRequest: SaveInterviewRequest): Flow<Resource<InterviewHistoryResponse>>
    suspend fun startInterview(startInterviewRequest: StartInterviewRequest): Flow<Resource<StartInterviewResponse>>
    suspend fun replyInterview(replyInterviewRequest: ReplyInterviewRequest): Flow<Resource<ReplyInterviewResponse>>
    suspend fun endInterview(endInterviewRequest: EndInterviewRequest): Flow<Resource<EndInterviewResponse>>
}