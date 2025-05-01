package com.example.nodrah_project.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nodrah_project.repository.AuthRepository
import com.example.nodrah_project.repository.AuthResult
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PhoneVerificationViewModel(
    private val authRepository: AuthRepository,
    private val context: Context
) : ViewModel() {

    companion object {
        private const val MAX_CODE_LENGTH = 6
        private const val RESEND_COOLDOWN_SECONDS = 30
        private const val VERIFICATION_TIMEOUT_SECONDS = 60L
    }

    private val auth = FirebaseAuth.getInstance()
    private val _verificationCode = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _remainingSeconds = MutableStateFlow(RESEND_COOLDOWN_SECONDS)
    private val _isResendEnabled = MutableStateFlow(false)
    private val _verificationSuccess = MutableStateFlow(false)

    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    // Exposed state flows
    val verificationCode: StateFlow<String> = _verificationCode.asStateFlow()
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    val remainingSeconds: StateFlow<Int> = _remainingSeconds.asStateFlow()
    val isResendEnabled: StateFlow<Boolean> = _isResendEnabled.asStateFlow()
    val verificationSuccess: StateFlow<Boolean> = _verificationSuccess.asStateFlow()

    init {
        startCountdown()
    }

    fun onVerificationCodeChanged(code: String) {
        if (code.length <= MAX_CODE_LENGTH) {
            _verificationCode.value = code
            _errorMessage.value = null
        }
    }

    fun verifyCode() {
        if (verificationCode.value.length != MAX_CODE_LENGTH) {
            _errorMessage.value = "Please enter a valid $MAX_CODE_LENGTH-digit code"
            return
        }

        val credential = PhoneAuthProvider.getCredential(
            storedVerificationId ?: run {
                _errorMessage.value = "Verification ID not found"
                return
            },
            verificationCode.value
        )

        _isLoading.value = true

        viewModelScope.launch {
            when (val result = authRepository.signInWithPhoneCredential(credential)) {
                is AuthResult.Success -> _verificationSuccess.value = true
                is AuthResult.Error -> _errorMessage.value = result.message
            }
            _isLoading.value = false
        }
    }

    fun resendCode(phoneNumber: String) {
        _isLoading.value = true
        _errorMessage.value = null
        _isResendEnabled.value = false
        _remainingSeconds.value = RESEND_COOLDOWN_SECONDS

        val currentActivity = context.findActivity() ?: run {
            _isLoading.value = false
            _errorMessage.value = "System error. Please restart the app."
            return
        }

        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(VERIFICATION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .setActivity(currentActivity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    _isLoading.value = false
                    _verificationCode.value = credential.smsCode ?: ""
                    verifyCode()
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    _isLoading.value = false
                    _errorMessage.value = e.message ?: "Failed to send verification code"
                    _isResendEnabled.value = true
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    _isLoading.value = false
                    storedVerificationId = verificationId
                    resendToken = token
                    startCountdown()
                }
            })

        resendToken?.let { optionsBuilder.setForceResendingToken(it) }

        try {
            PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
        } catch (e: Exception) {
            _isLoading.value = false
            _errorMessage.value = e.message ?: "Failed to start verification"
        }
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

class PhoneVerificationViewModelFactory(
    private val authRepository: AuthRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhoneVerificationViewModel::class.java)) {
            return PhoneVerificationViewModel(authRepository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Add this in a separate file (e.g., ContextExtensions.kt)
fun Context.findActivity(): android.app.Activity? = when (this) {
    is android.app.Activity -> this
    is android.content.ContextWrapper -> baseContext.findActivity()
    else -> null
}