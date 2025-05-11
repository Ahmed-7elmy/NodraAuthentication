package com.example.nodrah_project

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.request.CachePolicy
import com.example.nodrah_project.screens.EmailVerificationScreen
import com.example.nodrah_project.ui.theme.AppTheme
import com.example.nodrah_project.ui.theme.DyslexicFont
import com.example.nodrah_project.ui.theme.LocalAppColorScheme

@Composable
fun RedditPostItem(post: RedditPost, currentSettings: AccessibilitySettings) {

    AppTheme(
        isContrastTheme = currentSettings.isContrast,
        isMonoChrome = currentSettings.isMonochrome
    ) {
        val colors = LocalAppColorScheme.current
        val context = LocalContext.current
        val currentFont = if (currentSettings.useDyslexicFont) DyslexicFont else FontFamily.Default

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.background)
                .padding(vertical = 10.dp)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.background)
                    .padding(0.dp)


            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .background(color = colors.background)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // User Icon
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_account_circle_24),
                            contentDescription = "User Avatar",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(20.dp))

                        // Author
                        Text(
                            modifier = Modifier.weight(1f), // Take remaining space
                            text = post.author,
                            color = if (currentSettings.isContrast) colors.onBackground else colors.onPrimary,
                            fontFamily = currentFont,
                            fontSize = currentSettings.fontSize.sp,

                            )

                        // More Options
                        Text(
                            text = "...",color = if (currentSettings.isContrast) colors.onBackground else colors.onPrimary,
                             fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Title
                    Text(
                        text = post.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = if (currentSettings.isContrast) colors.onBackground else colors.onPrimary,
                        fontFamily = currentFont,
                        fontSize = currentSettings.fontSize.sp,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Selftext
                    post.selftext?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (currentSettings.isContrast) colors.onBackground else colors.onPrimary,
                            fontFamily = currentFont,
                            fontSize = currentSettings.fontSize.sp,
                        )
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
                            .padding(all = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.like),
                                contentDescription = "Like Button",
                                tint = if (currentSettings.isContrast) colors.onBackground else colors.onPrimary,
                                modifier = Modifier.size(16.dp)

                            )
                            Spacer(modifier = Modifier.width(3.dp))

                            Text(
                                text = "Like",
                                fontSize = currentSettings.fontSize.sp,
                                color = if (currentSettings.isContrast) colors.onBackground else colors.onPrimary,
                                fontFamily = currentFont,
                                modifier = Modifier.clickable { })
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.comment),
                                contentDescription = "Comment Button",
                                tint = if (currentSettings.isContrast) colors.onBackground else colors.onPrimary,                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))

                            Text(
                                text = "Comment",
                                fontSize = currentSettings.fontSize.sp,
                                fontFamily = currentFont,
                                color = if (currentSettings.isContrast) colors.onBackground else colors.onPrimary,
                                modifier = Modifier.clickable { })
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.share),
                                contentDescription = "Share Button",
                                tint = if (currentSettings.isContrast) colors.onBackground else colors.onPrimary,                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))

                            Text(
                                text = "Share",
                                fontSize = currentSettings.fontSize.sp,
                                fontFamily = currentFont,
                                color = if (currentSettings.isContrast) colors.onBackground else colors.onPrimary,
                                modifier = Modifier.clickable { })
                        }
                    }
                }
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
                ImageRequest.Builder(context).data(url).crossfade(true)
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
            onError = { error = true; loading = false })

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

    AndroidView(factory = {
        PlayerView(context).apply {
            player = exoPlayer
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 400)
            useController = true
        }
    }, update = { view ->
        view.player = exoPlayer
    })
}



