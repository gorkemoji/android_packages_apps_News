package com.gorkemoji.news.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.gorkemoji.news.data.Article
import com.gorkemoji.news.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val article = intent.getSerializableExtra("article") as Article

        Glide.with(this)
            .load(article.urlOfImage)
            .into(binding.image)

        binding.title.text = article.title
        binding.srcName.text = article.source.sourceName
        binding.date.text = article.date
        binding.content.text = article.content

    }
}