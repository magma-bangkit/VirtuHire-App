package com.magma.virtuhire.domain.repository

import android.net.Uri
import com.magma.virtuhire.data.remote.request.IntroductionRequest
import com.magma.virtuhire.data.remote.response.CityResponse
import com.magma.virtuhire.data.remote.response.DegreeResponse
import com.magma.virtuhire.data.remote.response.InstitutionResponse
import com.magma.virtuhire.data.remote.response.JobCategoryResponse
import com.magma.virtuhire.data.remote.response.MajorResponse
import com.magma.virtuhire.data.remote.response.ResumeResponse
import com.magma.virtuhire.data.remote.response.SkillResponse
import com.magma.virtuhire.utils.Resource
import kotlinx.coroutines.flow.Flow

interface IntroductionRepository {
    suspend fun getAllInstitutions(query: String, limit: Int): Flow<Resource<List<InstitutionResponse>>>
    suspend fun getAllMajors(query: String, limit: Int): Flow<Resource<List<MajorResponse>>>
    suspend fun getAllDegrees(query: String, limit: Int): Flow<Resource<List<DegreeResponse>>>
    suspend fun getAllJobCategories(query: String, limit: Int): Flow<Resource<List<JobCategoryResponse>>>
    suspend fun getAllSkills(query: String, limit: Int): Flow<Resource<List<SkillResponse>>>
    suspend fun getAllCities(query: String, limit: Int): Flow<Resource<List<CityResponse>>>
    suspend fun createUserProfile(introductionRequest: IntroductionRequest): Flow<Resource<Unit>>
    suspend fun parseResume(fileUri: Uri): Flow<Resource<ResumeResponse>>
}