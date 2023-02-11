package com.rofiqoff.news.data.implementation.mapper

import com.rofiqoff.news.data.api.model.Article
import com.rofiqoff.news.data.api.model.Source
import com.rofiqoff.news.data.implementation.remote.response.ArticleResponse
import com.rofiqoff.news.data.implementation.remote.response.SourceResponse

fun SourceResponse?.toSource() = Source(
    id = this?.id.orEmpty(),
    name = this?.name.orEmpty(),
)

fun ArticleResponse.toArticle() = Article(
    source = this.source.toSource(),
    author = this.author.orEmpty(),
    title = this.title.orEmpty(),
    description = this.description.orEmpty(),
    url = this.url.orEmpty(),
    urlToImage = urlToImage.orEmpty(),
    publishedAt = this.publishedAt.orEmpty(),
    content = this.content.orEmpty(),
)
