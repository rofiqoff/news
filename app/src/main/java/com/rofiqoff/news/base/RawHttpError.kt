package com.rofiqoff.news.base

import com.google.gson.annotations.SerializedName

internal data class RawHttpError(
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("code")
    val code: Boolean?,
    @SerializedName("message")
    val message: String?
)
