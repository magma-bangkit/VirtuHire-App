package com.magma.virtuhire.data.remote.response

import com.google.gson.annotations.SerializedName

data class JobResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("jobOpenings") val jobOpenings: List<JobOpeningResponse>
)

data class JobOpeningResponse(
    @SerializedName("id") val id: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("source") val source: String,
    @SerializedName("jobType") val jobType: String,
    @SerializedName("salaryFrom") val salaryFrom: Int,
    @SerializedName("salaryTo") val salaryTo: Int,
    @SerializedName("category") val category: JobCategoryResponse,
    @SerializedName("company") val company: Company,
    @SerializedName("city") val city: CityResponse,
    @SerializedName("skillRequirements") val skillRequirements: List<SkillResponse>
)

data class JobDetailResponse(
    @SerializedName("id") val id: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("deletedAt") val deletedAt: String?,
    @SerializedName("_v") val version: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("source") val source: String,
    @SerializedName("jobType") val jobType: String,
    @SerializedName("salaryFrom") val salaryFrom: Int,
    @SerializedName("salaryTo") val salaryTo: Int,
    @SerializedName("requirements") val requirements: List<String>,
    @SerializedName("responsibilities") val responsibilities: List<String>,
    @SerializedName("company") val company: Company,
    @SerializedName("city") val city: CityResponse,
    @SerializedName("category") val category: JobCategoryResponse,
    @SerializedName("skillRequirements") val skillRequirements: List<SkillResponse>
)

data class Company(
    @SerializedName("id") val id: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("deletedAt") val deletedAt: String?,
    @SerializedName("_v") val version: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("logo") val logo: String
)

