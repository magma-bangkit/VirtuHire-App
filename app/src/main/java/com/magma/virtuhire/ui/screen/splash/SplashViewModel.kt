package com.magma.virtuhire.ui.screen.splash

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magma.virtuhire.domain.repository.AuthRepository
import com.magma.virtuhire.domain.repository.JobRepository
import com.magma.virtuhire.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _splashState = mutableStateOf(SplashState())
    val splashState: State<SplashState> = _splashState

    init {
        getLoggedInStatus()
    }

    private fun getLoggedInStatus() {
        viewModelScope.launch {
            authRepository.getLoggedInUser().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _splashState.value = SplashState(
                            data = result.data,
                            isLoading = false,
                            isLoggedIn = true
                        )
                    }

                    is Resource.Error -> {
                        _splashState.value = SplashState(
                            isLoading = false,
                            isError = true,
                            errorMessage = result.message
                        )
                    }

                    is Resource.Loading -> {
                        _splashState.value = SplashState(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }
}