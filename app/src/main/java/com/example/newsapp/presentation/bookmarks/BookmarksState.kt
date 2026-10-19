package com.example.newsapp.presentation.bookmarks

import com.example.newsapp.domain.model.Article

data class BookmarksState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false
)