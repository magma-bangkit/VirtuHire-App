package com.magma.virtuhire.ui.screen.setting

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magma.virtuhire.domain.repository.AuthRepository
import com.magma.virtuhire.domain.repository.JobRepository
import com.magma.virtuhire.ui.screen.home.ProfileState
import com.magma.virtuhire.ui.screen.splash.SplashState
import com.magma.virtuhire.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private var _profileState = mutableStateOf(ProfileState())
    val profileState: State<ProfileState> = _profileState

    private var _logOutState = mutableStateOf(SettingState())
    val logOutState: State<SettingState> = _logOutState

    init {
        getLoggedInUser()
    }

    private fun getLoggedInUser() {
        viewModelScope.launch {
            authRepository.getLoggedInUser().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _profileState.value = ProfileState(
                            profile = result.data,
                            isLoading = false,
                        )
                    }

                    is Resource.Error -> {
                        _profileState.value = ProfileState(
                            isLoading = false,
                            isError = true,
                            errorMessage = result.message
                        )
                    }

                    is Resource.Loading -> {
                        _profileState.value = ProfileState(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    fun logOutUser() {
        viewModelScope.launch {
            authRepository.logOutUser().collect {result ->
                when (result) {
                    is Resource.Success -> {
                        _logOutState.value = SettingState(
                            isLoading = false,
                            isLoggedOut = true,
                            isError = false,
                        )
                    }

                    is Resource.Error -> {
                        _logOutState.value = SettingState(
                            isLoading = false,
                            isError = true,
                            errorMessage = result.message
                        )
                    }

                    is Resource.Loading -> {
                        _logOutState.value = SettingState(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

}