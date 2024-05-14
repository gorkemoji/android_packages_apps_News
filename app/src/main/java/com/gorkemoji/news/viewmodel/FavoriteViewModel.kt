package com.gorkemoji.news.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gorkemoji.news.data.FavoriteNews
import com.gorkemoji.news.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteViewModel(private val database: AppDatabase) : ViewModel() {
    val favoriteNewsLiveData: LiveData<List<FavoriteNews>> = database.favoriteNewsDao().getAll()

    suspend fun deleteFavoriteNews(favoriteNews: FavoriteNews) {
        withContext(Dispatchers.IO) {
            database.favoriteNewsDao().delete(favoriteNews)
        }
    }
}

class FavoriteViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(AppDatabase.getDatabase(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
