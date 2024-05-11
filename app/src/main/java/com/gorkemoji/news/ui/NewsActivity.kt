package com.gorkemoji.news.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.gorkemoji.news.R
import com.gorkemoji.news.adapter.NewsAdapter
import com.gorkemoji.news.databinding.ActivityNewsBinding
import com.gorkemoji.news.network.NewsRepository
import com.gorkemoji.news.network.RetrofitClient
import com.gorkemoji.news.viewmodel.NewsViewModel

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = NewsRepository(RetrofitClient.newsApiService)
        viewModel = NewsViewModel(repository)

        adapter = NewsAdapter()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@NewsActivity)
            adapter = this@NewsActivity.adapter
            itemAnimator = DefaultItemAnimator()
        }

        binding.srchBtn.setOnClickListener {
            val query = binding.searchHint.text.toString()
            if (query.isNotBlank()) {
                viewModel.getNews(query, 1, "872cbc1ccebf43f883df12f1d231c610")
            }
        }

        viewModel.news.observe(this) { newsResponse ->
            adapter.setData(newsResponse.articles)
        }

        viewModel.error.observe(this) {
            Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadMode(type: String, file: String): String? {
        val pref : SharedPreferences = applicationContext.getSharedPreferences(file, Context.MODE_PRIVATE)

        return pref.getString(type, "")
    }
}