package com.gorkemoji.news.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite_news")

data class FavoriteNews(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val article: Article,
    val isFavorite: Boolean = true
) : Serializable
