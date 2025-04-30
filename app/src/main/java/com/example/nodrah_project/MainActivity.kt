package com.example.nodrah_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.nodrah_project.ui.theme.Nodrah_projectTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.nodrah_project.navigation.authNavGraph
//import com.example.nodrah_project.navigation.authNavGraph
import com.example.nodrah_project.repository.AuthRepository
import androidx.navigation.compose.composable
import androidx.compose.material3.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()

        // Firebase initialization check
        val auth = FirebaseAuth.getInstance()
        val authRepository = AuthRepository(auth)
        Log.d("FirebaseCheck", "Current user: ${auth.currentUser?.email ?: "null"}")

        setContent {
            Nodrah_projectTheme {
                val navController = rememberNavController()
                val startDestination = remember {
                    if (authRepository.isUserLoggedIn()) "home" else "auth"
                }


                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    modifier = Modifier.fillMaxSize()
                ) {
                    authNavGraph(
                        navController = navController,
                        authRepository = authRepository
                    )

                    composable("home") {
                        // Replace with your actual HomeScreen.
                        Text("Welcome to the Home Screen!")
                    }
                }

                }
            }
        }
    }