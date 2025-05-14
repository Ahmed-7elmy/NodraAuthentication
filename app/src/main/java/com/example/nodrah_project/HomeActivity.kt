package com.example.nodrah_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.Accessibility
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nodrah_project.ui.theme.AppTheme
import com.example.nodrah_project.ui.theme.DyslexicFont
import com.example.nodrah_project.ui.theme.LocalAppColorScheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val accessibilitySettingsManager =
                (application as MyApplication).accessibilitySettingsManager
            val currentSettings by accessibilitySettingsManager.accessibilitySettingsFlow.collectAsState(
                initial = AccessibilitySettings()
            )
            var selectedTab by remember { mutableStateOf(0) } // This state is for your bottom nav bar if you have one


            AppTheme(
                isContrastTheme = currentSettings.isContrast,
                isMonoChrome = currentSettings.isMonochrome,
            ) {
                HomeScreen(
                    selectedItem = selectedTab,
                    onItemSelected = { selectedTab = it },
                    currentSettings = currentSettings
                )


            }
        }
    }
}


@Composable
fun HeaderSection(currentSettings: AccessibilitySettings) {

    val currentFont = if (currentSettings.useDyslexicFont) DyslexicFont else FontFamily.Default
    val colors = LocalAppColorScheme.current


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Top Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Noudrah",
                fontSize = currentSettings.fontSize.sp,
                color = if (currentSettings.isContrast) colors.onBackground else colors.onPrimary,
                fontFamily = currentFont
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Dots Icon",
                )
                Icon(
                    imageVector = Icons.Default.MailOutline,
                    contentDescription = "Mail Icon",
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Search Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .background(colors.primary)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.images),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape),
                colorFilter = if (currentSettings.isMonochrome) ColorFilter.colorMatrix(
                    ColorMatrix().apply {
                        setToSaturation(
                            0f
                        )
                    }) else null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Start your Journey...",
                color = colors.onPrimary,
                fontFamily = currentFont

            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.AccountBox, contentDescription = "Profile Icon")
        }
    }
}




@Composable
fun StoriesSection(currentSettings: AccessibilitySettings) {
    val stories: List<Int?> = listOf(
        null, // First item for the "+" button
        R.drawable.arthur,
        R.drawable.james,
        R.drawable.kratos,
        R.drawable.lara,
        R.drawable.ghost,
        R.drawable.tiger
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(stories) { index, item ->
            if (item == null) {
                AddStoryCard()
            } else {
                StoryImageCard(imageRes = item, currentSettings =currentSettings )
            }
        }
    }
}

@Composable
fun AddStoryCard() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Story",
            tint = Color.DarkGray,
            modifier = Modifier.size(32.dp),
        )
    }
}

@Composable
fun StoryImageCard(@DrawableRes imageRes: Int,currentSettings: AccessibilitySettings) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Story Image",
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentScale = ContentScale.Crop,
        colorFilter = if (currentSettings.isMonochrome) ColorFilter.colorMatrix(
            ColorMatrix().apply {
                setToSaturation(
                    0f
                )
            }) else null
    )
}


@Composable
fun HomeScreen(
    currentSettings: AccessibilitySettings,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
) {

    AppTheme(
        isContrastTheme = currentSettings.isContrast,
        isMonoChrome = currentSettings.isMonochrome,
    ) {
        val colors = LocalAppColorScheme.current
        val currentFont = if (currentSettings.useDyslexicFont) DyslexicFont else FontFamily.Default


        var selectedTab by remember { mutableStateOf(0) }
        val viewModel: RedditViewModel = viewModel()
        val posts by viewModel.posts.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()
        val error by viewModel.error.collectAsState()


        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colors.background),
            bottomBar = {
                BottomNavigationBar(
                    selectedItem = selectedTab,
                    onItemSelected = { selectedTab = it },
                    currentSettings = currentSettings
                )
            }) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.background)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()

                ) {
                    item { HeaderSection(currentSettings) }
                    item { StoriesSection(currentSettings) }
                    when {
                        isLoading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = colors.onPrimary)
                                }
                            }

                        }

                        error != null -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = error!!)
                                }
                            }

                        }

                        else -> {

                            items(posts) { post ->
                                RedditPostItem(post = post,currentSettings)
                            }

                        }
                    }


                }
            }
        }
    }
}


//    @Preview(showBackground = true)
//    @Composable
//    fun MainScreenPreview() {
//        MainScreen()
//
//    }
//}
