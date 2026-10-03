package com.example.newsapp.presentation.news_feed

import com.example.newsapp.domain.model.Article

data class NewsFeedState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)