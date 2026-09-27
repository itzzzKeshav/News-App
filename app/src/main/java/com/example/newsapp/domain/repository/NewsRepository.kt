package com.example.newsapp.domain.repository

import com.example.newsapp.domain.model.Article
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getTopHeadlines(forceFetchFromRemote: Boolean): Flow<Resource<List<Article>>>
    fun getBookmarkedArticles(): Flow<List<Article>>
    suspend fun toggleBookmark(url: String, isBookmarked: Boolean)
}