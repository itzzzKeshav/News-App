package com.example.newsapp.data.mapper

import com.example.newsapp.data.local.entity.ArticleEntity
import com.example.newsapp.data.remote.dto.ArticleDto
import com.example.newsapp.domain.model.Article

// 1. Remote DTO -> Database Entity
fun ArticleDto.toEntity(): ArticleEntity {
    return ArticleEntity(
        url = url.orEmpty(),
        title = title.orEmpty(),
        description = description ?: "No description",
        imageUrl = urlToImage.orEmpty(),
        publishedAt = publishedAt.orEmpty(),
        isBookmarked = false
    )
}

// 2. Database Entity -> Domain Model (Clean for UI)
fun ArticleEntity.toDomain(): Article {
    return Article(
        url = url,
        title = title,
        description = description,
        imageUrl = imageUrl,
        publishedAt = publishedAt,
        isBookmarked = isBookmarked
    )
}