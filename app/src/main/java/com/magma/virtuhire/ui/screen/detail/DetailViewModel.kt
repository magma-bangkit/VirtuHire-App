package com.magma.virtuhire.ui.screen.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magma.virtuhire.domain.repository.JobRepository
import com.magma.virtuhire.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val jobRepository: JobRepository
): ViewModel() {

    private var _jobState = mutableStateOf(JobOpeningDetailState())
    val jobState: State<JobOpeningDetailState> = _jobState

    fun getJobById(jobId: String) {
        viewModelScope.launch {
            jobRepository.getJobById(jobId).collect { result ->
                when(result) {
                    is Resource.Success -> {
                        _jobState.value = JobOpeningDetailState(
                            job = result.data
                        )
                    }
                    is Resource.Error -> {
                        _jobState.value = JobOpeningDetailState(
                            isError = true,
                            errorMessage = result.message
                        )
                    }
                    is Resource.Loading -> {
                        _jobState.value = JobOpeningDetailState(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }
}