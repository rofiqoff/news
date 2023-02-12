package com.rofiqoff.news.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rofiqoff.news.base.NewsResponse
import com.rofiqoff.news.data.api.model.Article
import com.rofiqoff.news.data.api.model.Source
import com.rofiqoff.news.data.api.repository.DataRepository
import com.rofiqoff.news.data.implementation.remote.request.NewsQuery
import com.rofiqoff.news.utils.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: DataRepository,
) : ViewModel() {

    var category = "business"
    var source = ""

    private val _articleHeadlineLiveData = MutableLiveData<NewsResponse<List<Article>>>()
    val articleHeadlineLiveData = _articleHeadlineLiveData.toLiveData()

    private val _sourceLiveData = MutableLiveData<NewsResponse<List<Source>>>()
    val sourceLiveData = _sourceLiveData.toLiveData()

    fun fetchTopHeadline() = viewModelScope.launch {
        _articleHeadlineLiveData.apply {
            postValue(NewsResponse.Loading)
            postValue(repository.fetchArticles(NewsQuery()))
        }
    }

    fun fetchSource() = viewModelScope.launch {
        _sourceLiveData.apply {
            postValue(NewsResponse.Loading)
            postValue(repository.fetchSources(NewsQuery(category = category, country = "us")))
        }
    }

    fun generateCategory() = repository.generateCategory()
}
