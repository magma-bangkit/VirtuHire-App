package com.magma.virtuhire.utils

import com.google.gson.Gson
import com.magma.virtuhire.data.remote.response.ErrorResponse
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

fun handleError(error: Throwable): String {
    return when (error) {
        is HttpException -> {
            val response = error.response()
            if (response != null) {
                val errorBody = response.errorBody()?.string()
                if (!errorBody.isNullOrEmpty()) {
                    try {
                        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        // Extract and return the error message
                        when (val message = errorResponse.message) {
                            is String -> message
                            is List<*> -> message.joinToString(", ")
                            else -> "Unknown error message format"
                        }
                        // errorResponse.message.toString()
                    } catch (e: Exception) {
                        // Handle any Gson parsing exceptions
                        "Error parsing error response"
                    }
                } else {
                    // Error body is empty or null
                    "Empty or null error response body"
                }
            } else {
                // Response is null
                "Null error response"
            }
        }

        is IOException -> {
            "IO Exception: ${error.message}"
        }

        is TimeoutException -> {
            "Timeout Exception: ${error.message}"
        }

        else -> error.message ?: "Unknown Error"
    }
}

