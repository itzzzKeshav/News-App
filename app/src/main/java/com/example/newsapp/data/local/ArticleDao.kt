package com.example.newsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.newsapp.data.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {


    // OnConflictStrategy.REPLACE: If article exists, update its content with fresh data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertArticles(articles: List<ArticleEntity>)

    // Returns a reactive Cold Flow. Room auto-emits new lists whenever the table changes!
    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun getArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE isBookmarked = 1")
    fun getBookmarkedArticles(): Flow<List<ArticleEntity>>

    @Query("UPDATE articles SET isBookmarked = :isBookmarked WHERE url = :url")
    suspend fun updateBookmarkStatus(url: String, isBookmarked: Boolean)

    @Query("DELETE FROM articles WHERE isBookmarked = 0")
    suspend fun clearNonBookmarkedArticles()
}