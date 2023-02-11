package com.rofiqoff.news.data.api.repository

import com.rofiqoff.news.base.NewsResponse
import com.rofiqoff.news.data.api.model.Article
import com.rofiqoff.news.data.implementation.remote.request.NewsQuery

interface DataRepository {

    suspend fun fetchTopHeadline(request: NewsQuery): NewsResponse<List<Article>>

}
