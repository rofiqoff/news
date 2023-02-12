package com.rofiqoff.news.data.api.model

data class Article(
    val source: Source = Source(),
    val author: String = "",
    val title: String = "",
    val description: String = "",
    val url: String = "",
    val urlToImage: String = "",
    val publishedAt: String = "",
    val content: String = "",
    val totalCount: Int = 0,
    val viewType: ViewType = ViewType.DATA
) {
    enum class ViewType {
        DATA, LOADING
    }
}
