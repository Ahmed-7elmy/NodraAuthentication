package com.example.nodrah_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nodrah_project.ui.theme.Nodrah_projectTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Nodrah_projectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))

                }
            }
        }
    }

    @Composable
    fun HeaderSection() {
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
                    fontSize = 30.sp,
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
                    .background(Color(0xFFF0F0F0))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.images),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Start your Journey...",
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.AccountBox, contentDescription = "Profile Icon")
            }
        }
    }

    @Composable
    fun BottomNavigationBar(selectedItem: Int, onItemSelected: (Int) -> Unit) {
        val context = LocalContext.current // 👈 الحصول على الـ Context

        val items = listOf(
            BottomNavItem("Home", Icons.Default.Home),
            BottomNavItem("Video", Icons.Default.PlayArrow),
            BottomNavItem("Accessibility", Icons.Rounded.Accessibility),
            BottomNavItem("Notification", Icons.Default.Notifications),
            BottomNavItem("Profile", Icons.Default.Person)
        )

        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 4.dp
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    label = {
                        Text(text = item.title, fontSize = 10.sp)
                    },
                    selected = selectedItem == index,
                    onClick = {
                        onItemSelected(index)

                        // إذا كان الزر هو "Video"، ننتقل إلى VideoActivity
                        if (index == 1) { // "Video" هو التبويب رقم 1 في الـ BottomNavBar
                            val intent = Intent(context, VideosActivity::class.java)
                            context.startActivity(intent)
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1A2C50),
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }

    @Composable
    fun StoriesSection() {
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
                    StoryImageCard(imageRes = item)
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
                modifier = Modifier.size(32.dp)
            )
        }
    }

    @Composable
    fun StoryImageCard(@DrawableRes imageRes: Int) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Story Image",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
    }


    @Composable
    fun MainScreen(modifier: Modifier = Modifier) {
        var selectedTab by remember { mutableStateOf(0) }
        val viewModel: RedditViewModel = viewModel()
        val posts by viewModel.posts.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()
        val error by viewModel.error.collectAsState()
        Scaffold(
            bottomBar = {
                BottomNavigationBar(selectedItem = selectedTab) {
                    selectedTab = it
                }
            }
        ) { innerPadding ->
            LazyColumn (
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                // عرض باقي المحتويات هنا
                item {
                    HeaderSection()
                }
                item {
                    StoriesSection()
                }
                    when {
                        isLoading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }

                        }

                        error != null -> {
                            item{
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
                                    RedditPostItem(post = post)
                                }

                        }
                    }



            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun MainScreenPreview() {
        MaterialTheme {
            MainScreen()
        }
    }
}
