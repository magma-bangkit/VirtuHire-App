package com.magma.virtuhire.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)

data class LoginResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("loginResult")
    val loginResult: LoginResult
)

data class LoginResult(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("token")
    val token: String
)

data class User(
    @SerializedName("id") val id: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("deletedAt") val deletedAt: String?,
    @SerializedName("_v") val version: Int,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: String,
    @SerializedName("isEmailVerified") val isEmailVerified: Boolean,
    @SerializedName("signUpMethod") val signUpMethod: String,
    @SerializedName("profile") val profile: Any? // Replace `Any?` with the actual type of the `profile` property
)

data class AccessToken(
    @SerializedName("token") val token: String,
    @SerializedName("expires") val expires: String
)

data class RefreshToken(
    @SerializedName("token") val token: String,
    @SerializedName("expires") val expires: String
)

data class UserDataResponse(
    @SerializedName("user") val user: User,
    @SerializedName("access") val access: AccessToken,
    @SerializedName("refresh") val refresh: RefreshToken
)

data class RefreshResponse(
    @SerializedName("access") val access: AccessToken
)
