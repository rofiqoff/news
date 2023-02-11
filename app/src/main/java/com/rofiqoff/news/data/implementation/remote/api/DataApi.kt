package com.rofiqoff.news.data.implementation.remote.api

import com.rofiqoff.news.data.implementation.remote.response.ApiResponse
import com.rofiqoff.news.data.implementation.remote.response.ArticleResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface DataApi {

    @GET("top-headlines")
    suspend fun fetchTopHeadline(
        @QueryMap query: Map<String, String>,
    ): ApiResponse<ArticleResponse>

}
