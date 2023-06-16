package com.magma.virtuhire.data.remote.request

data class StartInterviewRequest(
    val jobId: String
)

data class ReplyInterviewRequest(
    val sessionId: String,
    val message: String
)

data class EndInterviewRequest(
    val sessionId: String
)

data class SaveInterviewRequest(
    val sessionId: String
)
