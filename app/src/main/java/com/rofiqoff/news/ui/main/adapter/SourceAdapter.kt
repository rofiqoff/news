package com.rofiqoff.news.ui.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rofiqoff.news.data.api.model.Source
import com.rofiqoff.news.databinding.ItemSourceBinding

class SourceAdapter(
    private val onClick: (Source) -> Unit,
) : RecyclerView.Adapter<SourceAdapter.ViewHolder>() {

    private var data = listOf<Source>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSourceBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(data[position], onClick)
    }

    override fun getItemCount() = data.size

    inner class ViewHolder(
        private val binding: ItemSourceBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: Source, onClick: (Source) -> Unit) {
            binding.tvSourceName.text = data.name
            binding.tvSourceDesc.text = data.description.ifEmpty { "-" }

            binding.root.setOnClickListener {
                onClick.invoke(data)
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun pushItems(data: List<Source>) {
        this.data = data
        notifyDataSetChanged()
    }

}
