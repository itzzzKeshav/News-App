package com.example.newsapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Feed : Screen("feed", "News", Icons.Filled.Home)
    data object Bookmarks : Screen("bookmarks", "Bookmarks", Icons.Filled.Favorite)
}