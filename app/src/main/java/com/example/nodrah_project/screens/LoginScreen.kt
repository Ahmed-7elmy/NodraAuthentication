package com.example.nodrah_project.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nodrah_project.viewModel.LoginViewModel
import com.example.nodrah_project.R
import com.example.nodrah_project.repository.AuthResult
import com.example.nodrah_project.ui.theme.AppTheme
import com.example.nodrah_project.ui.theme.LocalAppColorScheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onGoogleLogin: () -> Unit = {},
    onFacebookLogin: () -> Unit = {},
) {
    AppTheme {

        val colors = LocalAppColorScheme.current


        // Collect ViewModel state
        val email by viewModel.email.collectAsState()
        val password by viewModel.password.collectAsState()
        val isLoading by viewModel.loading.collectAsState()
        val loginResult by viewModel.loginResult.collectAsState()

        var passwordVisible by remember { mutableStateOf(false) }
        var isLogin by remember { mutableStateOf(true) } // Toggle between login/signup

        // Handle login success
        LaunchedEffect(loginResult) {
            if (loginResult is AuthResult.Success) {
                onLoginSuccess()
            }
        }
        val context = LocalContext.current

        val googleSignInClient = remember {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("89832690932-3bc9qvorp598hppa8b73m4al9rtb436c.apps.googleusercontent.com")
                .requestEmail()
                .build()
            GoogleSignIn.getClient(context, gso)
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { authResult ->
                        if (authResult.isSuccessful) {
                            Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                            onLoginSuccess()
                        } else {
                            Toast.makeText(context, "Login failed. Try again.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

            } catch (e: ApiException) {
                Toast.makeText(
                    context,
                    "Google sign-in failed: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(40.dp))

                // Header
                Text(
                    text = "Join Our Community",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.onPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sign in or create an account to connect, share, and thrive in a space designed for everyone.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onPrimary.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Login/Create Account Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = { isLogin = true },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = if (isLogin) colors.onPrimary
                            else colors.onBackground.copy(alpha = 0.6f)
                        )
                    ) {
                        Text(
                            "Log In",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (isLogin) FontWeight.Bold else FontWeight.Normal
                        )
                    }

                    TextButton(
                        onClick = { isLogin = false; onSignUpClick() },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = if (!isLogin) colors.onPrimary
                            else colors.onBackground.copy(alpha = 0.6f)
                        )
                    ) {
                        Text(
                            "Create Account",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (!isLogin) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Email/Username Field
                OutlinedTextField(
                    value = email,
                    onValueChange = viewModel::onEmailChanged,
                    label = { Text("Username/Email", style = TextStyle(color = colors.onPrimary)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colors.background,
                        unfocusedContainerColor = colors.background,
                        focusedBorderColor = colors.onPrimary,
                        cursorColor = colors.onPrimary

                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password Field
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colors.background,
                        unfocusedContainerColor = colors.background,
                        focusedBorderColor = colors.onPrimary,
                        cursorColor = colors.onPrimary
                    ),
                    shape = MaterialTheme.shapes.medium,
                    value = password,
                    onValueChange = viewModel::onPasswordChanged,
                    label = { Text("Password", style = TextStyle(color = colors.onPrimary)) },
                    modifier = Modifier.fillMaxWidth(),

                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                // Error Message
                if (loginResult is AuthResult.Error) {
                    Text(
                        text = (loginResult as AuthResult.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Login Button
                Button(
                    onClick = { viewModel.login() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium,
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.onPrimary,
                        contentColor = colors.primary
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = colors.onPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text("Log In", style = MaterialTheme.typography.labelLarge)
                    }
                }

                // Forgot Password
                TextButton(
                    onClick = onForgotPassword,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        "Forgot password?",
                        color = colors.onPrimary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Divider with "or"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                    Text(
                        text = "or",
                        modifier = Modifier.padding(horizontal = 12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Social Login Buttons
                SocialLoginButton(
                    text = "Continue With Google",
                    icon = painterResource(id = R.drawable.google), // Replace with actual Google icon
                    onClick = {
                        val signInIntent = googleSignInClient.signInIntent
                        launcher.launch(signInIntent)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SocialLoginButton(
                    text = "Continue With Facebook",
                    icon = painterResource(id = R.drawable.facebook), // Replace with actual Facebook icon
                    onClick = onFacebookLogin
                )
                Spacer(modifier = Modifier.height(12.dp))
                SocialLoginButton(
                    text = "Continue With Phone",
                    icon = painterResource(R.drawable.phone),
                    onClick = onFacebookLogin
                )
            }
        }
    }
}

@Composable
private fun SocialLoginButton(
    text: String,
    icon: Painter,
    onClick: () -> Unit,
) {
    AppTheme {
        val colors = LocalAppColorScheme.current


        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = colors.onBackground
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = colors.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = text, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

//UI DONE