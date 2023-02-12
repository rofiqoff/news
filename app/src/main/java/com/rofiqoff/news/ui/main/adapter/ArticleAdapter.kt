package com.rofiqoff.news.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.rofiqoff.news.data.api.model.Article
import com.rofiqoff.news.databinding.ItemArticleListBinding
import com.rofiqoff.news.databinding.ItemLoadingBinding
import com.rofiqoff.news.databinding.ItemTopHeadlineBinding
import com.rofiqoff.news.utils.autoNotify
import com.rofiqoff.news.utils.getWidthOfScreen
import com.rofiqoff.news.utils.toDateFormat
import kotlin.properties.Delegates

class ArticleAdapter(
    private val type: Int,
    private val onClick: (Article) -> Unit,
) : RecyclerView.Adapter<ViewHolder>() {

    private var items: List<Article> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { oldItem, newItem, _ ->
            oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            ITEM_TYPE_HORIZONTAL -> {
                val binding = ItemTopHeadlineBinding.inflate(inflater, parent, false)
                if (items.size > 1) {
                    binding.root.layoutParams =
                        ViewGroup.LayoutParams((getWidthOfScreen() * 0.60).toInt(),
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                }
                ViewHolderHorizontal(binding)
            }
            ITEM_TYPE_VERTICAL -> {
                ViewHolderVertical(ItemArticleListBinding.inflate(inflater, parent, false))
            }
            else -> {
                LoadingViewHolder(ItemLoadingBinding.inflate(inflater, parent, false))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].viewType) {
            Article.ViewType.LOADING -> ITEM_TYPE_LOADING
            else -> type
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderHorizontal -> holder.onBind(items[position], onClick)
            is ViewHolderVertical -> holder.onBind(items[position], onClick)
        }
    }

    override fun getItemCount() = items.size

    inner class LoadingViewHolder(binding: ItemLoadingBinding) : ViewHolder(binding.root)

    inner class ViewHolderHorizontal(
        private val binding: ItemTopHeadlineBinding,
    ) : ViewHolder(binding.root) {

        fun onBind(data: Article, onClick: (Article) -> Unit) {
            with(binding) {
                Glide.with(ivContent.context)
                    .load(data.urlToImage)
                    .into(ivContent)
                tvTitle.text = data.title
                tvSource.text = data.source.name
                tvTime.text = data.publishedAt.toDateFormat(binding.tvTime.context)

                cardParent.setOnClickListener {
                    onClick.invoke(data)
                }
            }
        }
    }

    inner class ViewHolderVertical(
        private val binding: ItemArticleListBinding,
    ) : ViewHolder(binding.root) {

        fun onBind(data: Article, onClick: (Article) -> Unit) {
            with(binding) {
                Glide.with(ivContent.context)
                    .load(data.urlToImage)
                    .into(ivContent)
                tvTitle.text = data.title
                tvSource.text = data.source.name
                tvAuthor.text = data.author
                tvTime.text = data.publishedAt.toDateFormat(binding.tvTime.context)

                cardParent.setOnClickListener {
                    onClick.invoke(data)
                }
            }
        }
    }

    fun pushItems(item: List<Article>) {
        val newItem = calculateMutableItems()
        newItem.addAll(item)
        items = newItem
    }

    fun pushLoading() {
        val newItem = calculateMutableItems()
        newItem.add(Article(viewType = Article.ViewType.LOADING))

        items = newItem
    }

    fun clear() {
        val newItems = calculateMutableItems()
        val itemCount = newItems.size
        newItems.clear()
        notifyItemRangeRemoved(0, itemCount)
        items = newItems
    }

    private fun calculateMutableItems(): MutableList<Article> {
        val newItems = items.toMutableList()
        newItems.remove(Article(viewType = Article.ViewType.LOADING))

        return newItems
    }

    companion object {
        const val ITEM_TYPE_LOADING = 0
        const val ITEM_TYPE_HORIZONTAL = 1
        const val ITEM_TYPE_VERTICAL = 2
    }
}
