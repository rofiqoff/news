package com.rofiqoff.news.ui.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rofiqoff.news.data.api.model.Category
import com.rofiqoff.news.databinding.ItemCategoryBinding
import com.rofiqoff.news.utils.getFirstChar

class CategoryAdapter(
    private val onClick: (Category) -> Unit,
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var data = listOf<Category>()
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(data[position], position, onClick)
    }

    override fun getItemCount() = data.size

    inner class ViewHolder(
        private val binding: ItemCategoryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: Category, position: Int, onClick: (Category) -> Unit) {
            binding.tvCategory.apply {
                text = data.name.getFirstChar()
                isSelected = selectedPosition == position
            }

            binding.tvCategoryTitle.text = data.name
            binding.parent.setOnClickListener {
                onClick.invoke(data)

                notifyItemChanged(selectedPosition)

                selectedPosition = position
                notifyItemChanged(position)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun pushItems(data: List<Category>) {
        this.data = data
        notifyDataSetChanged()
    }

}
