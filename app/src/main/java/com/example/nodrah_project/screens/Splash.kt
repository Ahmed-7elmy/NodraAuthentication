package com.example.nodrah_project.screens

import com.example.nodrah_project.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex


@Composable
fun SplashScreen() {
    val logoOffsetY = remember { Animatable(0f) } // start at center
    val showText = remember { mutableStateOf(false) }
    val showLogo = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        logoOffsetY.animateTo(
            targetValue = 500f, // move down off-screen
            animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
        )
        showLogo.value = false // hide after animation
        showText.value = true // then show text
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF031239)), // dark blue
        contentAlignment = Alignment.Center
    ) {
        if (showLogo.value) {
            Image(
                painter = painterResource(id = R.drawable.curve_shape), // your vector XML file
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .offset(y = logoOffsetY.value.dp)
            )
        }

        AnimatedVisibility(
            visible = showText.value,
            enter = fadeIn(animationSpec = tween(1000)),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Add the stylized word using individual character drawables
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(30.dp))
                    // Replace these with your actual drawable resources for each character
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically)
                    {
                        Image(
                            painter = painterResource(id = R.drawable.no2ta),
                            contentDescription = "S",
                            Modifier
                                .padding(0.66651.dp)
                                .width(25.79256.dp)
                                .height(27.12734.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.no2ta),
                            contentDescription = "K",
                            Modifier
                                .padding(0.66651.dp)
                                .width(27.27408.dp)
                                .height(27.12734.dp)
                        )}
                    Spacer(modifier = Modifier.width(35.dp))
                    Image(
                        painter = painterResource(id = R.drawable.fat7a),
                        contentDescription = "Y",
                        Modifier
                            .padding(0.66651.dp)
                            .width(66.31701.dp)
                            .height(31.63679.dp)
                    )
                    Spacer(modifier = Modifier.width(46.dp))
                    Image(
                        painter = painterResource(id = R.drawable.dama),
                        contentDescription = "Y",
                        Modifier
                            .padding(0.66651.dp)
                            .width(48.4588.dp)
                            .height(47.16158.dp)

                    )

                    // A
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Replace these with your actual drawable resources for each character
                    Image(
                        painter = painterResource(id = R.drawable.heeh),
                        contentDescription = "S",
                        Modifier
                            .padding(0.66651.dp)
                            .width(115.37888.dp)
                            .height(127.70404.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.reeh),
                        contentDescription = "K",
                        Modifier
                            .padding(0.66651.dp)
                            .width(62.15097.dp)
                            .height(123.39569.dp)
                    )
                    Box(
                        modifier = Modifier
                            .width(120.44891.dp)  // Match the width of the largest image
                            .height(115.40136.dp) // Match the height of the largest image
                    ) {
                        // Bottom image (noon_dal)
                        Image(
                            painter = painterResource(id = R.drawable.noon_dal),
                            contentDescription = "Bottom Layer",
                            modifier = Modifier
                                .align(Alignment.BottomCenter) // Adjust alignment as needed
                                .padding(0.66651.dp)
                                .width(120.44891.dp)
                                .height(115.40136.dp)
                        )

                        // Top image (floating above)
                        Image(
                            painter = painterResource(id = R.drawable.no2ta), // Replace with your drawable
                            contentDescription = "Top Layer",
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .offset(x = 28.dp, y = (10).dp)
                                .width(27.00408.dp)
                                .height(27.12734.dp)
                                .zIndex(1f)
                                .padding(0.66651.dp)


                        )
                    }
                    // A
                }
                Text(
                    text = "Noudrah",
                    style = TextStyle(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.W200,
                        fontStyle = FontStyle.Normal,
                        lineHeight = 48.sp,
                        letterSpacing = 16.8.sp,
                        color = Color(0xFF3DEBEC)
                    )
                )

                Text(
                    text = "Communication Without Limits...\nA World Without Barriers",
                    fontSize = 14.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
