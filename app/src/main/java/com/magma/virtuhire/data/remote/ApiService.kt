package com.magma.virtuhire.data.remote

import com.magma.virtuhire.data.remote.request.EndInterviewRequest
import com.magma.virtuhire.data.remote.request.IntroductionRequest
import com.magma.virtuhire.data.remote.request.LoginRequest
import com.magma.virtuhire.data.remote.request.LogoutRequest
import com.magma.virtuhire.data.remote.request.RefreshRequest
import com.magma.virtuhire.data.remote.request.RegisterRequest
import com.magma.virtuhire.data.remote.request.ReplyInterviewRequest
import com.magma.virtuhire.data.remote.request.SaveInterviewRequest
import com.magma.virtuhire.data.remote.request.StartInterviewRequest
import com.magma.virtuhire.data.remote.response.ApiResponse
import com.magma.virtuhire.data.remote.response.CityResponse
import com.magma.virtuhire.data.remote.response.DegreeResponse
import com.magma.virtuhire.data.remote.response.EndInterviewResponse
import com.magma.virtuhire.data.remote.response.InstitutionResponse
import com.magma.virtuhire.data.remote.response.InterviewHistoryResponse
import com.magma.virtuhire.data.remote.response.JobCategoryResponse
import com.magma.virtuhire.data.remote.response.JobDetailResponse
import com.magma.virtuhire.data.remote.response.JobOpeningResponse
import com.magma.virtuhire.data.remote.response.JobResponse
import com.magma.virtuhire.data.remote.response.LoginResponse
import com.magma.virtuhire.data.remote.response.MajorResponse
import com.magma.virtuhire.data.remote.response.RefreshResponse
import com.magma.virtuhire.data.remote.response.RegisterResponse
import com.magma.virtuhire.data.remote.response.ReplyInterviewResponse
import com.magma.virtuhire.data.remote.response.ResumeResponse
import com.magma.virtuhire.data.remote.response.SkillResponse
import com.magma.virtuhire.data.remote.response.StartInterviewResponse
import com.magma.virtuhire.data.remote.response.User
import com.magma.virtuhire.data.remote.response.UserDataResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/register")
    @Headers("Content-Type: application/json")
    suspend fun registerUser(
       @Body registerData: RegisterRequest
    ): UserDataResponse

    @POST("auth/login")
    @Headers("Content-Type: application/json")
    suspend fun loginUser(
        @Body loginData: LoginRequest
    ): UserDataResponse

    @GET("account/me")
    suspend fun getLoggedInUser(
        @Header("Authorization") authHeader: String
    ): User

    @POST("auth/logout")
    suspend fun logOutUser(
        @Body logOutData: LogoutRequest
    ): Unit

    @POST("auth/refresh")
    suspend fun refreshAccessToken(
        @Body refreshData: RefreshRequest
    ): RefreshResponse

    @GET("jobs/search")
    suspend fun searchJobs(
        @Query("q") q: String = ""
    ): ApiResponse<List<JobOpeningResponse>>

    @GET("jobs/feed")
    suspend fun getAllJobs(
        @Query("filter.category") categoryFilter: String? = null,
        @Query("filter.city") cityFilter: String? = null,
        @Query("filter.company") companyFilter: String? = null,
        @Query("filter.salaryFrom") salaryFromFilter: String? = null,
        @Query("filter.salaryTo") salaryToFilter: String? = null,
        @Query("filter.skillRequirements") skillRequirementsFilter: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null,
        @Query("sortBy") sortBy: String? = null
    ): ApiResponse<List<JobOpeningResponse>>

    @GET("jobs/recommendations")
    suspend fun getRecommendedJobs(
        @Header("Authorization") accessToken: String,
        @Query("filter.category") categoryFilter: String? = null,
        @Query("filter.city") cityFilter: String? = null,
        @Query("filter.company") companyFilter: String? = null,
        @Query("filter.salaryFrom") salaryFromFilter: String? = null,
        @Query("filter.salaryTo") salaryToFilter: String? = null,
        @Query("filter.skillRequirements") skillRequirementsFilter: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null,
        @Query("sortBy") sortBy: String? = null
    ): ApiResponse<List<JobOpeningResponse>>

    @GET("jobs/{id}")
    suspend fun getJobById(
        @Path("id") id: String,
    ): JobDetailResponse

    // Introduction
    @GET("institution")
    suspend fun getAllInstitutions(
        @Query("q") query: String?,
        @Query("limit") limit: Int?
    ): ApiResponse<List<InstitutionResponse>>

    @GET("major")
    suspend fun getAllMajors(
        @Query("q") query: String?,
        @Query("limit") limit: Int?
    ): ApiResponse<List<MajorResponse>>

    @GET("degree")
    suspend fun getAllDegrees(
        @Query("q") query: String?,
        @Query("limit") limit: Int?
    ): ApiResponse<List<DegreeResponse>>

    @GET("job-category")
    suspend fun getAllJobCategories(
        @Query("q") query: String?,
        @Query("limit") limit: Int?
    ): ApiResponse<List<JobCategoryResponse>>

    @GET("skill")
    suspend fun getAllSkills(
        @Query("q") query: String?,
        @Query("limit") limit: Int?
    ): ApiResponse<List<SkillResponse>>

    @GET("city")
    suspend fun getAllCities(
        @Query("q") query: String?,
        @Query("limit") limit: Int?
    ): ApiResponse<List<CityResponse>>

    @POST("/v1/account/profile")
    @Headers("Content-Type: application/json")
    suspend fun createUserProfile(
        @Header("Authorization") accessToken: String,
        @Body request: IntroductionRequest
    ) // Change ApiResponse to your desired response type

    // Interviww
    @GET("interview/histories")
    suspend fun getInterviewHistories(
        @Header("Authorization") accessToken: String
    ): ApiResponse<List<InterviewHistoryResponse>>

    @GET("interview/histories/{id}")
    suspend fun getInterviewHistory(
        @Header("Authorization") accessToken: String,
        @Path("id") id: String
    ): InterviewHistoryResponse

    @POST("interview/save")
    @Headers("Content-Type: application/json")
    suspend fun saveInterview(
        @Header("Authorization") accessToken: String,
        @Body saveInterviewRequest: SaveInterviewRequest
    ):InterviewHistoryResponse

    @POST("interview/start")
    @Headers("Content-Type: application/json")
    suspend fun startInterview(
        @Header("Authorization") accessToken: String,
        @Body startInterviewRequest: StartInterviewRequest
    ):StartInterviewResponse

    @POST("interview/reply")
    @Headers("Content-Type: application/json")
    suspend fun replyInterview(
        @Header("Authorization") accessToken: String,
        @Body replyInterviewRequest: ReplyInterviewRequest
    ):ReplyInterviewResponse

    @POST("interview/end")
    @Headers("Content-Type: application/json")
    suspend fun endInterview(
        @Header("Authorization") accessToken: String,
        @Body endInterviewRequest: EndInterviewRequest
    ):EndInterviewResponse

    @Multipart
    @POST("/resume-parser/parse")
    suspend fun parseResume(
        @Header("Authorization") accessToken: String,
        @Part file: MultipartBody.Part
    ): ResumeResponse
}

