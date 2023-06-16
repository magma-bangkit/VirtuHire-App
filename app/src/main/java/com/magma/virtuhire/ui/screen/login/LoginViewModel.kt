package com.magma.virtuhire.ui.screen.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magma.virtuhire.domain.repository.AuthRepository
import com.magma.virtuhire.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            authRepository.loginUser(username, password)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _loginState.value = LoginState(userData = result.data?.user)
                        }
                        is Resource.Error -> {
                            _loginState.value = LoginState(isError = true, errorMessage = result.message)
                        }
                        is Resource.Loading -> {
                            _loginState.value = LoginState(isLoading = true)
                        }
                    }
                }
        }
    }
}
