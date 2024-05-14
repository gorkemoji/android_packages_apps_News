package com.gorkemoji.news.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gorkemoji.news.data.Article
import com.gorkemoji.news.databinding.NewsLayoutBinding
import com.gorkemoji.news.ui.DetailsActivity

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private var articles: List<Article> = listOf()
    private var onItemClickListener: OnItemClickListener? = null

    fun setData(articles: List<Article>) {
        this.articles = articles
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NewsLayoutBinding.inflate(inflater, parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = articles[position]
        holder.bind(newsItem)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("article", newsItem)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = articles.size

    inner class NewsViewHolder(private val binding: NewsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.title.text = article.title
            binding.desc.text = article.desc
            binding.date.text = article.date
            binding.source.text = article.source.sourceName

            Glide.with(binding.root.context)
                .load(article.urlOfImage)
                .into(binding.imageView)
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(newsItem: Article)
    }
}
