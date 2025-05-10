package com.example.nodrah_project.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nodrah_project.ui.theme.AppTheme
import com.example.nodrah_project.ui.theme.LocalAppColorScheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailVerificationScreen(

    email: String,
    onLoginClick: () -> Unit, // Add this parameter
) {
    AppTheme {
        val colors = LocalAppColorScheme.current

        Scaffold(topBar = {
            TopAppBar(
                title = { Text("Email Verification ") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = colors.primary, titleContentColor = colors.onPrimary
                )
            )
        }) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(colors.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Please Check your Email",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = colors.onPrimary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "We've sent an Email to your email address $email",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onPrimary.copy(alpha = 0.7f)
                )

                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onLoginClick, // Use the callback here
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.onPrimary,
                        contentColor = colors.primary
                    )
                ) {
                    Text(
                        "login please",
                        style = MaterialTheme.typography.labelLarge,
                        color = colors.primary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun previewCOm() {
    EmailVerificationScreen("", {})
}



//UI DONE
