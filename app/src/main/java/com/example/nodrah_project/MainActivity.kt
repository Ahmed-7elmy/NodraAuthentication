package com.example.nodrah_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.example.nodrah_project.HomeActivity
import com.example.nodrah_project.navigation.authNavGraph
import com.example.nodrah_project.repository.AuthRepository
import com.example.nodrah_project.ui.theme.Nodrah_projectTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()

        val auth = FirebaseAuth.getInstance()
        val authRepository = AuthRepository(auth)
        Log.d("FirebaseCheck", "Current user: ${auth.currentUser?.email ?: "null"}")

        setContent {
            Nodrah_projectTheme {
                val navController = rememberNavController()
                val startDestination = remember {
                    if (authRepository.isUserLoggedIn()) "home" else "auth"
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        authNavGraph(
                            navController = navController,
                            authRepository = authRepository
                        )

                        composable("home") {
                            HomeScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Welcome to the Home Screen!")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                context.startActivity(Intent(context, HomeActivity::class.java))
            }) {
                Text("Start")
            }
        }
    }
}
