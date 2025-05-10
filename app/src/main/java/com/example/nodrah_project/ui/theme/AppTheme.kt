package com.example.nodrah_project.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AppTheme(
    isContrastTheme: Boolean = false,
    isMonoChrome:Boolean=false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        isMonoChrome -> MonochromeAppColorScheme
        isContrastTheme -> ContrastAppColorScheme
        else -> LightAppColorScheme
    }

    CompositionLocalProvider(
        LocalAppColorScheme provides colorScheme
    ) {
        content()
    }
}
