package com.magma.virtuhire.data.remote.request

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val passwordConfirmation: String,
    val termsAndConditions: Boolean
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LogoutRequest(
    val refreshToken: String
)

data class RefreshRequest(
    val refreshToken: String
)