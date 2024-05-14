package com.gorkemoji.news.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gorkemoji.news.data.Article
import com.gorkemoji.news.data.FavoriteNews
import com.gorkemoji.news.databinding.NewsLayoutBinding
import com.gorkemoji.news.ui.DetailsActivity

class FavoriteAdapter(private val favoriteNewsList: List<FavoriteNews>) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var onItemClickListener: FavoriteAdapter.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NewsLayoutBinding.inflate(inflater, parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoriteNews = favoriteNewsList[position]
        holder.bind(favoriteNews)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("article", favoriteNews.article)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return favoriteNewsList.size
    }

    inner class FavoriteViewHolder(private val binding: NewsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteNews: FavoriteNews) {
            binding.title.text = favoriteNews.article.title
            binding.desc.text = favoriteNews.article.desc
            binding.date.text = favoriteNews.article.date
            binding.source.text = favoriteNews.article.source.sourceName

            Glide.with(binding.root.context)
                .load(favoriteNews.article.urlOfImage)
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
