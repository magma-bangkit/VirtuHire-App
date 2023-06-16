package com.magma.virtuhire.domain.repository

import com.magma.virtuhire.data.remote.response.LoginResponse
import com.magma.virtuhire.data.remote.response.User
import com.magma.virtuhire.data.remote.response.UserDataResponse
import com.magma.virtuhire.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginUser(userEmail: String, password: String): Flow<Resource<UserDataResponse>>
    suspend fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        passwordConfirmation: String,
    ): Flow<Resource<UserDataResponse>>
    suspend fun getLoggedInUser(): Flow<Resource<User>>
    suspend fun logOutUser(): Flow<Resource<Unit>>
}