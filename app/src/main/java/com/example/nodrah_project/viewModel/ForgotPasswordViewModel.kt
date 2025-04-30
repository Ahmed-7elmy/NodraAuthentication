package com.example.nodrah_project.viewModel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nodrah_project.repository.AuthRepository
import com.example.nodrah_project.repository.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    // Private mutable state
    private val _email = MutableStateFlow("")
    private val _loading = MutableStateFlow(false)
    private val _resetResult = MutableStateFlow<AuthResult?>(null)
    private val _emailError = MutableStateFlow<String?>(null)

    // Public immutable state
    val email: StateFlow<String> = _email.asStateFlow()
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    val resetResult: StateFlow<AuthResult?> = _resetResult.asStateFlow()
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
        _emailError.value = null // Clear error when user types
    }

    fun sendResetLink() {
        if (!validateEmail()) return

        viewModelScope.launch {
            _loading.value = true
            _resetResult.value = try {
                authRepository.sendPasswordReset(_email.value)
            } catch (e: Exception) {
                AuthResult.Error(e.message ?: "Failed to send reset link")
            }
            _loading.value = false
        }
    }

    private fun validateEmail(): Boolean {
        return when {
            _email.value.isBlank() -> {
                _emailError.value = "Email is required"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(_email.value).matches() -> {
                _emailError.value = "Please enter a valid email"
                false
            }
            else -> true
        }
    }

    fun resetState() {
        _resetResult.value = null
        _emailError.value = null
    }
}