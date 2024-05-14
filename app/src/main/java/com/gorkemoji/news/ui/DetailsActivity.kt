package com.gorkemoji.news.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.gorkemoji.news.R
import com.gorkemoji.news.data.Article
import com.gorkemoji.news.data.FavoriteNews
import com.gorkemoji.news.database.AppDatabase
import com.gorkemoji.news.databinding.ActivityDetailsBinding
import kotlinx.coroutines.*

class DetailsActivity : AppCompatActivity() {

    private val database by lazy {
        AppDatabase.getDatabase(this)
    }

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

        binding.sourceBtn.setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("article", article)
            startActivity(intent)
        }

        binding.backBtn.setOnClickListener { onBackPressed() }

        binding.linkBtn.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, article.url)
            startActivity(Intent.createChooser(shareIntent, resources.getString(R.string.share_via)))
        }

        binding.favoriteBtn.setOnClickListener {
            addOrRemoveArticleFromFavorites(article)
        }

    }

    private fun addOrRemoveArticleFromFavorites(article: Article) {
        GlobalScope.launch(Dispatchers.Main) {
            val isAlreadyAdded = withContext(Dispatchers.IO) {
                database.favoriteNewsDao().getByArticle(article) != null
            }
            if (isAlreadyAdded) {
                //val news = FavoriteNews(article = article, isFavorite = true)
                //database.favoriteNewsDao().delete(news)
                showToast(getString(R.string.exists))
            } else {
                val news = FavoriteNews(article = article, isFavorite = true)
                database.favoriteNewsDao().insert(news)
                showToast(getString(R.string.added_to_favorites))
            }
            setResult(RESULT_OK)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@DetailsActivity, message, Toast.LENGTH_SHORT).show()
    }

    @Deprecated("Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        super.onBackPressed()
    }
}
