package com.gorkemoji.news.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gorkemoji.news.data.Articles
import com.gorkemoji.news.data.News
import com.gorkemoji.news.data.Source
import com.gorkemoji.news.network.NewsRepository
import com.gorkemoji.news.time.DateConverter

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
                val sortedArticles = sortArticlesByPublishedDate(newsResponse.articles)
                val formattedArticles = formatArticlesDates(sortedArticles)
                val sortedNewsResponse = newsResponse.copy(articles = formattedArticles)
                postNewsResponse(sortedNewsResponse)
            } else {
                handleFetchError()
            }
        }
    }

    private fun sortArticlesByPublishedDate(articles: List<Articles>): List<Articles> {
        return articles.sortedByDescending { it.date }
    }

    private fun formatArticlesDates(articles: List<Articles>): List<Articles> {
        return articles.map { article ->
            article.copy(date = DateConverter.convertDate(article.date))
        }
    }

    private fun postNewsResponse(newsResponse: News) {
        _news.postValue(newsResponse)
    }

    private fun handleFetchError() {
        _error.postValue("Failed to fetch news")
    }
}