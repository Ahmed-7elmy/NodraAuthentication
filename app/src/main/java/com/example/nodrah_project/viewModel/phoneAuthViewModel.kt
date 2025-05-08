package com.example.nodrah_project.viewModel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nodrah_project.repository.AuthRepository
import com.example.nodrah_project.repository.AuthResult
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PhoneAuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_SECONDS = 60L
        private const val RESEND_COOLDOWN = 30
        private const val CODE_LENGTH = 6
    }

    // Phone-entry state
    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone.asStateFlow()
    private val _phoneError = MutableStateFlow<String?>(null)
    val phoneError: StateFlow<String?> = _phoneError.asStateFlow()
    private val _isSendingCode = MutableStateFlow(false)
    val isSendingCode: StateFlow<Boolean> = _isSendingCode.asStateFlow()

    // OTP state
    private val _verificationId = MutableStateFlow<String?>(null)
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    private val _otp = MutableStateFlow("")
    val otp: StateFlow<String> = _otp.asStateFlow()
    private val _otpError = MutableStateFlow<String?>(null)
    val otpError: StateFlow<String?> = _otpError.asStateFlow()
    private val _isVerifying = MutableStateFlow(false)
    val isVerifying: StateFlow<Boolean> = _isVerifying.asStateFlow()
    private val _verificationSuccess = MutableStateFlow(false)
    val verificationSuccess: StateFlow<Boolean> = _verificationSuccess.asStateFlow()

    // Resend cooldown
    private val _remaining = MutableStateFlow(RESEND_COOLDOWN)
    val remainingSeconds: StateFlow<Int> = _remaining.asStateFlow()
    private val _canResend = MutableStateFlow(false)
    val canResend: StateFlow<Boolean> = _canResend.asStateFlow()

    // ——————————————————————
    // Phone-number input handlers
    fun onPhoneChanged(new: String) {
        _phone.value = new
        _phoneError.value = null
    }

    fun startVerification(
        activity: Activity,
        onCodeSent: ()->Unit = {}
    ) {
        val number = _phone.value.trim()
        if (number.length < 10) {
            _phoneError.value = "Enter valid phone number"
            return
        }
        _isSendingCode.value = true
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(number)
            .setTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(cred: PhoneAuthCredential) {
                    // auto‑retrieval: directly sign in
                    signInWithCredential(cred)
                }
                override fun onVerificationFailed(e: FirebaseException) {
                    _isSendingCode.value = false
                    _phoneError.value = e.localizedMessage
                }
                override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                    _isSendingCode.value = false
                    _verificationId.value = id
                    resendToken = token
                    onCodeSent()
                    startCooldown()
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun startCooldown() {
        viewModelScope.launch {
            _remaining.value = RESEND_COOLDOWN
            _canResend.value = false
            while (_remaining.value > 0) {
                delay(1000)
                _remaining.value -= 1
            }
            _canResend.value = true
        }
    }

    fun resendCode(activity: Activity) {
        resendToken?.let { token ->
            val options = PhoneAuthOptions.newBuilder(Firebase.auth)
                .setPhoneNumber(_phone.value)
                .setTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    override fun onVerificationCompleted(cred: PhoneAuthCredential) {
                        signInWithCredential(cred)
                    }
                    override fun onVerificationFailed(e: FirebaseException) {
                        _phoneError.value = e.localizedMessage
                        _canResend.value = true
                    }
                    override fun onCodeSent(id: String, tok: PhoneAuthProvider.ForceResendingToken) {
                        _verificationId.value = id
                        resendToken = tok
                        startCooldown()
                    }
                })
                .setForceResendingToken(token)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    // ——————————————————————
    // OTP input handlers
    fun onOtpChanged(code: String) {
        if (code.length <= CODE_LENGTH) {
            _otp.value = code
            _otpError.value = null
        }
    }

    fun verifyOtp() {
        val id = _verificationId.value
        if (id == null) {
            _otpError.value = "No verification ID"
            return
        }
        if (_otp.value.length != CODE_LENGTH) {
            _otpError.value = "Enter $CODE_LENGTH‑digit code"
            return
        }
        val cred = PhoneAuthProvider.getCredential(id, _otp.value)
        signInWithCredential(cred)
    }

    private fun signInWithCredential(cred: PhoneAuthCredential) {
        viewModelScope.launch {
            _isVerifying.value = true
            when(val r = repository.signInWithPhoneCredential(cred)) {
                is AuthResult.Success -> _verificationSuccess.value = true
                is AuthResult.Error   -> _otpError.value = r.message
            }
            _isVerifying.value = false
        }
    }
}
