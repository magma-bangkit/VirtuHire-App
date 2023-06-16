package com.magma.virtuhire.ui.screen.register

import com.magma.virtuhire.data.remote.response.User

data class RegisterState(
    val userData: User? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null
)