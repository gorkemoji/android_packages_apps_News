package com.gorkemoji.news.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Source(
    @SerializedName("id")
    val sourceId: String,

    @SerializedName("name")
    val sourceName: String,
) : Serializable
