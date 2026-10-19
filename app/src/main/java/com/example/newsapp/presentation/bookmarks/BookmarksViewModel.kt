package com.example.newsapp.presentation.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.use_case.GetBookmarksUseCase
import com.example.newsapp.domain.use_case.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BookmarksState())
    val state = _state.asStateFlow()

    init {
        observeBookmarks()
    }

    private fun observeBookmarks() {
        getBookmarksUseCase()
            .onEach { bookmarkedArticles ->
                _state.update {
                    it.copy(
                        articles = bookmarkedArticles,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun removeBookmark(article: Article) {
        viewModelScope.launch {
            toggleBookmarkUseCase(
                url = article.url,
                isBookmarked = false
            )
        }
    }
}