package com.gorkemoji.news.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gorkemoji.news.data.Article
import com.gorkemoji.news.data.News
import com.gorkemoji.news.network.NewsRepository
import com.gorkemoji.news.time.DateConverter
import java.text.SimpleDateFormat
import java.util.*

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {
    private val _news = MutableLiveData<News>()
    val news: LiveData<News>
        get() = _news

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun findSearchedNews(query: String, page: Int, apiKey: String) {
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

    fun fetchTodayNews(apiKey: String) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val yesterdayDate = getYesterdayDateString()

        repository.fetchTodayNews("us", apiKey, yesterdayDate, currentDate) { newsResponse ->
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

    private fun getYesterdayDateString(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }

    private fun sortArticlesByPublishedDate(articles: List<Article>): List<Article> {
        return articles.sortedByDescending { it.date }
    }

    private fun formatArticlesDates(articles: List<Article>): List<Article> {
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