package com.example.nodrah_project

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nodrah_project.ui.theme.AppTheme
import com.example.nodrah_project.ui.theme.DyslexicFont
import com.example.nodrah_project.ui.theme.LocalAppColorScheme
import java.util.Locale

class AccessibilityActivity : ComponentActivity() {
    val talkAndTypeParser by lazy {
        TalkAndType(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            var selectedTab by remember { mutableStateOf(0) }

            AccessibilityScreen(
                talkAndTypeParser = talkAndTypeParser,
                selectedItem = selectedTab,
                onItemSelected = { selectedTab = it }
            )
        }
    }
}


@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessibilityScreen(
    talkAndTypeParser: TalkAndType,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
) {



    var isContrast by remember { mutableStateOf(false) }
    var isMonochromeTheme by remember { mutableStateOf(false) }
    var useCustomFont by remember { mutableStateOf(false) }
    val currentFont = if (useCustomFont) DyslexicFont else FontFamily.Default

    var lineHeightVal by remember { mutableStateOf(24.0) }
    var fontSizeVal by remember { mutableStateOf(20.0) }
    var letterSpacingVal by remember { mutableStateOf(0.0) }
    var contentScaleVal by remember { mutableStateOf(400.0) }

    var canRecord by remember { mutableStateOf(false) }

    val recordAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            canRecord = isGranted
        }
    )
    LaunchedEffect(recordAudioLauncher) {
        recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
    val state by talkAndTypeParser.state.collectAsState()
    var isListening by remember { mutableStateOf(false) }

    AppTheme(isContrastTheme = isContrast, isMonoChrome = isMonochromeTheme) {
        var selectedTab by remember { mutableStateOf(0) }
        val context = LocalContext.current
        val colors = LocalAppColorScheme.current


        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Accessibility Preferences ") },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = colors.background, titleContentColor = colors.onBackground
                    )
                )
            },
            bottomBar = {
                BottomNavigationBar(selectedItem = selectedTab) {
                    selectedTab = it
                }
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
                        isContrast = !isContrast
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
                        useCustomFont = !useCustomFont
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
                        style = TextStyle(color = colors.onBackground, fontSize = fontSizeVal.sp),
                        modifier = Modifier.padding(8.dp),
                        fontFamily = currentFont,
                    )
                }
                if (state.error != null) {
                    Text(
                        "Error: ${state.error}",
                        style = TextStyle(color = Color.Red, fontSize = fontSizeVal.sp),
                        fontFamily = currentFont,
                        modifier = Modifier.padding(8.dp)
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
                        letterSpacingValue = letterSpacingVal,
                        onLetterSpacingChange = { letterSpacingVal = it },
                        )
                    CardLineHeight(
                        "Line Height",
                        fontFamily = currentFont,
                        lineheightValue = lineHeightVal,
                        onLineHeightChange = { lineHeightVal = it },            )
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
                        fontSizeValue = fontSizeVal,              // need to be changed
                        onFontSizeChange = { fontSizeVal = it }      // need to be changed
                    )
                    CardContentScale(
                        "Content Scale",
                        fontFamily = currentFont,
                        contentScaleValue = contentScaleVal,              // need to be changed
                        onContentScaleChange = { contentScaleVal = it }      // need to be changed
                    )
                }
                RowButton(
                    "Hide Images",
                    "Monochrome",
                    R.drawable.hide_image_icon,
                    R.drawable.monochrome_icon,
                    onClick1 = { /*Hide Images logic*/ },
                    onClick2 = {
                        isMonochromeTheme = !isMonochromeTheme
                    },
                    fontFamily = currentFont
                )
                Text(
                    "Lorem Ipsum\n" +
                            "\"Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit...\"\n" +
                            "\"There is no one who loves pain itself, who seeks after it and wants to have it, simply because it is pain...\"",
                    style = TextStyle(
                        lineHeight = lineHeightVal.sp,
                        color = colors.onBackground,
                        fontSize = fontSizeVal.sp,
                        letterSpacing = letterSpacingVal.sp
                    ),
                    fontFamily = currentFont,
                    modifier = Modifier.padding(8.dp)
                )
                val imageModifier = Modifier
                    .size(contentScaleVal.dp)
                    .border(BorderStroke(1.dp, Color.Black))
                    .background(Color.White)
                Image(
                    painter = painterResource(id = R.drawable.demopic),
                    contentDescription = null,
//                    contentScale = ContentScale.Fit,
                    modifier = imageModifier,
                    colorFilter = if (isMonochromeTheme) ColorFilter.colorMatrix(ColorMatrix().apply {
                        setToSaturation(
                            0f
                        )
                    }) else null
                )

            }
        }
    }
}