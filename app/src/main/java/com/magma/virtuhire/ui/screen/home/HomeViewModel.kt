package com.magma.virtuhire.ui.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magma.virtuhire.domain.repository.AuthRepository
import com.magma.virtuhire.domain.repository.JobRepository
import com.magma.virtuhire.ui.screen.splash.SplashState
import com.magma.virtuhire.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    private var _jobState = mutableStateOf(JobOpeningListState())
    val jobState: State<JobOpeningListState> = _jobState

    private var _fallbackJobState = mutableStateOf(JobOpeningListState())
    val fallbackJobState: State<JobOpeningListState> = _fallbackJobState

    private var _recommendedJobState = mutableStateOf(JobOpeningListState())
    val recommendedJobState: State<JobOpeningListState> = _recommendedJobState

    private var _profileState = mutableStateOf(ProfileState())
    val profileState: State<ProfileState> = _profileState

    init {
        getAllJobs()
        getRecommendedJobs()
        getFallbackJobs()
        getLoggedInUser()
    }

    private fun getFallbackJobs() {
        viewModelScope.launch {
            jobRepository.getAllJobs(
                categoryFilter = null,
                cityFilter = null,
                companyFilter = null,
                salaryFromFilter = null,
                salaryToFilter = null,
                skillRequirementsFilter = null,
                limit = null,
                page = null,
                sortBy = "salaryFrom:DESC"
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _fallbackJobState.value = JobOpeningListState(
                            jobs = result.data ?: emptyList()
                        )
                    }
                    is Resource.Error -> {
                        _fallbackJobState.value = JobOpeningListState(
                            isError = true,
                            errorMessage = result.message
                        )
                    }
                    is Resource.Loading -> {
                        _fallbackJobState.value = JobOpeningListState(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    private fun getAllJobs(
        categoryFilter: String? = null,
        cityFilter: String? = null,
        companyFilter: String? = null,
        salaryFromFilter: String? = null,
        salaryToFilter: String? = null,
        skillRequirementsFilter: String? = null,
        limit: Int? = null,
        page: Int? = null,
        sortBy: String? = null
    ) {
        viewModelScope.launch {
            jobRepository.getAllJobs(
                categoryFilter,
                cityFilter,
                companyFilter,
                salaryFromFilter,
                salaryToFilter,
                skillRequirementsFilter,
                limit,
                page,
                sortBy
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _jobState.value = JobOpeningListState(
                            jobs = result.data ?: emptyList()
                        )
                    }
                    is Resource.Error -> {
                        _jobState.value = JobOpeningListState(
                            isError = true,
                            errorMessage = result.message
                        )
                    }
                    is Resource.Loading -> {
                        _jobState.value = JobOpeningListState(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    private fun getRecommendedJobs(
        categoryFilter: String? = null,
        cityFilter: String? = null,
        companyFilter: String? = null,
        salaryFromFilter: String? = null,
        salaryToFilter: String? = null,
        skillRequirementsFilter: String? = null,
        limit: Int? = null,
        page: Int? = null,
        sortBy: String? = null
    ) {
        viewModelScope.launch {
            jobRepository.getRecommendedJobs(
                categoryFilter,
                cityFilter,
                companyFilter,
                salaryFromFilter,
                salaryToFilter,
                skillRequirementsFilter,
                limit,
                page,
                sortBy
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _recommendedJobState.value = JobOpeningListState(
                            jobs = result.data ?: emptyList()
                        )
                    }
                    is Resource.Error -> {
                        _recommendedJobState.value = JobOpeningListState(
                            isError = true,
                            errorMessage = result.message
                        )
                    }
                    is Resource.Loading -> {
                        _recommendedJobState.value = JobOpeningListState(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    private fun getLoggedInUser() {
        viewModelScope.launch {
            authRepository.getLoggedInUser().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _profileState.value = ProfileState(
                            profile = result.data,
                            isLoading = false,
                        )
                    }

                    is Resource.Error -> {
                        _profileState.value = ProfileState(
                            isLoading = false,
                            isError = true,
                            errorMessage = result.message
                        )
                    }

                    is Resource.Loading -> {
                        _profileState.value = ProfileState(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

}