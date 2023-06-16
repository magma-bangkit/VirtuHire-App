package com.magma.virtuhire.ui.screen.interview

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magma.virtuhire.data.remote.request.EndInterviewRequest
import com.magma.virtuhire.data.remote.request.ReplyInterviewRequest
import com.magma.virtuhire.data.remote.request.SaveInterviewRequest
import com.magma.virtuhire.data.remote.request.StartInterviewRequest
import com.magma.virtuhire.data.remote.response.ChatData
import com.magma.virtuhire.data.remote.response.JobDetailResponse
import com.magma.virtuhire.data.remote.response.ReplyInterviewResponse
import com.magma.virtuhire.domain.model.Author
import com.magma.virtuhire.domain.model.InterviewChat
import com.magma.virtuhire.domain.repository.InterviewRepository
import com.magma.virtuhire.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterviewViewModel @Inject constructor(
    private val interviewRepository: InterviewRepository
) : ViewModel() {
    private val _sessionState = mutableStateOf("")
    private val sessionState: State<String> = _sessionState

    private val _interviewState = mutableStateOf(InterviewState())
    val interviewState: State<InterviewState> = _interviewState

    private val _jobState = mutableStateOf<JobDetailResponse?>(null)
    val jobState: State<JobDetailResponse?> = _jobState

    fun startInterview(jobId: String) {
        viewModelScope.launch {
            interviewRepository.startInterview(StartInterviewRequest(jobId)).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val interview = result.data
                        if (interview != null) {
                            _jobState.value = interview.job
                            _sessionState.value = interview.sessionId
                            val updatedInterviewState = _interviewState.value.copy(
                                isLoading = false,
                                interview = _interviewState.value.interview + ReplyInterviewResponse(
                                    data = interview.data,
                                    type = interview.type,
                                    isDone = false
                                ),
                                errorMessage = null
                            )
                            _interviewState.value = updatedInterviewState
                        }
                    }
                    is Resource.Error -> {
                        val errorMessage = result.message
                        val updatedInterviewState = _interviewState.value.copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = errorMessage
                        )
                        _interviewState.value = updatedInterviewState
                    }
                    is Resource.Loading -> {
                        val updatedInterviewState = _interviewState.value.copy(isLoading = true)
                        _interviewState.value = updatedInterviewState
                    }
                }
            }
        }
    }

    fun replyInterview(message: String) {
        val updatedInterview = _interviewState.value.interview + ReplyInterviewResponse(ChatData(message), "human", false)
        _interviewState.value = _interviewState.value.copy(interview = updatedInterview)
        viewModelScope.launch {
            interviewRepository.replyInterview(ReplyInterviewRequest(sessionState.value, message)).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val interview = result.data
                        if (interview != null) {
                            val updatedInterviewState = _interviewState.value.copy(
                                isLoading = false,
                                interview = _interviewState.value.interview + ReplyInterviewResponse(
                                    data = interview.data,
                                    type = interview.type,
                                    isDone = interview.isDone
                                ),
                                errorMessage = null
                            )
                            _interviewState.value = updatedInterviewState
                        }
                    }
                    is Resource.Error -> {
                        val errorMessage = result.message
                        val updatedInterviewState = _interviewState.value.copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = errorMessage
                        )
                        _interviewState.value = updatedInterviewState
                    }
                    is Resource.Loading -> {
                        val updatedInterviewState = _interviewState.value.copy(isLoading = true)
                        _interviewState.value = updatedInterviewState
                    }
                }
            }
        }
    }

    fun endInterview() {
        _interviewState.value = _interviewState.value.copy(isEnding = true)
        viewModelScope.launch {
            interviewRepository.endInterview(EndInterviewRequest(sessionState.value)).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val interview = result.data
                        if (interview != null) {
                            val updatedInterviewState = _interviewState.value.copy(
                                isLoading = false,
                                interview = _interviewState.value.interview + ReplyInterviewResponse(
                                    data = interview.data,
                                    type = interview.type,
                                    isDone = interview.isDone
                                ),
                                errorMessage = null,
                                isInterviewDone = true
                            )
                            _interviewState.value = updatedInterviewState
                        }
                    }
                    is Resource.Error -> {
                        val errorMessage = result.message
                        val updatedInterviewState = _interviewState.value.copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = errorMessage,
                            isEnding = true
                        )
                        _interviewState.value = updatedInterviewState
                    }
                    is Resource.Loading -> {
                        val updatedInterviewState = _interviewState.value.copy(isLoading = true)
                        _interviewState.value = updatedInterviewState
                    }
                }
            }
        }
    }

    fun saveInterview() {
        _interviewState.value = _interviewState.value.copy(isSaving = true)
        viewModelScope.launch {
            interviewRepository.saveInterview(SaveInterviewRequest(sessionState.value)).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val interview = result.data
                        if (interview != null) {
                            val updatedInterviewState = _interviewState.value.copy(
                                isLoading = false,
                                errorMessage = null,
                            )
                            _interviewState.value = updatedInterviewState
                        }
                    }
                    is Resource.Error -> {
                        val errorMessage = result.message
                        val updatedInterviewState = _interviewState.value.copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = errorMessage,
                            isSaving = false
                        )
                        _interviewState.value = updatedInterviewState
                    }
                    is Resource.Loading -> {
                        val updatedInterviewState = _interviewState.value.copy(isLoading = true)
                        _interviewState.value = updatedInterviewState
                    }
                }
            }
        }
    }

//    fun startInterview(message: String) {
//        viewModelScope.launch {
//            interviewRepository.startInterview().collect { result ->
//                when (result) {
//                    is Resource.Success -> {
//                        val interview = result.data
//                        if (interview != null) {
//                            val updatedInterviewState = _interviewState.value.copy(
//                                isLoading = false,
//                                interview = _interviewState.value.interview + interview,
//                                errorMessage = null
//                            )
//                            _interviewState.value = updatedInterviewState
//                        }
//                    }
//                    is Resource.Error -> {
//                        val errorMessage = result.message
//                        val updatedInterviewState = _interviewState.value.copy(
//                            isLoading = false,
//                            isError = true,
//                            errorMessage = errorMessage
//                        )
//                        _interviewState.value = updatedInterviewState
//                    }
//                    is Resource.Loading -> {
//                        val updatedInterviewState = _interviewState.value.copy(isLoading = true)
//                        _interviewState.value = updatedInterviewState
//                    }
//                }
//            }
//        }
//    }
//
//    fun sendMessage(message: String) {
//        _interviewState.value = _interviewState.value.copy(
//            interview = _interviewState.value.interview + InterviewChat(message, Author.User)
//        )
//        viewModelScope.launch {
//            interviewRepository.sendMessage().collect { result ->
//                when (result) {
//                    is Resource.Success -> {
//                        val interview = result.data
//                        if (interview != null) {
//                            val updatedInterviewState = _interviewState.value.copy(
//                                isLoading = false,
//                                interview = _interviewState.value.interview + interview,
//                                errorMessage = null
//                            )
//                            _interviewState.value = updatedInterviewState
//                        }
//                    }
//                    is Resource.Error -> {
//                        val errorMessage = result.message
//                        val updatedInterviewState = _interviewState.value.copy(
//                            isLoading = false,
//                            isError = true,
//                            errorMessage = errorMessage
//                        )
//                        _interviewState.value = updatedInterviewState
//                    }
//                    is Resource.Loading -> {
//                        val updatedInterviewState = _interviewState.value.copy(isLoading = true)
//                        _interviewState.value = updatedInterviewState
//                    }
//                }
//            }
//        }
//    }
}