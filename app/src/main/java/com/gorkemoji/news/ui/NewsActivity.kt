package com.gorkemoji.news.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.gorkemoji.news.R
import com.gorkemoji.news.adapter.NewsAdapter
import com.gorkemoji.news.data.Article
import com.gorkemoji.news.databinding.ActivityNewsBinding
import com.gorkemoji.news.network.NewsRepository
import com.gorkemoji.news.network.RetrofitClient
import com.gorkemoji.news.viewmodel.NewsViewModel

class NewsActivity : AppCompatActivity(), NewsAdapter.OnItemClickListener {

    private lateinit var binding: ActivityNewsBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = NewsRepository(RetrofitClient.newsApiService)
        viewModel = NewsViewModel(repository)

        val apiKey = loadMode("api_key", "keys")

        if (apiKey.isNullOrEmpty()) {
            binding.srchBtn.isClickable = false
            binding.srchBtn.isEnabled = false
        }
        else viewModel.fetchTodayNews(apiKey)

        binding.bottomNavigationView.selectedItemId = R.id.home

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.favorites -> {
                    startActivity(Intent(applicationContext, FavoriteActivity::class.java))
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(applicationContext, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        adapter = NewsAdapter()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@NewsActivity)
            adapter = this@NewsActivity.adapter
            itemAnimator = DefaultItemAnimator()
        }

        binding.srchBtn.setOnClickListener {
            val query = binding.searchHint.text.toString()
            if (query.isNotBlank() && !apiKey.isNullOrEmpty()) {
                viewModel.findSearchedNews(query, 1, apiKey)
            }
        }

        viewModel.news.observe(this) { newsResponse ->
            adapter.setData(newsResponse.articles)
        }

        viewModel.error.observe(this) {
            Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
        }
    }

    override fun onItemClick(newsItem: Article) {
        val intent = Intent(this, DetailsActivity::class.java)
        startActivity(intent)
    }

    private fun loadMode(type: String, file: String): String? {
        val pref : SharedPreferences = applicationContext.getSharedPreferences(file, Context.MODE_PRIVATE)

        return pref.getString(type, "")
    }
}