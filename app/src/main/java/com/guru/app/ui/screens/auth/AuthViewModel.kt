package com.guru.app.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guru.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<String?>(null)
    val authState: StateFlow<String?> = _authState

    fun login(email: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val success = authRepository.login(email, pass)
            if (success) {
                onSuccess()
            } else {
                _authState.value = "Invalid email or password"
            }
        }
    }

    fun signup(name: String, email: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val success = authRepository.signup(name, email, pass)
            if (success) {
                onSuccess()
            } else {
                _authState.value = "Please enter valid signup details"
            }
        }
    }

    fun loginAsGuest(onSuccess: () -> Unit) {
        viewModelScope.launch {
            authRepository.loginAsGuest()
            onSuccess()
        }
    }
}
