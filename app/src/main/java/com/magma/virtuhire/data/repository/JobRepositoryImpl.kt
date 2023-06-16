package com.magma.virtuhire.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.magma.virtuhire.data.local.UserPreferences
import com.magma.virtuhire.data.mock.MockJobDataSource
import com.magma.virtuhire.data.remote.ApiService
import com.magma.virtuhire.data.remote.paging.JobPagingSource
import com.magma.virtuhire.data.remote.response.JobDetailResponse
import com.magma.virtuhire.data.remote.response.JobOpeningResponse
import com.magma.virtuhire.domain.repository.JobRepository
import com.magma.virtuhire.utils.Resource
import com.magma.virtuhire.utils.handleError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences,
    private val mockJobDataSource: MockJobDataSource
) : JobRepository {

    override suspend fun searchJobs(q: String): Flow<Resource<List<JobOpeningResponse>>> {
        return flow {
            try {
                emit(Resource.Loading(true))
                val response = apiService.searchJobs(
                    q = q
                )
                emit(Resource.Success(data = response.data))
            } catch (e: Exception) {
                emit(Resource.Error(message = handleError(e)))
            }
        }
    }

    override suspend fun getAllJobs(
        categoryFilter: String?,
        cityFilter: String?,
        companyFilter: String?,
        salaryFromFilter: String?,
        salaryToFilter: String?,
        skillRequirementsFilter: String?,
        limit: Int?,
        page: Int?,
        sortBy: String?
    ): Flow<Resource<List<JobOpeningResponse>>> {
        return flow {
            try {
                emit(Resource.Loading(true))
                val response = apiService.getAllJobs(
                    categoryFilter = categoryFilter,
                    cityFilter = cityFilter,
                    companyFilter = companyFilter,
                    salaryFromFilter = salaryFromFilter,
                    salaryToFilter = salaryToFilter,
                    skillRequirementsFilter = skillRequirementsFilter,
                    limit = limit,
                    page = page,
                    sortBy = sortBy
                )
                emit(Resource.Success(data = response.data))
                // val response = mockJobDataSource.getAllJobs()
                // emit(Resource.Success(data = response))
            } catch (e: IOException) {
                emit(Resource.Error("IO Exception: ${e.message}"))
            } catch (e: TimeoutException) {
                emit(Resource.Error("Timeout Exception: ${e.message}"))
            } catch (e: HttpException) {
                emit(Resource.Error("Http Exception: ${e.message}"))
            }
        }
    }


    override suspend fun getRecommendedJobs(
        categoryFilter: String?,
        cityFilter: String?,
        companyFilter: String?,
        salaryFromFilter: String?,
        salaryToFilter: String?,
        skillRequirementsFilter: String?,
        limit: Int?,
        page: Int?,
        sortBy: String?
    ): Flow<Resource<List<JobOpeningResponse>>> {
        val token = userPreferences.getAccessToken().firstOrNull()
        return flow {
            try {
                emit(Resource.Loading(true))
                val response = apiService.getRecommendedJobs(
                    accessToken = "Bearer $token",
                    categoryFilter = categoryFilter,
                    cityFilter = cityFilter,
                    companyFilter = companyFilter,
                    salaryFromFilter = salaryFromFilter,
                    salaryToFilter = salaryToFilter,
                    skillRequirementsFilter = skillRequirementsFilter,
                    limit = limit,
                    page = page,
                    sortBy = sortBy
                )
                emit(Resource.Success(data = response.data))
                // val response = mockJobDataSource.getRecommendedJobs()
                // emit(Resource.Success(data = response))
            } catch (e: Exception) {
                emit(Resource.Error(handleError(e)))
            }
        }
    }


    override suspend fun getJobById(jobId: String): Flow<Resource<JobDetailResponse>> {
        return flow {
            try {
                emit(Resource.Loading(true))
                val response = apiService.getJobById(jobId)
                emit(Resource.Success(data = response))
            } catch (e: IOException) {
                emit(Resource.Error("IO Exception: ${e.message}"))
            } catch (e: TimeoutException) {
                emit(Resource.Error("Timeout Exception: ${e.message}"))
            } catch (e: HttpException) {
                emit(Resource.Error("Http Exception: ${e.message}"))
            }
        }
    }

    override fun getAllJobsWithPaging(
        categoryFilter: String?,
        cityFilter: String?,
        companyFilter: String?,
        salaryFromFilter: String?,
        salaryToFilter: String?,
        skillRequirementsFilter: String?,
        limit: Int?,
        page: Int?,
        sortBy: String?
    ) = Pager(
        config = PagingConfig(
            pageSize = 20,
        ),
        pagingSourceFactory = {
            JobPagingSource(apiService)
        }
    ).flow
}