package com.example.nodrah_project.screens

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nodrah_project.repository.AuthRepository
import com.example.nodrah_project.viewModel.PhoneVerificationViewModel
import com.example.nodrah_project.viewModel.PhoneVerificationViewModelFactory
import kotlinx.coroutines.delay


@Composable
fun PhoneVerificationScreen(
    phone: String,
    authRepository: AuthRepository,
    onVerificationSuccess: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    viewModel: PhoneVerificationViewModel = viewModel(
        factory = PhoneVerificationViewModelFactory(
            authRepository,
            context = TODO()
        )
    )
) {
    val verificationCode by viewModel.verificationCode.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val remainingSeconds by viewModel.remainingSeconds.collectAsState()
    val isResendEnabled by viewModel.isResendEnabled.collectAsState()

    // Handle verification success
    LaunchedEffect(viewModel.verificationSuccess.collectAsState().value) {
        if (viewModel.verificationSuccess.value) {
            onVerificationSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Header
        Text(
            text = "Verify Your Phone Number",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Phone display
        Text(
            text = "We've sent a verification code to",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Text(
            text = phone,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // OTP Input
        OtpTextField(
            otpText = verificationCode,
            onOtpTextChange = { code ->
                viewModel.onVerificationCodeChanged(code)
                if (code.length == 6) {
                    viewModel.verifyCode()
                }
            },
            isError = errorMessage != null
        )

        // Error message
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Verify Button
        Button(
            onClick = { viewModel.verifyCode() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = verificationCode.length == 6 && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Verify", style = MaterialTheme.typography.labelLarge)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Resend Code Section
        Divider(modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Didn't Receive Code?",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = {
                if (isResendEnabled) {
                    viewModel.resendCode(phone)
                }
            },
            enabled = isResendEnabled
        ) {
            Text(
                text = if (isResendEnabled) "Resend Code"
                else "Resend code in ${remainingSeconds}s",
                color = if (isResendEnabled) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }

        // Back button
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = onNavigateBack) {
            Text("Back to Phone Entry")
        }
    }
}

@Composable
fun OtpTextField(
    otpText: String,
    onOtpTextChange: (String) -> Unit,
    isError: Boolean = false
) {
    BasicTextField(
        value = otpText,
        onValueChange = onOtpTextChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(6) { index ->
                    val char = when {
                        index >= otpText.length -> ""
                        else -> otpText[index].toString()
                    }
                    val isFocused = otpText.length == index

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .border(
                                width = 1.dp,
                                color = when {
                                    isError -> MaterialTheme.colorScheme.error
                                    isFocused -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.outline
                                },
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    )
}
