package com.example.nodrah_project

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun VideosScreen(viewModel: RedditViewModel = viewModel(),
                 currentSettings: AccessibilitySettings) {
    val videoPosts by viewModel.videoPosts.collectAsState()
    val isVideoLoading by viewModel.isVideoLoading.collectAsState()
    val videoError by viewModel.videoError.collectAsState()

    Scaffold(
        topBar = {}
    ) { paddingValues ->
        if (isVideoLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (videoError != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = videoError!!)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(videoPosts) { post ->
                    RedditPostItem(post = post, currentSettings) // هنستخدم نفس الـ Item Composable
                }
            }
        }
    }
}
