package com.gorkemoji.news.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gorkemoji.news.data.News
import com.gorkemoji.news.data.Source
import com.gorkemoji.news.network.NewsRepository

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _news = MutableLiveData<News>()
    val news: LiveData<News>
        get() = _news

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun getNews(query: String, page: Int, apiKey: String) {
        repository.searchNews(query, page, apiKey) { newsResponse ->
            if (newsResponse != null) {
                _news.postValue(newsResponse)
            } else {
                _error.postValue("Fail.")
            }
        }
    }
}
