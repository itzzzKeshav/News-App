package com.example.newsapp.presentation.news_feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.use_case.GetNewsUseCase
import com.example.newsapp.domain.use_case.ToggleBookmarkUseCase
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsFeedViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NewsFeedState())
    val state = _state.asStateFlow()

    init {
        fetchNews(forceFetchFromRemote = false)
    }

    fun onEvent(event: NewsFeedEvent) {
        when (event) {
            is NewsFeedEvent.Refresh -> {
                fetchNews(forceFetchFromRemote = true)
            }
            is NewsFeedEvent.OnBookmarkClick -> {
                toggleBookmark(event.article)
            }
        }
    }

    private fun fetchNews(forceFetchFromRemote: Boolean) {
        getNewsUseCase(forceFetchFromRemote)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                articles = result.data.orEmpty(),
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                articles = result.data.orEmpty(),
                                isLoading = false,
                                errorMessage = result.message
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                articles = result.data ?: it.articles,
                                isLoading = result.isLoading
                            )
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun toggleBookmark(article: Article) {
        viewModelScope.launch {
            toggleBookmarkUseCase(
                url = article.url,
                isBookmarked = !article.isBookmarked
            )
        }
    }
}