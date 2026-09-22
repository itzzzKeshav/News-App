package com.example.newsapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey
    val url: String, // Unique identifier for each news article
    val title: String,
    val description: String,
    val imageUrl: String,
    val publishedAt: String,
    val isBookmarked: Boolean = false
)