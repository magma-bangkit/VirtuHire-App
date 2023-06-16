package com.magma.virtuhire.data.remote

import com.magma.virtuhire.data.local.UserPreferences
import com.magma.virtuhire.data.remote.ApiService
import com.magma.virtuhire.data.remote.request.RefreshRequest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

class TokenInterceptor(
    private val userPreferences: UserPreferences,
    private val retrofit: Retrofit
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)

        // Check if the response is an unauthorized error and access token has expired
        if (response.code == 401) {
            // Refresh the access token and get the new token
            val newAccessToken = runBlocking { refreshAccessToken() }

            if (newAccessToken != null) {
                // Retry the original request with the new access token
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
                return chain.proceed(newRequest)
            }
        }

        return response
    }

    private suspend fun refreshAccessToken(): String? {
        val refreshToken = userPreferences.getRefreshToken().firstOrNull()

        try {
            val apiService = retrofit.create(ApiService::class.java)
            val response = apiService.refreshAccessToken(RefreshRequest(refreshToken ?: ""))

            val newAccessToken = response.access.token
            // Update the access token in UserPreferences or any other necessary storage
            userPreferences.saveAccessToken(newAccessToken)
            return newAccessToken
        } catch (e: Exception) {
            // Handle any exceptions that occur during the refresh token request
        }

        return null
    }
}
