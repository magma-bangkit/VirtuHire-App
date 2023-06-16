package com.magma.virtuhire.ui.screen.find

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.magma.virtuhire.data.remote.ApiService
import com.magma.virtuhire.data.remote.paging.JobPagingSource
import com.magma.virtuhire.data.remote.response.JobOpeningResponse
import com.magma.virtuhire.domain.repository.JobRepository
import com.magma.virtuhire.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    private val apiService: ApiService,
) : ViewModel() {

    private var _jobState = mutableStateOf(JobOpeningListState())
    val jobState: State<JobOpeningListState> = _jobState

    fun getAllJobsWithPaging(
        categoryFilter: String? = null,
        cityFilter: String? = null,
        companyFilter: String? = null,
        salaryFromFilter: String? = null,
        salaryToFilter: String? = null,
        skillRequirementsFilter: String? = null,
        limit: Int? = null,
        page: Int? = null,
        sortBy: String? = null
    ): Flow<PagingData<JobOpeningResponse>> = jobRepository.getAllJobsWithPaging(
        categoryFilter,
        cityFilter,
        companyFilter,
        salaryFromFilter,
        salaryToFilter,
        skillRequirementsFilter,
        limit,
        page,
        sortBy
    ).cachedIn(viewModelScope)

    fun searchJobs(q: String) {
        viewModelScope.launch {
            jobRepository.searchJobs(q).collect { result ->
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

    val jobsPaging =
        Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            pagingSourceFactory = {
                JobPagingSource(apiService)
            }
        ).flow.cachedIn(viewModelScope)


}