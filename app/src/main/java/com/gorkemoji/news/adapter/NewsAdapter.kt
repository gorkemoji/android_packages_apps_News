package com.gorkemoji.news.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gorkemoji.news.data.Articles
import com.gorkemoji.news.data.Source
import com.gorkemoji.news.databinding.NewsLayoutBinding

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private var articles: List<Articles> = listOf()
   // private var sources: List<Source> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(articles: List<Articles>) {
        this.articles = articles
        //this.sources = sources
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NewsLayoutBinding.inflate(inflater, parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
      //  val source = sources[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int = articles.size

    inner class NewsViewHolder(private val binding: NewsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Articles) {
            binding.title.text = article.title
            binding.desc.text = article.desc
            binding.date.text = article.date
            binding.source.text = article.source.sourceName

            Glide.with(binding.root.context)
                .load(article.urlOfImage)
                .into(binding.imageView)
        }
    }
}
