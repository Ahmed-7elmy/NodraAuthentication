package com.example.nodrah_project.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


@Composable
fun EmailVerificationScreen(
    email: String,
    onVerify: (code: String) -> Unit,
    onResendCode: () -> Unit,
    cooldownSeconds: Int = 30
) {
    var verificationCode by remember { mutableStateOf("") }
    var remainingSeconds by remember { mutableStateOf(cooldownSeconds) }
    var isResendEnabled by remember { mutableStateOf(false) }

    // Countdown timer for resend button
    LaunchedEffect(remainingSeconds) {
        if (remainingSeconds > 0) {
            delay(1000)
            remainingSeconds--
            isResendEnabled = false
        } else {
            isResendEnabled = true
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
            text = "Please Check your Email",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email display
        Text(
            text = "We've sent a code to",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Text(
            text = email,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // OTP Input
        OtpTextField(
            otpText = verificationCode,
            onOtpTextChange = {
                if (it.length <= 6) verificationCode = it
                if (it.length == 6) onVerify(it)
            }
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Verify Button
        Button(
            onClick = { onVerify(verificationCode) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = verificationCode.length == 6
        ) {
            Text("Verify", style = MaterialTheme.typography.labelLarge)
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
                    onResendCode()
                    remainingSeconds = cooldownSeconds
                }
            },
            enabled = isResendEnabled
        ) {
            Text(
                text = if (isResendEnabled) "Resend Code"
                else "Send code again in ${remainingSeconds.toString().padStart(2, '0')}",
                color = if (isResendEnabled) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun OtpTextField(
    otpText: String,
    onOtpTextChange: (String) -> Unit
) {
    BasicTextField(
        value = otpText,
        onValueChange = onOtpTextChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
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