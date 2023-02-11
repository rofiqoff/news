package com.rofiqoff.news.data.implementation.repository

import com.rofiqoff.news.base.NewsResponse
import com.rofiqoff.news.base.toError
import com.rofiqoff.news.data.api.model.Article
import com.rofiqoff.news.data.api.repository.DataRepository
import com.rofiqoff.news.data.implementation.mapper.toArticle
import com.rofiqoff.news.data.implementation.remote.api.DataApi
import com.rofiqoff.news.data.implementation.remote.request.NewsQuery
import com.rofiqoff.news.utils.orNullListNot
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DataRepositoryImpl(
    private val api: DataApi,
    private val ioDispatcher: CoroutineDispatcher,
) : DataRepository {

    override suspend fun fetchTopHeadline(request: NewsQuery): NewsResponse<List<Article>> {
        return withContext(context = ioDispatcher) {
            try {
                val response = api.fetchTopHeadline(request.toMap())

                when (response.status == "ok") {
                    true -> NewsResponse.Success(response.articles.orNullListNot { it.toArticle() })
                    else -> NewsResponse.Error("")
                }

            } catch (e: Exception) {
                e.toError()
            }
        }
    }

}
