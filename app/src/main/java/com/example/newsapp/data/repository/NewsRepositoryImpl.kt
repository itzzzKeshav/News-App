package com.example.newsapp.data.repository

import com.example.newsapp.data.local.ArticleDao
import com.example.newsapp.data.mapper.toDomain
import com.example.newsapp.data.mapper.toEntity
import com.example.newsapp.data.remote.NewsApi
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class NewsRepositoryImpl(
    private val api: NewsApi,
    private val dao: ArticleDao
) : NewsRepository {

    override fun getTopHeadlines(forceFetchFromRemote: Boolean): Flow<Resource<List<Article>>> = flow {
        // 1. Emit Loading state with current local DB cache (if any)
        val localArticles = dao.getArticles().first()
        emit(Resource.Loading(isLoading = true, data = localArticles.map { it.toDomain() }))

        val shouldJustLoadFromCache = localArticles.isNotEmpty() && !forceFetchFromRemote

        if (shouldJustLoadFromCache) {
            emit(Resource.Loading(isLoading = false))
            emit(Resource.Success(data = localArticles.map { it.toDomain() }))
            return@flow
        }

        // 2. Fetch fresh data from Network
        val remoteArticles = try {
            val response = api.getTopHeadlines(
                country = "us",
                apiKey = "09f50b4e950d46bc91033099913fb0a1" // We will inject this cleanly later via BuildConfig or Hilt
            )
            response.articles
        } catch (e: IOException) {
            // No internet connection or socket timeout
            e.printStackTrace()
            emit(Resource.Error(
                message = "Couldn't reach server. Check your internet connection.",
                data = localArticles.map { it.toDomain() }
            ))
            emit(Resource.Loading(isLoading = false))
            return@flow
        } catch (e: HttpException) {
            // 4xx or 5xx HTTP response
            e.printStackTrace()
            emit(Resource.Error(
                message = "Oops, something went wrong: ${e.message()}",
                data = localArticles.map { it.toDomain() }
            ))
            emit(Resource.Loading(isLoading = false))
            return@flow
        }

        // 3. Update Database (Preserving bookmark flags)
        remoteArticles?.let { dtos ->
            // Clear unbookmarked old items so stale news doesn't pile up
            dao.clearNonBookmarkedArticles()

            val entities = dtos.map { it.toEntity() }
            dao.upsertArticles(entities)
        }

        // 4. Emit final updated list from the Single Source of Truth (Room DB)
        val updatedLocalArticles = dao.getArticles().first()
        emit(Resource.Success(data = updatedLocalArticles.map { it.toDomain() }))
        emit(Resource.Loading(isLoading = false))
    }

    override fun getBookmarkedArticles(): Flow<List<Article>> = flow {
        dao.getBookmarkedArticles().collect { entities ->
            emit(entities.map { it.toDomain() })
        }
    }

    override suspend fun toggleBookmark(url: String, isBookmarked: Boolean) {
        dao.updateBookmarkStatus(url = url, isBookmarked = isBookmarked)
    }
}