package com.dicoding.kulitku.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kulitku.api.ResponseTipsItem
import com.dicoding.kulitku.databinding.ItemTipsBinding

class TipsAdapter(
    private val listener: OnTipsClickListener
) : ListAdapter<ResponseTipsItem, TipsAdapter.TipsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TipsViewHolder {
        val binding = ItemTipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TipsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TipsViewHolder, position: Int) {
        val tipsItem = getItem(position)
        holder.bind(tipsItem)
        holder.itemView.setOnClickListener {
            listener.onTipsClick(position)
        }
    }

    inner class TipsViewHolder(private val binding: ItemTipsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tipsItem: ResponseTipsItem) {
            binding.tipsTitle.text = tipsItem.title
            binding.tipsDescription.text = getShortenedDescription(tipsItem.content)
        }
    }

    private fun getShortenedDescription(description: String): String {
        val maxLength = 100
        return if (description.length > maxLength) {
            description.substring(0, maxLength) + "..."
        } else {
            description
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ResponseTipsItem>() {
        override fun areItemsTheSame(
            oldItem: ResponseTipsItem,
            newItem: ResponseTipsItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResponseTipsItem,
            newItem: ResponseTipsItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}

interface OnTipsClickListener {
    fun onTipsClick(position: Int)
}