package com.magma.virtuhire.data.remote.response

import com.google.gson.annotations.SerializedName

// For Historu
data class InterviewHistoryResponse(
    @SerializedName("id") val id: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("deletedAt") val deletedAt: String?,
    @SerializedName("_v") val version: Int,
    @SerializedName("chatHistories") val chatHistories: List<ChatHistory>,
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("isDone") val isDone: Boolean,
    @SerializedName("jobOpening") val job: JobMiniResponse
)

data class JobMiniResponse(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("company") val company: CompanyMiniResponse
)

data class CompanyMiniResponse(
    @SerializedName("id") val id: String,   
    @SerializedName("name") val name: String,
    @SerializedName("logo") val logo: String,
)

data class ChatHistory(
    @SerializedName("data") val data: ChatData,
    @SerializedName("type") val type: String
)

data class ChatData(
    @SerializedName("content") val content: String
)

// For Chat
data class StartInterviewResponse(
    @SerializedName("data") val data: ChatData,
    @SerializedName("type") val type: String,
    @SerializedName("job") val job: JobDetailResponse?,
    @SerializedName("sessionId") val sessionId: String
)

data class ReplyInterviewResponse(
    @SerializedName("data") val data: ChatData,
    @SerializedName("type") val type: String,
    @SerializedName("isDone") val isDone: Boolean
)

data class EndInterviewResponse(
    @SerializedName("data") val data: ChatData,
    @SerializedName("type") val type: String,
    @SerializedName("isDone") val isDone: Boolean
)

enum class ChatType {
    HUMAN,
    AI,
    REVIEWER
}