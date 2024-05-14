package com.gorkemoji.news.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gorkemoji.news.data.Article
import com.gorkemoji.news.data.FavoriteNews
import com.gorkemoji.news.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsViewModel(private val database: AppDatabase) : ViewModel() {
    suspend fun checkIfExists(article: Article): Boolean {
        return withContext(Dispatchers.IO) {
            database.favoriteNewsDao().getByArticle(article) != null
        }
    }

    suspend fun addArticleToFavorites(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            val news = FavoriteNews(article = article, isFavorite = true)
            database.favoriteNewsDao().insert(news)
        }
    }
}

