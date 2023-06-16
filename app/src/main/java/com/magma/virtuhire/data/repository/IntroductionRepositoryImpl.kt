package com.magma.virtuhire.data.repository

import android.net.Uri
import com.magma.virtuhire.data.local.UserPreferences
import com.magma.virtuhire.data.remote.ApiService
import com.magma.virtuhire.data.remote.request.IntroductionRequest
import com.magma.virtuhire.data.remote.response.CityResponse
import com.magma.virtuhire.data.remote.response.DegreeResponse
import com.magma.virtuhire.data.remote.response.InstitutionResponse
import com.magma.virtuhire.data.remote.response.JobCategoryResponse
import com.magma.virtuhire.data.remote.response.MajorResponse
import com.magma.virtuhire.data.remote.response.ResumeResponse
import com.magma.virtuhire.data.remote.response.SkillResponse
import com.magma.virtuhire.domain.repository.IntroductionRepository
import com.magma.virtuhire.utils.Resource
import com.magma.virtuhire.utils.handleError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class IntroductionRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) : IntroductionRepository {
    override suspend fun getAllInstitutions(
        query: String,
        limit: Int
    ): Flow<Resource<List<InstitutionResponse>>> {
        return flow {
            try {
                emit(Resource.Loading(true))
                val response = apiService.getAllInstitutions(query, limit)
                emit(Resource.Success(response.data))
            } catch (e: Exception) {
                emit(Resource.Error(handleError(e)))
            }
        }
    }

    override suspend fun getAllMajors(
        query: String,
        limit: Int
    ): Flow<Resource<List<MajorResponse>>> {
        return flow {
            try {
                emit(Resource.Loading(true))
                val response = apiService.getAllMajors(query, limit)
                emit(Resource.Success(response.data))
            } catch (e: Exception) {
                emit(Resource.Error(handleError(e)))
            }
        }
    }

    override suspend fun getAllDegrees(
        query: String,
        limit: Int
    ): Flow<Resource<List<DegreeResponse>>> {
        return flow {
            try {
                emit(Resource.Loading(true))
                val response = apiService.getAllDegrees(query, limit)
                emit(Resource.Success(response.data))
            } catch (e: Exception) {
                emit(Resource.Error(handleError(e)))
            }
        }
    }

    override suspend fun getAllJobCategories(
        query: String,
        limit: Int
    ): Flow<Resource<List<JobCategoryResponse>>> {
        return flow {
            try {
                emit(Resource.Loading(true))
                val response = apiService.getAllJobCategories(query, limit)
                emit(Resource.Success(response.data))
            } catch (e: Exception) {
                emit(Resource.Error(handleError(e)))
            }
        }
    }

    override suspend fun getAllSkills(
        query: String,
        limit: Int
    ): Flow<Resource<List<SkillResponse>>> {
        return flow {
            try {
                emit(Resource.Loading(true))
                val response = apiService.getAllSkills(query, limit)
                emit(Resource.Success(response.data))
            } catch (e: Exception) {
                emit(Resource.Error(handleError(e)))
            }
        }
    }

    override suspend fun getAllCities(
        query: String,
        limit: Int
    ): Flow<Resource<List<CityResponse>>> {
        return flow {
            try {
                emit(Resource.Loading(true))
                val response = apiService.getAllCities(query, limit)
                emit(Resource.Success(response.data))
            } catch (e: Exception) {
                emit(Resource.Error(handleError(e)))
            }
        }
    }

    override suspend fun createUserProfile(introductionRequest: IntroductionRequest): Flow<Resource<Unit>> {
        val token = userPreferences.getAccessToken().firstOrNull()
        return flow {
            try {
                emit(Resource.Loading(true))
                if (token != null) {
                    val response = apiService.createUserProfile("Bearer $token",introductionRequest)
                    emit(Resource.Success(Unit))
                } else {
                    emit(Resource.Error(message = "Access token is null"))
                }
            } catch (e: Exception) {
                emit(Resource.Error(handleError(e)))
            }
        }
    }

    override suspend fun parseResume(fileUri: Uri): Flow<Resource<ResumeResponse>> {
        val token = userPreferences.getAccessToken().firstOrNull()
        return flow {
            try {
                emit(Resource.Loading(true))
                if (token != null) {
                    val file = File(fileUri.path ?: "")
                    val requestFile = file.asRequestBody("application/pdf".toMediaType())
                    val multipartBody = MultipartBody.Part.createFormData("file", file.name, requestFile)
                    val response = apiService.parseResume("Bearer $token", multipartBody)
                    emit(Resource.Success(response))
                } else {
                    emit(Resource.Error(message = "Access token is null"))
                }
            } catch (e: Exception) {
                emit(Resource.Error(handleError(e)))
            }
        }
    }

}