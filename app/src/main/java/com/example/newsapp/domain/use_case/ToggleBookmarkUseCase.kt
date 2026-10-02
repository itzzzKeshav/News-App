package com.example.newsapp.domain.use_case

import com.example.newsapp.domain.repository.NewsRepository

class ToggleBookmarkUseCase(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(url: String, isBookmarked: Boolean) {
        repository.toggleBookmark(url, isBookmarked)
    }
}