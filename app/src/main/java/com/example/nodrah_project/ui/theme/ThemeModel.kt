package com.example.nodrah_project.ui.theme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Color scheme structure
data class MainAppColorScheme(
    val background: Color,
    val onBackground: Color,
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color
)

// Default CompositionLocal (will be overridden later)
val LocalAppColorScheme = staticCompositionLocalOf {
    MainAppColorScheme(
        background = Color.White,
        onBackground = Color.Black,
        primary = Color.Blue,
        onPrimary = Color.White,
        secondary = Color.LightGray,
        onSecondary = Color.Black
    )
}
