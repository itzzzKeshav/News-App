package com.example.newsapp.presentation.news_feed

import com.example.newsapp.domain.model.Article

sealed interface NewsFeedEvent {
    data object Refresh : NewsFeedEvent
    data class OnBookmarkClick(val article: Article) : NewsFeedEvent
}