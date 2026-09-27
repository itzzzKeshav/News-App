package com.example.newsapp.data.remote

import com.example.newsapp.data.remote.dto.NewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String
    ): NewsResponseDto

    companion object {
        const val BASE_URL = "https://newsapi.org/"
    }
}