package com.gorkemoji.news.network

import com.gorkemoji.news.data.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRepository(private val newsApiService: NewsApiService) {
    fun searchNews(query: String, page: Int, apiKey: String, callback: (News?) -> Unit) {
        val call = newsApiService.searchNews(query, page, apiKey)
        call.enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    callback(newsResponse)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                callback(null)
            }
        })
    }
}