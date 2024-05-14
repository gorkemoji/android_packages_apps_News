package com.gorkemoji.news.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.gorkemoji.news.R
import com.gorkemoji.news.adapter.FavoriteAdapter
import com.gorkemoji.news.database.AppDatabase
import com.gorkemoji.news.databinding.ActivityFavoriteBinding
import androidx.lifecycle.Observer

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.selectedItemId = R.id.favorites

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(applicationContext, NewsActivity::class.java))
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(applicationContext, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        val favoriteNewsLiveData = AppDatabase.getDatabase(this)
            .favoriteNewsDao().getAll()

        favoriteNewsLiveData.observe(this, Observer { favoriteNewsList ->
            adapter = FavoriteAdapter(favoriteNewsList.reversed()) // Reverse the list
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@FavoriteActivity)
                adapter = this@FavoriteActivity.adapter
                itemAnimator = DefaultItemAnimator()
            }
        })
    }
}
