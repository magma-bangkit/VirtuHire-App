package com.magma.virtuhire.ui.screen.history_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magma.virtuhire.data.remote.request.EndInterviewRequest
import com.magma.virtuhire.data.remote.request.ReplyInterviewRequest
import com.magma.virtuhire.data.remote.request.SaveInterviewRequest
import com.magma.virtuhire.data.remote.response.ChatData
import com.magma.virtuhire.data.remote.response.ReplyInterviewResponse
import com.magma.virtuhire.domain.repository.InterviewRepository
import com.magma.virtuhire.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryDetailViewModel @Inject constructor(
    private val interviewRepository: InterviewRepository
) : ViewModel() {
    private val _sessionState = mutableStateOf("")
    private val sessionState: State<String> = _sessionState

    private var _interviewState = mutableStateOf(InterviewHistoryDetailState())
    val interviewState: State<InterviewHistoryDetailState> = _interviewState

    private var _saveInterviewState = mutableStateOf(InterviewHistoryDetailState())
    val saveInterviewState: State<InterviewHistoryDetailState> = _interviewState

    fun getInterviewHistory(sessionId: String) {
        viewModelScope.launch {
            interviewRepository.getInterviewHistory(sessionId).collect { result ->
                _interviewState.value = when (result) {
                    is Resource.Success -> {
                        if (result.data != null) {
                            _sessionState.value = result.data.sessionId
                        }
                        InterviewHistoryDetailState(
                            interviews = result.data,
                            isLoading = false,
                            errorMessage = null,
                            isError = false
                        )
                    }
                    is Resource.Error ->
                        InterviewHistoryDetailState(
                            isLoading = false,
                            errorMessage = result.message,
                            isError = true,
                        )

                    is Resource.Loading -> InterviewHistoryDetailState(isLoading = true)
                }
            }
        }
    }

    fun replyInterview(message: String) {
        val updatedInterview = _interviewState.value.chatHistory + ReplyInterviewResponse(ChatData(message), "human", false)
        _interviewState.value = _interviewState.value.copy(chatHistory = updatedInterview)
        viewModelScope.launch {
            interviewRepository.replyInterview(ReplyInterviewRequest(sessionState.value, message)).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val interview = result.data
                        if (interview != null) {
                            val updatedInterviewState = _interviewState.value.copy(
                                isLoading = false,
                                chatHistory = _interviewState.value.chatHistory + ReplyInterviewResponse(
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
        viewModelScope.launch {
            interviewRepository.endInterview(EndInterviewRequest(sessionState.value)).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val interview = result.data
                        if (interview != null) {
                            val updatedInterviewState = _interviewState.value.copy(
                                isLoading = false,
                                chatHistory = _interviewState.value.chatHistory + ReplyInterviewResponse(
                                    data = interview.data,
                                    type = interview.type,
                                    isDone = interview.isDone
                                ),
                                errorMessage = null,
                                isInterviewDone = true,
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

    fun saveInterview() {
        viewModelScope.launch {
            interviewRepository.saveInterview(SaveInterviewRequest(sessionState.value)).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val interview = result.data
                        if (interview != null) {
                            val updatedInterviewState = _saveInterviewState.value.copy(
                                isLoading = false,
                                errorMessage = null,
                            )
                            _saveInterviewState.value = updatedInterviewState
                        }
                    }
                    is Resource.Error -> {
                        val errorMessage = result.message
                        val updatedInterviewState = _saveInterviewState.value.copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = errorMessage
                        )
                        _saveInterviewState.value = updatedInterviewState
                    }
                    is Resource.Loading -> {
                        val updatedInterviewState = _saveInterviewState.value.copy(isLoading = true)
                        _saveInterviewState.value = updatedInterviewState
                    }
                }
            }
        }
    }

}