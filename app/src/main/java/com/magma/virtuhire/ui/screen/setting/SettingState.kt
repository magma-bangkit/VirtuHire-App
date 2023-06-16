package com.magma.virtuhire.ui.screen.setting

import com.magma.virtuhire.data.remote.response.User

data class SettingState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isLoggedOut: Boolean = false,
    val errorMessage: String? = null
)