package com.magma.virtuhire.data.repository

import com.magma.virtuhire.data.local.UserPreferences
import com.magma.virtuhire.data.remote.ApiService
import com.magma.virtuhire.data.remote.request.LoginRequest
import com.magma.virtuhire.data.remote.request.LogoutRequest
import com.magma.virtuhire.data.remote.request.RegisterRequest
import com.magma.virtuhire.data.remote.response.LoginResponse
import com.magma.virtuhire.data.remote.response.User
import com.magma.virtuhire.data.remote.response.UserDataResponse
import com.magma.virtuhire.domain.repository.AuthRepository
import com.magma.virtuhire.utils.Resource
import com.magma.virtuhire.utils.handleError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
): AuthRepository {
    override suspend fun loginUser(
        userEmail: String,
        password: String
    ): Flow<Resource<UserDataResponse>> {
        return flow {
            try {
                emit(Resource.Loading(true))
                val loginData = LoginRequest(userEmail, password)
                val response = apiService.loginUser(loginData)
                userPreferences.saveAccessToken(response.access.token)
                userPreferences.saveRefreshToken(response.refresh.token)
                emit(Resource.Success(response))
            } catch (e: Throwable) {
                emit(Resource.Error(handleError(e)))
            }
        }
    }

    override suspend fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        passwordConfirmation: String,
    ): Flow<Resource<UserDataResponse>> {
        return flow {
            try {
                emit(Resource.Loading(true))
                val registerData = RegisterRequest(
                    firstName,
                    lastName,
                    email,
                    password,
                    passwordConfirmation,
                    true
                )
                val response = apiService.registerUser(registerData)
                emit(Resource.Success(response))
            } catch (e: Exception) {
                emit(Resource.Error(handleError(e)))
            }
        }
    }


    override suspend fun getLoggedInUser(): Flow<Resource<User>> {
        val token = userPreferences.getAccessToken().firstOrNull()
        return flow {
            if (token != null) {
                try {
                    val response = apiService.getLoggedInUser("Bearer $token")
                    emit(Resource.Success(response))
                } catch (e: Exception) {
                    emit(Resource.Error(handleError(e)))
                }
            } else {
                emit(Resource.Error("Unauthorized: No Token"))
            }
        }
    }

    override suspend fun logOutUser(): Flow<Resource<Unit>> {
        val token = userPreferences.getRefreshToken().firstOrNull()
        return flow {
            if (token != null) {
                try {
                    val response = apiService.logOutUser(LogoutRequest(token))
                    userPreferences.clearTokens()
                    emit(Resource.Success(response))
                } catch (e: Exception) {
                    emit(Resource.Error(handleError(e)))
                }
            } else {
                emit(Resource.Error("Unauthorized: No Token"))
            }
        }
    }
}