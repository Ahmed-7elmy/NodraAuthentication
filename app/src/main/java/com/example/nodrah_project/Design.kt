package com.example.nodrah_project

import android.annotation.SuppressLint
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.request.CachePolicy

@Composable
fun RedditPostItem(post: RedditPost) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp)
                .background(color = Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User Icon
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_account_circle_24),
                    contentDescription = "User Avatar",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))

                // Author
                Text(
                    text = post.author,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f) // Take remaining space
                )

                // More Options
                Text(
                    text = "...",
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Title
            Text(text = post.title, style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            // Selftext
            post.selftext?.let {
                Text(text = it, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Image
            post.imageUrl?.let { url ->
                SubredditImage(url = url)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Video
            post.videoUrl?.let { url ->
                SubredditVideo(url = url)
                Spacer(modifier = Modifier.height(8.dp))
            }
            // Like, Comment, Share Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Like",
                    modifier = Modifier.clickable {  }
                )
                Text(
                    text = "Comment",
                    modifier = Modifier.clickable { }
                )
                Text(
                    text = "Share",
                    modifier = Modifier.clickable {  }
                )
            }
        }
    }
}

@Composable
fun SubredditImage(url: String) {
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = remember(url) { // Use remember with url
                ImageRequest.Builder(context)
                    .data(url)
                    .crossfade(true)
                    .memoryCachePolicy(CachePolicy.DISABLED) // Disable memory cache
                    .diskCachePolicy(CachePolicy.DISABLED)   // Disable disk cache
                    .build()
            },
            contentDescription = "Reddit Image",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp),
            onLoading = { loading = true },
            onSuccess = { loading = false },
            onError = { error = true; loading = false }
        )

        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        if (error) {
            Text("Failed to load image", modifier = Modifier.align(Alignment.Center))
        }
    }
}

@SuppressLint("OpaqueUnit")
@Composable
fun SubredditVideo(url: String) {
    val context = LocalContext.current
    val exoPlayer = remember(url) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            playWhenReady = false
            prepare()
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }

    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                layoutParams = android.view.ViewGroup.LayoutParams(MATCH_PARENT, 400)
                useController = true
            }
        },
        update = { view ->
            view.player = exoPlayer
        }
    )
}