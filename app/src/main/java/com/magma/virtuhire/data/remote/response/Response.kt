package com.magma.virtuhire.data.remote.response

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("data") val data: T,
    @SerializedName("links") val links: Links,
    @SerializedName("meta") val meta: Meta
)

data class Meta(
    @SerializedName("itemsPerPage") val itemsPerPage: Int,
    @SerializedName("totalItems") val totalItems: Int,
    @SerializedName("currentPage") val currentPage: Int,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("sortBy") val sortBy: List<List<String>>
)

data class Links(
    @SerializedName("current") val current: String,
    @SerializedName("next") val next: String,
    @SerializedName("last") val last: String
)

data class ErrorResponse(
    val statusCode: Int,
    val code: String,
    val message: Any,
    val timestamp: String,
)