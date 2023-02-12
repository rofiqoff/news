package com.rofiqoff.news.data.implementation.remote.api

import com.rofiqoff.news.data.implementation.remote.response.ApiResponse
import com.rofiqoff.news.data.implementation.remote.response.ArticleResponse
import com.rofiqoff.news.data.implementation.remote.response.SourceResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface DataApi {

    @GET("top-headlines")
    suspend fun fetchArticles(
        @QueryMap query: Map<String, String>,
    ): ApiResponse<ArticleResponse>

    @GET("top-headlines/sources")
    suspend fun fetchSources(
        @QueryMap query: Map<String, String>,
    ): ApiResponse<SourceResponse>

    @GET("everything")
    suspend fun fetchArticlesSearch(
        @QueryMap query: Map<String, String>,
    ): ApiResponse<ArticleResponse>

}
