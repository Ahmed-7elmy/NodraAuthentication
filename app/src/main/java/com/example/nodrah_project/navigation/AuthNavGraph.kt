package com.example.nodrah_project.navigation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.nodrah_project.repository.AuthRepository
import com.example.nodrah_project.screens.EmailVerificationScreen
import com.example.nodrah_project.screens.ForgotPasswordScreen
import com.example.nodrah_project.screens.LoginScreen
import com.example.nodrah_project.screens.PutYourPhoneNumberScreen
import com.example.nodrah_project.screens.SignUpScreen
import com.example.nodrah_project.screens.OnboardingScreen
import com.example.nodrah_project.ui.screens.PhoneVerificationScreen
import com.example.nodrah_project.viewModel.PhoneAuthViewModel
import com.example.nodrah_project.viewModel.SignUpViewModel
import com.example.nodrah_project.data.PreferencesHelper


// Helper function to find Activity from Context
fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("No Activity found")
}

// Factory for PhoneAuthViewModel
class PhoneAuthViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhoneAuthViewModel::class.java)) {
            return PhoneAuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    authRepository: AuthRepository = AuthRepository()
) {
    navigation(
        startDestination = "initial_check",
        route = "auth"
    ) {
        composable("initial_check") {
            val context = LocalContext.current
            val isFirstLaunch = PreferencesHelper.isFirstLaunch(context) // Use PreferencesHelper

            LaunchedEffect(Unit) {
                val destination = if (isFirstLaunch) "onboarding" else "login"
                navController.navigate(destination) {
                    popUpTo("initial_check") { inclusive = true }
                }
            }
        }


        composable("onboarding") {
            val context = LocalContext.current
            OnboardingScreen(
                onComplete = {
                    PreferencesHelper.setFirstLaunchCompleted(context) // ✅ use captured context
                    navController.navigate("login") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }



        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                onForgotPassword = { navController.navigate("forgot_password") },
                onSignUpClick = { navController.navigate("signup") }
            )
        }

        composable("signup") {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                onVerificationSent = {
                    navController.navigate("verify_email") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onPhoneLogin = {
                    navController.navigate("phone_auth") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            )
        }

        composable("verify_email") {
            val viewModel: SignUpViewModel = viewModel()
            val email by viewModel.email.collectAsState()

            EmailVerificationScreen(
                email = email,
                onLoginClick = {
                    navController.navigate("login") {
                        popUpTo("verify_email") { inclusive = true }
                    }
                }
            )
        }

        composable("forgot_password") {
            ForgotPasswordScreen(
                onResetSent = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
                onLoginClick = {
                    navController.navigate("login") {
                        popUpTo("forgot_password") { inclusive = true }
                    }
                }
            )
        }

        composable("phone_auth") {
            val vm: PhoneAuthViewModel = viewModel(factory = PhoneAuthViewModelFactory(authRepository))
            val phone by vm.phone.collectAsState()
            val phoneError by vm.phoneError.collectAsState()
            val sending by vm.isSendingCode.collectAsState()
            val context = LocalContext.current
            val activity = context.findActivity()

            PutYourPhoneNumberScreen(
                phoneNumber = phone,
                onPhoneChanged = vm::onPhoneChanged,
                onNextClick = {
                    vm.startVerification(activity) {
                        navController.navigate("phone_verification")
                    }
                },
                loading = sending,
                phoneError = phoneError
            )
        }

        composable("phone_verification") {
            val vm: PhoneAuthViewModel = viewModel(factory = PhoneAuthViewModelFactory(authRepository))
            val phone by vm.phone.collectAsState()
            val otp by vm.otp.collectAsState()
            val otpError by vm.otpError.collectAsState()
            val verifying by vm.isVerifying.collectAsState()
            val remaining by vm.remainingSeconds.collectAsState()
            val canResend by vm.canResend.collectAsState()
            val success by vm.verificationSuccess.collectAsState()
            val context = LocalContext.current
            val activity = context.findActivity()

            LaunchedEffect(success) {
                if (success) navController.navigate("home") {
                    popUpTo("auth") { inclusive = true }
                }
            }

            PhoneVerificationScreen(
                phoneNumber = phone,
                otp = otp,
                onOtpChanged = vm::onOtpChanged,
                onVerify = vm::verifyOtp,
                loading = verifying,
                error = otpError,
                remainingSeconds = remaining,
                canResend = canResend,
                onResend = {
                    vm.resendCode(activity)
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}