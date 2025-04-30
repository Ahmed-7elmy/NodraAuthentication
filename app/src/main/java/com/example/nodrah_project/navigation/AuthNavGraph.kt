package com.example.nodrah_project.navigation

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
    }
}
