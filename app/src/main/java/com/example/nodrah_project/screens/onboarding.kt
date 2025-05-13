package com.example.nodrah_project.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nodrah_project.R

@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    val messages = listOf(
        "Accessibility for ALL\n\nExperience seamless interaction with professional accessibility features designed for everyone.",
        "Inclusive Design\n\nEnjoy a user-friendly interface crafted to support diverse needs and preferences."
    )
    val imageResources = listOf(
        R.drawable.accessibilityicon,
        R.drawable.connectwithothers
    )
    var currentPage by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background logo image
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Background Logo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay with reduced opacity
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E3A8A).copy(alpha = 0.7f),
                            Color(0xFF6EE7B7).copy(alpha = 0.7f)
                        )
                    )
                )
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        if (dragAmount < -50 && currentPage < messages.size - 1) {
                            currentPage += 1 // Swipe left to right
                        } else if (dragAmount > 50 && currentPage > 0) {
                            currentPage -= 1 // Swipe right to left
                        }
                    }
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onComplete, // Skip triggers onComplete
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(
                            text = "Skip",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))

                // Display the vector image based on current page
                Image(
                    painter = painterResource(id = imageResources[currentPage]),
                    contentDescription = "Onboarding Vector Image",
                    modifier = Modifier.size(250.dp)
                )

                Spacer(modifier = Modifier.height(50.dp))

                Text(
                    text = messages[currentPage],
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                // Next/Continue Button
                Button(
                    onClick = {
                        if (currentPage < messages.size - 1) {
                            currentPage += 1 // Go to next page
                        } else {
                            onComplete() // On last page, trigger onComplete
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Text(
                        text = if (currentPage == messages.size - 1) "Continue" else "Next",
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Page Indicators
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    messages.forEachIndexed { index, _ ->
                        Box(
                            modifier = Modifier
                                .size(if (index == currentPage) 16.dp else 8.dp)
                                .background(
                                    color = if (index == currentPage) Color.White else Color.Gray,
                                    shape = MaterialTheme.shapes.small
                                )
                        )
                        if (index < messages.size - 1) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }
        }
    }
}