package com.example.nodrah_project
import retrofit2.http.GET
import retrofit2.http.Url

interface RedditApi {
    @GET
    suspend fun getPosts(@Url url: String): RedditResponse
}