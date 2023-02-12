package com.rofiqoff.news.ui.search

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.rofiqoff.news.R
import com.rofiqoff.news.base.NewsResponse
import com.rofiqoff.news.data.api.model.Article
import com.rofiqoff.news.databinding.ActivitySearchBinding
import com.rofiqoff.news.ui.base.BaseActivity
import com.rofiqoff.news.ui.common.adapter.ArticleAdapter
import com.rofiqoff.news.ui.detail.DetailActivity
import com.rofiqoff.news.utils.LinearItemDecoration
import com.rofiqoff.news.utils.getDimensInt
import com.rofiqoff.news.utils.gone
import com.rofiqoff.news.utils.invisible
import com.rofiqoff.news.utils.reObserve
import com.rofiqoff.news.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : BaseActivity<ActivitySearchBinding>() {

    private var nextPage = 1

    private val viewModel by viewModels<SearchViewModel>()

    private val listAdapter by lazy {
        ArticleAdapter(ArticleAdapter.ITEM_TYPE_VERTICAL) {
            navigateToDetailPage(it)
        }
    }

    private val doneTypingChecker by lazy {
        Looper.myLooper()?.let {
            Handler(it)
        }
    }

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = ActivitySearchBinding::inflate

    override fun initView() {
        initList()
        observer()

        binding.ivBack.setOnClickListener { finish() }

        binding.svContent.apply {
            requestFocus()
            setOnQueryTextListener(onQueryTextListener())
        }

        binding.nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            if (binding.nestedScrollView.getChildAt(0).bottom <= (binding.nestedScrollView.height + binding.nestedScrollView.scrollY)) {
                if (viewModel.checkLoadMore(listAdapter.itemCount)) {
                    nextPage++
                    listAdapter.pushLoading()
                    requestSearch()
                }
            }
        }
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

    private fun requestSearch() {
        viewModel.fetchArticle(nextPage)
    }

    private fun observer() = with(viewModel) {
        querySearch.reObserve(this@SearchActivity) {
            if (it.trim().isNotEmpty()) {
                nextPage = 1
                requestSearch()
            }
        }

        articleLiveData.reObserve(this@SearchActivity) { result ->
            when (result) {
                is NewsResponse.Loading -> onLoadingNews(true)
                is NewsResponse.Error -> {
                    onLoadingNews(false)
                    showToast(result.message)

                    binding.tvNoData.visible()
                }
                is NewsResponse.Success -> onSuccessNews(result.data)
            }
        }
    }

    private fun onLoadingNews(visible: Boolean) {
        if (visible) binding.tvNoData.gone()

        if (nextPage == 1) listAdapter.clear()

        binding.pbLoading.isVisible = visible && nextPage == 1
        binding.rvList.apply {
            if (visible && nextPage == 1) invisible() else visible()
        }
    }

    private fun onSuccessNews(data: List<Article>) {
        binding.tvNoData.isVisible = data.isEmpty()

        onLoadingNews(false)
        listAdapter.pushItems(data)
    }

    private fun onQueryTextListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setQuerySearch(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                checkDoneTyping(newText?.trim().orEmpty())
                return true
            }
        }
    }

    private fun checkDoneTyping(input: String) {
        if (input.isNotEmpty()) {
            doneTypingChecker?.apply {
                removeCallbacksAndMessages(null)
                postDelayed({
                    if (input.isEmpty() || input.length >= 3) {
                        viewModel.setQuerySearch(input)
                    }
                }, 1_000)
            }
        } else {
            nextPage = 1
            doneTypingChecker?.removeCallbacksAndMessages(null)
            listAdapter.clear()
        }
    }

    private fun navigateToDetailPage(article: Article) {
        Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.PARAM_URL, article.url)
        }.also { startActivity(it) }
    }

}
