package com.gorkemoji.news.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gorkemoji.news.data.Article
import com.gorkemoji.news.data.FavoriteNews

@Dao
interface FavoriteNewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(news: FavoriteNews)

    @Delete
    suspend fun delete(news: FavoriteNews)

    @Query("SELECT * FROM favorite_news WHERE article = :article")
    suspend fun getByArticle(article: Article): FavoriteNews?

    @Query("SELECT * FROM favorite_news")
    fun getAll(): LiveData<List<FavoriteNews>>
}