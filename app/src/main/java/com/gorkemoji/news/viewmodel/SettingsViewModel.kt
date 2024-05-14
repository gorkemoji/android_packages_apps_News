package com.gorkemoji.news.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("keys", Context.MODE_PRIVATE)

    fun saveApiKey(apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            sharedPreferences.edit().putString("api_key", apiKey).apply()
        }
    }

    fun loadApiKey(): String? {
        return sharedPreferences.getString("api_key", null)
    }
}
