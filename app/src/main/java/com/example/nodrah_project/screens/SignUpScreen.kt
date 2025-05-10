package com.example.nodrah_project.screens
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.nodrah_project.R
import com.example.nodrah_project.ui.theme.AppTheme
import com.example.nodrah_project.ui.theme.LocalAppColorScheme
import com.example.nodrah_project.viewModel.SignUpViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider



@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = viewModel(),
    onSignUpSuccess: () -> Unit = {},
    onVerificationSent: () -> Unit = {},
    onFacebookLogin: () -> Unit = {},
    onPhoneLogin: () -> Unit = {}

    ) {
    val colors = LocalAppColorScheme.current

    AppTheme {

        // Collect state from ViewModel
        val email by viewModel.email.collectAsState()
        val password by viewModel.password.collectAsState()
        val confirmPassword by viewModel.confirmPassword.collectAsState()
        val loading by viewModel.loading.collectAsState()
        val signUpResult by viewModel.signUpResult.collectAsState()
        val emailError by viewModel.emailError.collectAsState()
        val passwordError by viewModel.passwordError.collectAsState()
        val verificationSent by viewModel.verificationSent.collectAsState()
        var passwordVisible by remember { mutableStateOf(false) }
        var confirmPasswordVisible by remember { mutableStateOf(false) }
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
                            // ✅ Success: Navigate to home screen
                            Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                            onSignUpSuccess()
                        } else {
                            Toast.makeText(context, "Login failed. Try again.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

            } catch (e: ApiException) {
                // ❌ Google sign-in failed
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
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (verificationSent) {
                    Spacer(modifier = Modifier.height(16.dp))
                    AlertDialog(
                        onDismissRequest = { /* Optional: allow dismissal */ },
                        title = { Text("Verify Your Email",style = TextStyle(color = colors.onPrimary)) },
                        text = {
                            Text(
                                "We've sent a verification link to $email. " +
                                        "Please check your inbox and verify your email address."
                            ,style = TextStyle(color = colors.onPrimary))
                        },
                        confirmButton = {
                            Button(
                                onClick = onVerificationSent
                            ) {
                                Text("Got It",style = TextStyle(color = colors.onPrimary))
                            }
                        }
                    )
                }
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
                    color = colors.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Email Field
                OutlinedTextField(//here is the supposed to change email
                    value = email,
                    onValueChange = viewModel::onEmailChanged,
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = emailError != null,
                    supportingText = {
                        emailError?.let {
                            Text(
                                it,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
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
                    value = password,
                    onValueChange = viewModel::onPasswordChanged,
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = passwordError != null,
                    supportingText = {
                        passwordError?.let {
                            Text(
                                it,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    shape = MaterialTheme.shapes.medium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colors.background,
                        unfocusedContainerColor = colors.background,
                        focusedBorderColor = colors.onPrimary,
                        cursorColor = colors.onPrimary
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Confirm Password Field
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = viewModel::onConfirmPasswordChanged,
                    label = { Text("Confirm Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    shape = MaterialTheme.shapes.medium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colors.background,
                        unfocusedContainerColor = colors.background,
                        focusedBorderColor = colors.onPrimary,
                        cursorColor = colors.onPrimary

                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(24.dp))
                // Sign Up Button
                Button(
                    onClick = { viewModel.signUp() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium,
                    enabled = !loading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.onPrimary,
                        contentColor = colors.primary
                    )
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            color = colors.onPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text("Create Account", style = MaterialTheme.typography.labelLarge)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Divider with "or"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = colors.onBackground.copy(alpha = 0.3f)
                    )
                    Text(
                        text = "or",
                        modifier = Modifier.padding(horizontal = 12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onBackground.copy(alpha = 0.3f)
                    )
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = colors.onBackground.copy(alpha = 0.3f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Social Login Buttons signup
                SocialLoginButton(

                    text = "Continue With Google",
                    icon = painterResource(R.drawable.google),
                    onClick = {
                        val signInIntent = googleSignInClient.signInIntent
                        launcher.launch(signInIntent)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SocialLoginButton(
                    text = "Continue With Facebook",
                    icon = painterResource(R.drawable.facebook), // Replace with actual Facebook icon
                    onClick = onFacebookLogin
                )
                Spacer(modifier = Modifier.height(12.dp))
                SocialLoginButton(
                    text = "Continue With Phone",
                    icon = painterResource(R.drawable.phone),
                    onClick = onPhoneLogin
                )
            }
        }
    }
}
@Composable
private fun SocialLoginButton(
    text: String,
    icon: Painter,
    onClick: () -> Unit
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
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),colors.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = text, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}