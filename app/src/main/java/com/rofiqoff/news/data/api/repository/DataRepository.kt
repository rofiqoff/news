package com.rofiqoff.news.data.api.repository

import com.rofiqoff.news.base.NewsResponse
import com.rofiqoff.news.data.api.model.Article
import com.rofiqoff.news.data.api.model.Category
import com.rofiqoff.news.data.api.model.Source
import com.rofiqoff.news.data.implementation.remote.request.NewsQuery

interface DataRepository {

    suspend fun fetchArticles(request: NewsQuery): NewsResponse<List<Article>>

    suspend fun fetchArticlesSearch(request: NewsQuery): NewsResponse<List<Article>>

    suspend fun fetchSources(request: NewsQuery): NewsResponse<List<Source>>

    fun generateCategory(): List<Category>
}
