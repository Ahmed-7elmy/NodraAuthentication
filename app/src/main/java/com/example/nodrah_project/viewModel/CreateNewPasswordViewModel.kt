package com.example.nodrah_project.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nodrah_project.repository.AuthRepository
import com.example.nodrah_project.repository.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// CreateNewPasswordViewModel.kt
class CreateNewPasswordViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {
    private val _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _uiState = MutableStateFlow<AuthResult?>(null)
    val uiState: StateFlow<AuthResult?> = _uiState

    fun updateNewPassword(password: String) {
        _newPassword.value = password
    }

    fun updateConfirmPassword(password: String) {
        _confirmPassword.value = password
    }

    fun resetPassword(actionCode: String) {
        viewModelScope.launch {
            _uiState.value = try {
                // Implement Firebase password reset with action code
                AuthResult.Success
            } catch (e: Exception) {
                AuthResult.Error(e.message ?: "Password reset failed")
            }
        }
    }
}