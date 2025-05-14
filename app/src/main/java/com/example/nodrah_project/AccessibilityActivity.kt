package com.example.nodrah_project

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.Accessibility
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nodrah_project.ui.theme.AppTheme
import com.example.nodrah_project.ui.theme.DyslexicFont
import com.example.nodrah_project.ui.theme.LocalAppColorScheme
import kotlinx.coroutines.launch
import java.util.Locale

class AccessibilityActivity : ComponentActivity() {
    val talkAndTypeParser by lazy {
        TalkAndType(application)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val accessibilitySettingsManager =
                (application as MyApplication).accessibilitySettingsManager

            val settings by accessibilitySettingsManager.accessibilitySettingsFlow.collectAsState(
                initial = AccessibilitySettings()
            )
            var selectedTab by remember { mutableStateOf(0) } // This state is for your bottom nav bar if you have one


            AccessibilityScreen(
                talkAndTypeParser = talkAndTypeParser,
                currentSettings = settings,
                onSettingsChange = { newSettings ->
                    (application as MyApplication).applicationScope.launch {
                        accessibilitySettingsManager.saveSettings(newSettings)
                    }
                },
                selectedItem = selectedTab,
                onItemSelected = { selectedTab = it }
            )
        }
    }



    @SuppressLint("UnusedContentLambdaTargetStateParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AccessibilityScreen(
        talkAndTypeParser: TalkAndType,
        currentSettings: AccessibilitySettings,
        onSettingsChange: (AccessibilitySettings) -> Unit,
        selectedItem: Int,
        onItemSelected: (Int) -> Unit,
    ) {

        val currentFont = if (currentSettings.useDyslexicFont) DyslexicFont else FontFamily.Default

        var canRecord by remember { mutableStateOf(false) }

        val recordAudioLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(), onResult = { isGranted ->
                canRecord = isGranted
            })
        LaunchedEffect(recordAudioLauncher) {
            recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }

        val state by talkAndTypeParser.state.collectAsState()
        var isListening by remember { mutableStateOf(false) }

        // Use rememberCoroutineScope to launch coroutines from a composable
        val coroutineScope = rememberCoroutineScope()

        AppTheme(isContrastTheme = currentSettings.isContrast, currentSettings.isMonochrome) {
            var selectedTab by remember { mutableStateOf(0) }
            val context = LocalContext.current
            val colors = LocalAppColorScheme.current
            Scaffold(topBar = {
                TopAppBar(
                    title = { Text("Accessibility Preferences ") },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = if (currentSettings.isContrast)colors.onBackground else colors.onPrimary,
                        titleContentColor =  if (currentSettings.isContrast)colors.background else colors.primary
                    )
                )
            }, bottomBar = {
                BottomNavigationBar(
                    selectedItem = selectedTab,
                    onItemSelected = { selectedTab = it },
                    currentSettings = currentSettings
                )
            })


            { innerPadding -> // <-- Important for avoiding overlap

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.background)
                        .padding(innerPadding) // applies padding from Scaffold (topBar)
                        .padding(top = 30.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    RowButton(
                        "Voice Navigation",
                        if (isListening) "Stop Talk & Type" else "Talk & Type",
                        R.drawable.voice_navigation_icon,
                        if (isListening) R.drawable.talk_type_icon else R.drawable.talk_type_icon,
                        onClick1 = { /* voice nav logic */ },
                        onClick2 = {
                            if (canRecord) {
                                isListening = !isListening
                                if (isListening) {
                                    talkAndTypeParser.startListening(Locale.getDefault().language)
                                } else {
                                    talkAndTypeParser.stopListening()
                                }
                            } else {
                                // Optionally show a message that recording permission is needed
                            }
                        },
                        fontFamily = currentFont,
                    )
                    RowButton(
                        "Screen Reader",
                        "Dark Contrast",
                        R.drawable.screen_reader_icon,
                        R.drawable.dark_contrast_icon,
                        onClick1 = { /* screen reader logic */ },
                        onClick2 = {
                            onSettingsChange(currentSettings.copy(isContrast = !currentSettings.isContrast))
                        },
                        fontFamily = currentFont,
                    )
                    RowButton(
                        "Reading Mask",
                        "Dyslexia Font",
                        R.drawable.reading_mask_icon,
                        R.drawable.dyslexia_font_icon,
                        onClick1 = { /*Reading Mask logic*/ },
                        onClick2 = {
                            onSettingsChange(currentSettings.copy(useDyslexicFont = !currentSettings.useDyslexicFont))
                        },
                        fontFamily = currentFont
                    )
                    RowButton(
                        "Invert Color",
                        "Stop Animation",
                        R.drawable.invert_color_icon,
                        R.drawable.stop_animation_icon,
                        onClick1 = { /*Invert Color logic*/ },
                        onClick2 = { /*Stop Animation logic*/ },
                        fontFamily = currentFont,
                    )

                    // Display the spoken text and any errors
                    if (state.spokenText.isNotBlank()) {
                        Text(
                            "Spoken Text: ${state.spokenText}",
                            style = TextStyle(
                                color = colors.onBackground, fontSize = currentSettings.fontSize.sp
                            ),
                            modifier = Modifier.padding(8.dp),
                            fontFamily = currentFont,
                        )
                    }
                    if (state.error != null) {
                        Text(
                            "Error: ${state.error}", style = TextStyle(
                                color = Color.Red, fontSize = currentSettings.fontSize.sp
                            ), fontFamily = currentFont, modifier = Modifier.padding(8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CardLetterSpacing(
                            "Letter Spacing",
                            fontFamily = currentFont,
                            letterSpacingValue = currentSettings.letterSpacing,
                            onLetterSpacingChange = { newValue ->
                                onSettingsChange(currentSettings.copy(letterSpacing = newValue))
                            }
                        )
                        CardLineHeight(
                            "Line Height",
                            fontFamily = currentFont,
                            lineheightValue = currentSettings.lineHeight,
                            onLineHeightChange = { newValue ->
                                onSettingsChange(currentSettings.copy(lineHeight = newValue))
                            }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CardFontSize(
                            "Font Size ",
                            fontFamily = currentFont,
                            fontSizeValue = currentSettings.fontSize,              // need to be changed
                            onFontSizeChange = { newValue ->
                                onSettingsChange(currentSettings.copy(fontSize = newValue))
                            }
                        )
                        CardContentScale(
                            "Content Scale",
                            fontFamily = currentFont,
                            contentScaleValue = currentSettings.contentScale,              // need to be changed
                            onContentScaleChange = { newValue ->
                                onSettingsChange(currentSettings.copy(contentScale = newValue))
                            }
                        )
                    }
                    RowButton(
                        "Hide Images",
                        "Monochrome",
                        R.drawable.hide_image_icon,
                        R.drawable.monochrome_icon,
                        onClick1 = { /*Hide Images logic*/ },
                        onClick2 = {
                            onSettingsChange(currentSettings.copy(isMonochrome = !currentSettings.isMonochrome))
                        },
                        fontFamily = currentFont
                    )

                    val imageModifier = Modifier
                        .size(currentSettings.contentScale.dp)
                        .border(BorderStroke(1.dp, Color.Black))
                        .background(Color.White)
                    Image(
                        painter = painterResource(id = R.drawable.demopic),
                        contentDescription = null,
                    contentScale = ContentScale.Fit,
                        modifier = imageModifier,

                        )
                }
            }
        }
    }
}

