package com.rofiqoff.news.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rofiqoff.news.base.NewsResponse
import com.rofiqoff.news.data.api.model.Article
import com.rofiqoff.news.data.api.repository.DataRepository
import com.rofiqoff.news.data.implementation.remote.request.NewsQuery
import com.rofiqoff.news.utils.orNull
import com.rofiqoff.news.utils.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: DataRepository,
) : ViewModel() {

    private val _articleLiveData = MutableLiveData<NewsResponse<List<Article>>>()
    val articleLiveData = _articleLiveData.toLiveData()

    private val _querySearch = MutableLiveData("")
    val querySearch = _querySearch.toLiveData()

    fun fetchArticle(page: Int) = viewModelScope.launch {
        _articleLiveData.apply {
            postValue(NewsResponse.Loading)
            postValue(repository.fetchArticlesSearch(NewsQuery(
                country = "",
                query = querySearch.value,
                page = page.toString()
            )))
        }
    }

    fun checkLoadMore(currentItemCount: Int): Boolean {
        val data = (articleLiveData.value as? NewsResponse.Success)?.data

        if (data.isNullOrEmpty().not()) {
            val total = data?.get(0)?.totalCount
            return currentItemCount < total.orNull()
        }

        return false
    }

    fun setQuerySearch(value: String) {
        _querySearch.value = value
    }

}
