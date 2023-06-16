package com.magma.virtuhire.ui.screen.splash

import com.magma.virtuhire.data.remote.response.User

data class SplashState(
    val data: User? = null,
    val isLoading: Boolean = true,
    val isLoggedIn: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null
)