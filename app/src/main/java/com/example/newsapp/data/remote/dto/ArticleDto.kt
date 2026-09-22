package com.example.newsapp.data.remote.dto

data class NewsResponseDto(
    val status: String,
    val articles: List<ArticleDto>
)

data class ArticleDto(
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)
