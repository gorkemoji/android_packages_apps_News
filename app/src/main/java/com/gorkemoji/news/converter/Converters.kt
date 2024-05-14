package com.gorkemoji.news.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.gorkemoji.news.data.Article

class Converters {
    @TypeConverter
    fun fromArticle(article: Article): String {
        return Gson().toJson(article)
    }

    @TypeConverter
    fun toArticle(jsonString: String): Article {
        return Gson().fromJson(jsonString, Article::class.java)
    }
}