package com.rofiqoff.news.data.implementation.repository

import com.rofiqoff.news.base.NewsResponse
import com.rofiqoff.news.base.toError
import com.rofiqoff.news.data.api.model.Article
import com.rofiqoff.news.data.api.model.Category
import com.rofiqoff.news.data.api.model.Source
import com.rofiqoff.news.data.api.repository.DataRepository
import com.rofiqoff.news.data.implementation.mapper.toArticle
import com.rofiqoff.news.data.implementation.mapper.toSource
import com.rofiqoff.news.data.implementation.remote.api.DataApi
import com.rofiqoff.news.data.implementation.remote.request.NewsQuery
import com.rofiqoff.news.utils.orNull
import com.rofiqoff.news.utils.orNullListNot
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DataRepositoryImpl(
    private val api: DataApi,
    private val ioDispatcher: CoroutineDispatcher,
) : DataRepository {

    override suspend fun fetchArticles(request: NewsQuery): NewsResponse<List<Article>> {
        return withContext(context = ioDispatcher) {
            try {
                val response = api.fetchArticles(request.toMap())

                when (response.status == "ok") {
                    true -> NewsResponse.Success(response.articles.orNullListNot {
                        it.toArticle(response.totalResults.orNull())
                    })
                    else -> NewsResponse.Error(response.message.orEmpty())
                }

            } catch (e: Exception) {
                e.toError()
            }
        }
    }

    override suspend fun fetchArticlesSearch(request: NewsQuery): NewsResponse<List<Article>> {
        return withContext(context = ioDispatcher) {
            try {
                val response = api.fetchArticlesSearch(request.toMap())

                when (response.status == "ok") {
                    true -> NewsResponse.Success(response.articles.orNullListNot {
                        it.toArticle(response.totalResults.orNull())
                    })
                    else -> NewsResponse.Error(response.message.orEmpty())
                }

            } catch (e: Exception) {
                e.toError()
            }
        }
    }

    override suspend fun fetchSources(request: NewsQuery): NewsResponse<List<Source>> {
        return withContext(context = ioDispatcher) {
            try {
                val response = api.fetchSources(request.toMap())

                when (response.status == "ok") {
                    true -> NewsResponse.Success(response.sources.orNullListNot { it.toSource() })
                    else -> NewsResponse.Error(response.message.orEmpty())
                }

            } catch (e: Exception) {
                e.toError()
            }
        }
    }

    override fun generateCategory() = arrayListOf<Category>().apply {
        add(Category(name = "Bisnis", value = "business"))
        add(Category(name = "Hiburan", value = "entertainment"))
        add(Category(name = "Umum", value = "general"))
        add(Category(name = "Kesehatan", value = "health"))
        add(Category(name = "Pengetahuan", value = "science"))
        add(Category(name = "Olahraga", value = "sports"))
        add(Category(name = "Teknologi", value = "technology"))
    }.toList()

}
