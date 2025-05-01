package com.example.nodrah_project.viewModel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nodrah_project.repository.AuthRepository
import com.example.nodrah_project.repository.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    // State Flows
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _signUpResult = MutableStateFlow<AuthResult?>(null)
    val signUpResult: StateFlow<AuthResult?> = _signUpResult.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    private val _verificationSent = MutableStateFlow(false)
    val verificationSent: StateFlow<Boolean> = _verificationSent.asStateFlow()

    // Form Handling
    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
        _emailError.value = null
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
        _passwordError.value = null
    }

    fun onConfirmPasswordChanged(newPassword: String) {
        _confirmPassword.value = newPassword
    }

    fun signUp() {
        if (!validateForm()) return

        viewModelScope.launch {
            _loading.value = true
            _signUpResult.value = try {
                val result = authRepository.signUp(_email.value, _password.value)
                if (result is AuthResult.Success) {
                    sendVerificationEmail()
                }
                result
            } catch (e: Exception) {
                AuthResult.Error(e.message ?: "Sign up failed")
            }
            _loading.value = false
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        if (_email.value.isBlank()) {
            _emailError.value = "Email is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()) {
            _emailError.value = "Enter a valid email"
            isValid = false
        }

        if (_password.value.length < 6) {
            _passwordError.value = "Password needs 6+ characters"
            isValid = false
        } else if (_password.value != _confirmPassword.value) {
            _passwordError.value = "Passwords don't match"
            isValid = false
        }

        return isValid
    }

    // Email Verification
    private suspend fun sendVerificationEmail() {
        _loading.value = true
        val result = authRepository.sendVerificationEmail()
        _signUpResult.value = result
        if (result is AuthResult.Success) {
            _verificationSent.value = true
        }
        _loading.value = false
    }

    fun resetVerificationState() {
        _verificationSent.value = false
    }

    // Google Sign-In
    fun signInWithGoogle(idToken: String) {
        _loading.value = true
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                _loading.value = false
                _signUpResult.value = if (task.isSuccessful) {
                    AuthResult.Success
                } else {
                    AuthResult.Error(task.exception?.message ?: "Google sign-in failed")
                }
            }
    }



}