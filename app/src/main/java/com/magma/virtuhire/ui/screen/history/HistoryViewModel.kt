package com.magma.virtuhire.ui.screen.history

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magma.virtuhire.domain.repository.InterviewRepository
import com.magma.virtuhire.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val interviewRepository: InterviewRepository
) : ViewModel() {

    private var _interviewState = mutableStateOf(InterviewHistoryListState())
    val interviewState: State<InterviewHistoryListState> = _interviewState

    init {
        getInterviewHistories()
    }

    private fun getInterviewHistories() {
        viewModelScope.launch {
            interviewRepository.getInterviewHistories().collect { result ->
                _interviewState.value = when (result) {
                    is Resource.Success -> InterviewHistoryListState(
                        interviews = result.data ?: emptyList(),
                        isLoading = false,
                        errorMessage = null,
                        isError = false
                    )

                    is Resource.Error ->
                        InterviewHistoryListState(
                            isLoading = false,
                            errorMessage = result.message,
                            isError = true,
                        )

                    is Resource.Loading -> InterviewHistoryListState(isLoading = true)
                }
            }
        }
    }

}