package com.rofiqoff.news.ui.category

import android.content.Intent
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.rofiqoff.news.R
import com.rofiqoff.news.base.NewsResponse
import com.rofiqoff.news.data.api.model.Article
import com.rofiqoff.news.databinding.ActivityCategoryBinding
import com.rofiqoff.news.ui.base.BaseActivity
import com.rofiqoff.news.ui.detail.DetailActivity
import com.rofiqoff.news.ui.common.adapter.ArticleAdapter
import com.rofiqoff.news.utils.LinearItemDecoration
import com.rofiqoff.news.utils.getDimensInt
import com.rofiqoff.news.utils.invisible
import com.rofiqoff.news.utils.reObserve
import com.rofiqoff.news.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryActivity : BaseActivity<ActivityCategoryBinding>() {

    private var nextPage = 1

    private val viewModel by viewModels<CategoryViewModel>()

    private val listAdapter by lazy {
        ArticleAdapter(ArticleAdapter.ITEM_TYPE_VERTICAL) {
            navigateToDetailPage(it)
        }
    }

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = ActivityCategoryBinding::inflate

    override fun initView() {
        setUpView()
        observer()
        request()
    }

    private fun setUpView() = with(binding) {
        initList()

        val sourceId = intent?.getStringExtra(PARAM_SOURCE_ID).orEmpty()
        val sourceName = intent?.getStringExtra(PARAM_SOURCE_NAME).orEmpty()

        viewModel.source = sourceId

        tvTitle.text = sourceName.ifEmpty { getString(R.string.app_name) }

        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
            nextPage = 1
            request()
        }

        nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            if (binding.nestedScrollView.getChildAt(0).bottom <= (binding.nestedScrollView.height + binding.nestedScrollView.scrollY)) {
                if (viewModel.checkLoadMore(listAdapter.itemCount)) {
                    nextPage++
                    listAdapter.pushLoading()
                    request()
                }
            }
        }

        ivBack.setOnClickListener { finish() }
        ivSearch.setOnClickListener { navigateToSearch() }
    }

    private fun initList() {
        binding.rvList.apply {
            adapter = listAdapter

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
        viewModel.fetchArticle(nextPage)
    }

    private fun observer() {
        viewModel.articleLiveData.reObserve(this) { result ->
            when (result) {
                is NewsResponse.Loading -> onLoading(true)
                is NewsResponse.Error -> {
                    onLoading(false)
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
                is NewsResponse.Success -> onSuccess(result.data)
            }
        }
    }

    private fun onLoading(show: Boolean) {
        if (nextPage == 1) listAdapter.clear()

        binding.pbLoading.isVisible = show && nextPage == 1
        binding.rvList.apply {
            if (show && nextPage == 1) invisible() else visible()
        }
    }

    private fun onSuccess(data: List<Article>) {
        onLoading(false)
        listAdapter.pushItems(data)
    }

    private fun navigateToDetailPage(article: Article) {
        Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.PARAM_URL, article.url)
        }.also { startActivity(it) }
    }

    private fun navigateToSearch() {

    }

    companion object {
        const val PARAM_SOURCE_ID = "param-source-id"
        const val PARAM_SOURCE_NAME = "param-source-name"
    }

}
