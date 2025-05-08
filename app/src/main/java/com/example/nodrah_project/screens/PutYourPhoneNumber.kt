package com.example.nodrah_project.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PutYourPhoneNumberScreen(
    phoneNumber: String = "",
    onPhoneChanged: (String) -> Unit = {},
    onNextClick: () -> Unit = {},
    loading: Boolean = false,
    phoneError: String? = null,
    onResetSent: () -> Unit = {},
    onBack: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Enter your phone number",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        val prefix = "+2"

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                    onPhoneChanged(newValue)
                }
            },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = phoneError != null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            prefix = { Text(prefix) } // Visually show +2
        )

        if (phoneError != null) {
            Text(
                text = phoneError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Append +2 to phoneNumber before triggering onNextClick
                if (phoneNumber.isNotEmpty()) {
                    onPhoneChanged(prefix + phoneNumber)
                }
                onNextClick()
            },
            enabled = !loading && phoneNumber.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (loading) "Sending..." else "Next")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { onLoginClick() }) {
            Text("Back to Login")
        }
    }
}