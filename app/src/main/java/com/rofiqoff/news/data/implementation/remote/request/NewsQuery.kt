package com.rofiqoff.news.data.implementation.remote.request

data class NewsQuery(
    val country: String?,
    val page: String?,
    val pageSize: String?,
) {
    fun toMap() = mapOf(
        "country" to country.orEmpty(),
        "page" to page.orEmpty(),
        "pageSize" to page.orEmpty(),
    ).filterValues { it.isNotEmpty() && it.isNotBlank() }
}
