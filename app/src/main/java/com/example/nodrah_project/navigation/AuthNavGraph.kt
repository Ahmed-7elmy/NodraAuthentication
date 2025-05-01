package com.example.nodrah_project.navigation

import android.R.attr.phoneNumber
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.nodrah_project.screens.LoginScreen
import com.example.nodrah_project.screens.SignUpScreen
import com.example.nodrah_project.screens.EmailVerificationScreen
import com.example.nodrah_project.viewModel.SignUpViewModel
import com.example.nodrah_project.repository.AuthRepository
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.nodrah_project.screens.ForgotPasswordScreen
import com.example.nodrah_project.screens.PhoneVerificationScreen
import com.example.nodrah_project.screens.PutYourPhoneNumberScreen
import com.example.nodrah_project.viewModel.PhoneAuthViewModel


fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    authRepository: AuthRepository = AuthRepository()
) {
    navigation(
        startDestination = "login",
        route = "auth"
    ) {
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
                onVerify = { code -> viewModel.verifyCode(code) },
                onResendCode = { viewModel.resendVerificationCode() }
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
                },
            )
        }
        composable("phone_auth") {
            val viewModel: PhoneAuthViewModel = viewModel()
            val phoneNumber by viewModel.phoneNumber.collectAsState()
            val loading by viewModel.loading.collectAsState()
            val phoneError by viewModel.phoneError.collectAsState()

            PutYourPhoneNumberScreen(
                phoneNumber = phoneNumber,
                onPhoneChanged = viewModel::onPhoneChanged,
                onNextClick = {
                    viewModel.onNextClick {
                        // After the phone number is validated, navigate to the PhoneVerificationScreen
                        navController.navigate("phone_verification/${phoneNumber}") {
                            popUpTo("phone_auth") { inclusive = true }
                        }
                    }
                },
                loading = loading,
                phoneError = phoneError,
                onResetSent = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
                onLoginClick = {
                    navController.navigate("login") {
                        popUpTo("phone_auth") { inclusive = true }
                    }
                }
            )
        }
        // In your navigation graph:
        composable("phone_verification/{phone}") { backStackEntry ->
            PhoneVerificationScreen(
                phone = backStackEntry.arguments?.getString("phone") ?: "",
                onVerificationSuccess = {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }






    }
}
