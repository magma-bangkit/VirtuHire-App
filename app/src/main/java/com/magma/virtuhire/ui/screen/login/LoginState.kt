package com.magma.virtuhire.ui.screen.login

import com.magma.virtuhire.data.remote.response.User

data class LoginState(
    val userData: User? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null
)