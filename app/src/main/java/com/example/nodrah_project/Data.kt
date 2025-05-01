package com.example.nodrah_project

//package com.example.redditreader.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RedditResponse(
    val data: Data
)

@JsonClass(generateAdapter = true)
data class Data(
    val children: List<Children>
)

@JsonClass(generateAdapter = true)
data class Children(
    val data: PostData
)

@JsonClass(generateAdapter = true)
data class PostData(
    val title: String?,
    val author: String?,
    val subreddit: String?,
    val selftext: String?,
    val preview: Preview?,
    val media: Media?,
    val thumbnail: String?,
    @Json(name = "url_overridden_by_dest")
    val linkUrl: String?
)

@JsonClass(generateAdapter = true)
data class Preview(
    val images: List<Image>?
)

@JsonClass(generateAdapter = true)
data class Image(
    val source: Source?
)

@JsonClass(generateAdapter = true)
data class Source(
    val url: String?
)

@JsonClass(generateAdapter = true)
data class Media(
    @Json(name = "reddit_video")
    val redditVideo: RedditVideo?
)

@JsonClass(generateAdapter = true)
data class RedditVideo(
    @Json(name = "fallback_url")
    val fallbackUrl: String?
)

data class RedditPost(
    val title: String,
    val author: String,
    val subreddit: String,
    val selftext: String? = null,
    val imageUrl: String? = null,
    val videoUrl: String? = null,
    val linkUrl: String? = null
)
