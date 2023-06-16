package com.magma.virtuhire.ui.screen.home

import com.magma.virtuhire.data.remote.response.User

data class ProfileState(
    val profile: User? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null
)