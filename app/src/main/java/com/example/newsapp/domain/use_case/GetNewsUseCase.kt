package com.example.newsapp.domain.use_case

import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.flow.Flow

class GetNewsUseCase(
    private val repository: NewsRepository
) {
    operator fun invoke(forceFetchFromRemote: Boolean = false): Flow<Resource<List<Article>>> {
        return repository.getTopHeadlines(forceFetchFromRemote)
    }
}