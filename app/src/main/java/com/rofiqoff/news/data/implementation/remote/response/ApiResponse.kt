package com.rofiqoff.news.data.implementation.remote.response

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("totalResults")
    val totalResults: Int?,
    @SerializedName("articles")
    val articles: List<T>?,
    @SerializedName("sources")
    val sources: List<T>?,
)
