package com.dicoding.kulitku.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kulitku.data.InformationItem
import com.dicoding.kulitku.databinding.ItemSkinInformationBinding

class SkinInformationAdapter(private val informasiItem: List<InformationItem>, private val listener: OnSkinInformationClickListener) :
    RecyclerView.Adapter<SkinInformationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemSkinInformationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val information = informasiItem[position]
        holder.bind(information)
        holder.itemView.setOnClickListener {
            listener.onInformationClick(position)
        }
    }

    override fun getItemCount(): Int {
        return informasiItem.size
    }

    class ViewHolder(private val binding: ItemSkinInformationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(informiItem: InformationItem) {
            binding.articleImage.setImageResource(informiItem.imageResId)
            binding.articleTitle.text = informiItem.title
//            binding.articleDescription.text = informiItem.desctiption
//            binding.viewMore
        }
    }
}

interface OnSkinInformationClickListener {
    fun onInformationClick(position: Int)
}