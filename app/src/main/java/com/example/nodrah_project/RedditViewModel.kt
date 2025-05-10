package com.example.nodrah_project
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nodrah_project.ui.theme.AppTheme
import com.example.nodrah_project.ui.theme.LocalAppColorScheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RedditViewModel : ViewModel() {
    private val repository = RedditRepository()
    private val _posts = MutableStateFlow<List<RedditPost>>(emptyList())
    val posts: StateFlow<List<RedditPost>> = _posts
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _videoPosts = MutableStateFlow<List<RedditPost>>(emptyList())
    val videoPosts: StateFlow<List<RedditPost>> = _videoPosts
    private val _isVideoLoading = MutableStateFlow(false)
    val isVideoLoading: StateFlow<Boolean> = _isVideoLoading
    private val _videoError = MutableStateFlow<String?>(null)
    val videoError: StateFlow<String?> = _videoError

    init {
        fetchPosts()
        fetchVideoPosts() // Fetch video posts when ViewModel is created
    }

    private fun fetchPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _posts.value = repository.fetchAndProcessPosts()
            } catch (e: Exception) {
                _error.value = "Failed to load posts: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchVideoPosts() {
        viewModelScope.launch {
            _isVideoLoading.value = true
            _videoError.value = null
            try {
                val allPosts = repository.fetchAndProcessPosts()
                _videoPosts.value = repository.filterVideoPosts(allPosts)
            } catch (e: Exception) {
                _videoError.value = "Failed to load video posts: ${e.localizedMessage}"
            } finally {
                _isVideoLoading.value = false
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun RedditFeedScreen(
    modifier: Modifier = Modifier,
    viewModel: RedditViewModel = viewModel()
) {

    AppTheme {
        val colors = LocalAppColorScheme.current

        val posts by viewModel.posts.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()
        val error by viewModel.error.collectAsState()

        Scaffold(
            modifier = modifier,
            topBar = {

            }
        ) { paddingValues ->
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }

                error != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = error!!)
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        items(posts) { post ->
                            RedditPostItem(post = post)
                        }
                    }
                }
            }
        }
    }
}
