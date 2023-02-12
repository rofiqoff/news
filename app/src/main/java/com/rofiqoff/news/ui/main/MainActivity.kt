package com.rofiqoff.news.ui.main

import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.rofiqoff.news.R
import com.rofiqoff.news.base.NewsResponse
import com.rofiqoff.news.data.api.model.Article
import com.rofiqoff.news.data.api.model.Source
import com.rofiqoff.news.databinding.ActivityMainBinding
import com.rofiqoff.news.ui.base.BaseActivity
import com.rofiqoff.news.ui.category.CategoryActivity
import com.rofiqoff.news.ui.detail.DetailActivity
import com.rofiqoff.news.ui.common.adapter.ArticleAdapter
import com.rofiqoff.news.ui.common.adapter.ArticleAdapter.Companion.ITEM_TYPE_HORIZONTAL
import com.rofiqoff.news.ui.main.adapter.CategoryAdapter
import com.rofiqoff.news.ui.main.adapter.SourceAdapter
import com.rofiqoff.news.utils.LinearItemDecoration
import com.rofiqoff.news.utils.getDimensInt
import com.rofiqoff.news.utils.invisible
import com.rofiqoff.news.utils.reObserve
import com.rofiqoff.news.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val headlineAdapter by lazy {
        ArticleAdapter(ITEM_TYPE_HORIZONTAL) {
            navigateToDetailPage(it)
        }
    }

    private val sourceAdapter by lazy {
        SourceAdapter {
            navigateToCategoryList(it)
        }
    }

    private val categoryAdapter by lazy {
        CategoryAdapter { data ->
            viewModel.category = data.value
            requestSource()
        }
    }

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = ActivityMainBinding::inflate

    override fun initView() {
        initList()

        observer()
        showCategory()
        request()

        binding.swipeRefresh.apply {
            setOnRefreshListener {
                isRefreshing = false
                request()
            }
        }
    }

    private fun initList() = with(binding) {
        rvTopHeadline.apply {
            adapter = headlineAdapter

            addItemDecoration(
                LinearItemDecoration(
                    space = context.getDimensInt(R.dimen.dimens_16dp),
                    spaceMiddle = context.getDimensInt(R.dimen.dimens_16dp),
                    orientation = LinearLayoutManager.HORIZONTAL
                )
            )
        }

        rvCategory.apply {
            adapter = categoryAdapter
            setHasFixedSize(false)

            addItemDecoration(
                LinearItemDecoration(
                    space = context.getDimensInt(R.dimen.dimens_16dp),
                    spaceMiddle = context.getDimensInt(R.dimen.dimens_16dp),
                    orientation = LinearLayoutManager.HORIZONTAL
                )
            )
        }

        rvSource.apply {
            adapter = sourceAdapter

            addItemDecoration(
                LinearItemDecoration(
                    space = context.getDimensInt(R.dimen.dimens_16dp),
                    spaceMiddle = context.getDimensInt(R.dimen.dimens_16dp),
                    orientation = LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    private fun request() {
        requestArticlesTopHeadline()
        requestSource()
    }

    private fun requestArticlesTopHeadline() {
        viewModel.fetchTopHeadline()
    }

    private fun requestSource() {
        viewModel.fetchSource()
    }

    private fun showCategory() {
        categoryAdapter.pushItems(viewModel.generateCategory())
    }

    private fun observer() = with(viewModel) {
        articleHeadlineLiveData.reObserve(this@MainActivity) { result ->
            when (result) {
                is NewsResponse.Loading -> onLoadingHeadline(true)
                is NewsResponse.Error -> {
                    onLoadingHeadline(false)
                    showToast(result.message)
                }
                is NewsResponse.Success -> onSuccessHeadline(result.data)
            }
        }

        sourceLiveData.reObserve(this@MainActivity) { result ->
            when (result) {
                is NewsResponse.Loading -> onLoadingSource(true)
                is NewsResponse.Error -> {
                    onLoadingSource(false)
                    showToast(result.message)
                }
                is NewsResponse.Success -> onSuccessSource(result.data)
            }
        }
    }

    private fun onLoadingHeadline(visible: Boolean) {
        binding.pbLoading.isVisible = visible
        binding.rvTopHeadline.isVisible = !visible
    }

    private fun onSuccessHeadline(data: List<Article>) {
        onLoadingHeadline(false)
        headlineAdapter.pushItems(data)
    }

    private fun onLoadingSource(show: Boolean) {
        binding.pbLoadingSource.isVisible = show
        binding.rvSource.apply {
            if (show) invisible() else visible()
        }
    }

    private fun onSuccessSource(data: List<Source>) {
        onLoadingSource(false)
        sourceAdapter.pushItems(data)
    }

    private fun navigateToCategoryList(source: Source) {
        Intent(this, CategoryActivity::class.java).apply {
            putExtra(CategoryActivity.PARAM_SOURCE_ID, source.id)
            putExtra(CategoryActivity.PARAM_SOURCE_NAME, source.name)
        }.also { startActivity(it) }
    }

    private fun navigateToDetailPage(article: Article) {
        Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.PARAM_URL, article.url)
        }.also { startActivity(it) }
    }

}
