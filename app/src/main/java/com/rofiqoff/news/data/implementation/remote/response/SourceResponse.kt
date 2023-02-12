package com.rofiqoff.news.data.implementation.remote.response

import com.google.gson.annotations.SerializedName

data class SourceResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("category")
    val category: String?,
    @SerializedName("description")
    val description: String?,
)
