package com.rofiqoff.news.data.implementation.remote.request

data class NewsQuery(
    val query: String? = "",
    val country: String? = "id",
    val category: String? = "",
    val sources: String? = "",
    val page: String? = "1",
    val pageSize: String? = "10",
) {
    fun toMap() = mapOf(
        "q" to query.orEmpty(),
        "country" to country.orEmpty(),
        "category" to category.orEmpty(),
        "sources" to sources.orEmpty(),
        "page" to page.orEmpty(),
        "pageSize" to pageSize.orEmpty(),
    ).filterValues { it.isNotEmpty() && it.isNotBlank() }
}
