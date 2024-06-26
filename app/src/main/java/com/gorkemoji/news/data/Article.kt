package com.gorkemoji.news.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Article(
    @SerializedName("author")
    val author: String?,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val desc: String?,

    @SerializedName("url")
    val url: String,

    @SerializedName("urlToImage")
    val urlOfImage: String?,

    @SerializedName("publishedAt")
    val date: String,

    @SerializedName("content")
    val content: String?,

    @SerializedName("source")
    val source: Source
) : Serializable
