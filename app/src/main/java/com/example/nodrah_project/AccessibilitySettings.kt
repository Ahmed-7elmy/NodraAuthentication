package com.example.nodrah_project

import kotlinx.serialization.Serializable

@Serializable
data class AccessibilitySettings(
    val lineHeight:Double=24.0,
    val fontSize:Double=20.0,
    val letterSpacing:Double=0.0,
    val isContrast:Boolean=false,
    val isMonochrome:Boolean=false,
    val useDyslexicFont:Boolean=false,
    val contentScale:Double =400.0



)
