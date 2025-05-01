package com.example.nodrah_project.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PhoneVerificationViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _verificationCode = MutableStateFlow("")
    val verificationCode: StateFlow<String> = _verificationCode.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _remainingSeconds = MutableStateFlow(30)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds.asStateFlow()

    private val _isResendEnabled = MutableStateFlow(false)
    val isResendEnabled: StateFlow<Boolean> = _isResendEnabled.asStateFlow()

    private val _verificationSuccess = MutableStateFlow(false)
    val verificationSuccess: StateFlow<Boolean> = _verificationSuccess.asStateFlow()

    private var storedVerificationId: String? = null

    init {
        startCountdown()
    }

    fun onVerificationCodeChanged(code: String) {
        if (code.length <= 6) {
            _verificationCode.value = code
            _errorMessage.value = null
        }
    }

    fun verifyCode(phoneNumber: String) {
        if (verificationCode.value.length != 6) {
            _errorMessage.value = "Please enter a valid 6-digit code"
            return
        }

        _isLoading.value = true

        val credential = PhoneAuthProvider.getCredential(
            storedVerificationId ?: run {
                _isLoading.value = false
                _errorMessage.value = "Verification ID not found"
                return
            },
            verificationCode.value
        )

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    _verificationSuccess.value = true
                } else {
                    _errorMessage.value = task.exception?.message ?: "Verification failed"
                }
            }
    }

    fun resendCode(phoneNumber: String) {
        _isLoading.value = true
        _errorMessage.value = null
        _isResendEnabled.value = false
        _remainingSeconds.value = 30

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    _isLoading.value = false
                    _verificationCode.value = credential.smsCode ?: ""
                    verifyCode(phoneNumber)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    _isLoading.value = false
                    _errorMessage.value = e.message ?: "Failed to send verification code"
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    _isLoading.value = false
                    storedVerificationId = verificationId
                    startCountdown()
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun startCountdown() {
        viewModelScope.launch {
            while (_remainingSeconds.value > 0) {
                delay(1000)
                _remainingSeconds.value -= 1
            }
            _isResendEnabled.value = true
        }
    }
}