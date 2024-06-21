package com.dicoding.kulitku.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.kulitku.R
import com.dicoding.kulitku.api.ResponseArticleItem
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.dicoding.kulitku.databinding.ItemArticleBinding

class ArticleAdapter(
    private val listener: OnArticleClickListener
) : ListAdapter<ResponseArticleItem, ArticleAdapter.ArticleViewHolder>(ArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val articleItem = getItem(position)
        holder.bind(articleItem)
        holder.itemView.setOnClickListener {
            listener.onArticleClick(position)
        }
    }

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(articleItem: ResponseArticleItem) {
            Glide.with(itemView.context)
                .load(articleItem.image_url)
                .placeholder(R.drawable.ic_place_holder)
                .into(binding.articleImage)

            Log.d("Article Adapter", articleItem.image_url)
            binding.articleTitle.text = articleItem.title ?: ""
            binding.articleDescription.text = getShortenedDescription(articleItem.content)
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

    class ArticleDiffCallback : DiffUtil.ItemCallback<ResponseArticleItem>() {
        override fun areItemsTheSame(
            oldItem: ResponseArticleItem,
            newItem: ResponseArticleItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResponseArticleItem,
            newItem: ResponseArticleItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}

interface OnArticleClickListener {
    fun onArticleClick(position: Int)
}

