package com.example.nodrah_project.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PhoneAuthViewModel : ViewModel() {
    private val _phoneNumber = MutableStateFlow("+2")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _phoneError = MutableStateFlow<String?>(null)
    val phoneError: StateFlow<String?> = _phoneError

    fun onPhoneChanged(newPhone: String) {
        _phoneNumber.value = newPhone
        _phoneError.value = null
    }

    fun onNextClick(navigateToVerificationScreen: () -> Unit) {
        val trimmed = _phoneNumber.value.trim()
        if (trimmed.isEmpty() || trimmed.length < 10) {
            _phoneError.value = "Enter a valid phone number"
            return
        }

        _loading.value = true
        // Simulate sending verification code (replace with Firebase later)
        // For now, navigate directly after delay
        navigateToVerificationScreen()
        _loading.value = false
    }
}
