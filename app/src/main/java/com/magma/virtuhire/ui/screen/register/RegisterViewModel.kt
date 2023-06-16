package com.magma.virtuhire.ui.screen.register

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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registerState = mutableStateOf(RegisterState())
    val registerState: State<RegisterState> = _registerState

    fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        passwordConfirmation: String,
    ) {
        viewModelScope.launch {
            authRepository.registerUser(
                firstName,
                lastName,
                email,
                password,
                passwordConfirmation,
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _registerState.value = RegisterState(userData = result.data?.user)
                    }
                    is Resource.Error -> {
                        _registerState.value = RegisterState(isError = true, errorMessage = result.message)
                    }
                    is Resource.Loading -> {
                        _registerState.value = RegisterState(isLoading = true)
                    }
                }
            }
        }
    }

}
