package com.dicoding.kulitku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kulitku.R
import com.dicoding.kulitku.data.ArticleItem
import com.dicoding.kulitku.databinding.ItemArticleBinding

class ArticleAdapter(private val articleItems: List<ArticleItem>, private val listener: OnArticleClickListener) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val articleItem = articleItems[position]
        holder.bind(articleItem)
        holder.itemView.setOnClickListener {
            listener.onArcticleClick(position)
        }
    }

    override fun getItemCount(): Int {
        return articleItems.size
    }

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

//        init {
//            itemView.setOnClickListener(this)
//        }

        fun bind(articleItem: ArticleItem) {
            binding.articleTitle.text = articleItem.title
            binding.articleDescription.text = getShortenedDescription(articleItem.description)
        }

        private fun getShortenedDescription(description: String): String {
            val maxLength = 100 // Maximum length of description to show
            return if (description.length > maxLength) {
                description.substring(0, maxLength) + "..."
            } else {
                description
            }
        }

//        override fun onClick(v: View?) {
//            listener.onArcticleClick(adapterPosition)
//        }
    }
}

interface OnArticleClickListener {
    fun onArcticleClick(position: Int)
}