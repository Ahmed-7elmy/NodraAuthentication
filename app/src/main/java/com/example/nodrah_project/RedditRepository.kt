package com.example.nodrah_project
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.URLDecoder

class RedditRepository {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.reddit.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val redditApi: RedditApi = retrofit.create(RedditApi::class.java)

    private val subredditUrls = listOf(
        "https://www.reddit.com/r/disability/.json",
        "https://www.reddit.com/r/disabled/.json",
        "https://www.reddit.com/r/deaf/.json",
        "https://www.reddit.com/r/LoveIsBlindOnNetflix/.json",
        "https://www.reddit.com/r/TwoXChromosomes/.json",
        "https://www.reddit.com/r/ChronicIllness/.json",
        "https://www.reddit.com/r/car/.json"
    )

    suspend fun fetchAndProcessPosts(): List<RedditPost> = withContext(Dispatchers.IO) {
        val responses = subredditUrls.map { url ->
            async {
                try {
                    val posts = redditApi.getPosts(url).data.children.mapNotNull { it.data }
                    posts.forEach { post ->
                        Log.d("RedditRepository", "Raw Image URL: ${post.preview?.images?.firstOrNull()?.source?.url}")
                        Log.d("RedditRepository", "Raw Thumbnail URL: ${post.thumbnail}")
                        Log.d("RedditRepository", "Raw Video URL: ${post.media?.redditVideo?.fallbackUrl}")
                    }
                    posts
                } catch (e: Exception) {
                    e.printStackTrace()
                    emptyList()
                }
            }
        }.awaitAll().flatten()

        responses.map { postData ->
            RedditPost(
                title = postData.title.orEmpty(),
                author = postData.author.orEmpty(),
                subreddit = postData.subreddit.orEmpty(),
                selftext = postData.selftext,
                imageUrl = postData.preview?.images?.firstOrNull()?.source?.url?.safeUrlProcess() ?: postData.thumbnail?.takeIf { it.startsWith("http") }?.safeUrlProcess(),
                videoUrl = postData.media?.redditVideo?.fallbackUrl?.safeUrlProcess()
            )
        }.shuffled()
    }

    fun filterVideoPosts(posts: List<RedditPost>): List<RedditPost> {
        return posts.filter { !it.videoUrl.isNullOrEmpty() }
    }

    private fun String.safeUrlProcess(): String {
        Log.d("RedditRepository", "URL before processing: $this")
        return try {
            val htmlDecoded = this.replace("&amp;", "&")
            val temp1 = htmlDecoded.replace("%", "<percentage>").replace("+", "<plus>")
            val decoded = URLDecoder.decode(temp1, "UTF-8")
            val processedUrl = decoded.replace("<percentage>", "%").replace("<plus>", "+")
            Log.d("RedditRepository", "URL after processing: $processedUrl")
            processedUrl
        } catch (e: Exception) {
            e.printStackTrace()
            this
        }
    }
}