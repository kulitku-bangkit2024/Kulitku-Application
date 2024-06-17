package com.dicoding.kulitku.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kulitku.R
import com.dicoding.kulitku.data.Analyze
import java.text.NumberFormat

class HistoryAdapter(private var historyList: List<Analyze>, private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    interface ItemClickListener {
        fun onItemClick(analyze: Analyze)
    }

    fun updateData(newList: List<Analyze>) {
        historyList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val analyzeHistoryImg: ImageView = item.findViewById(R.id.analyze_history_image)
        val analyzeHistoryType: TextView = item.findViewById(R.id.analyze_history_type)
        val analyzeHistoryScore: TextView = item.findViewById(R.id.analyze_history_score)

        fun bind(analyze: Analyze) {
            analyzeHistoryImg.setImageURI(Uri.parse(analyze.uri))
            analyzeHistoryType.text = analyze.type
            analyzeHistoryScore.text = itemView.context.getString(
                R.string.analyze_score,
                NumberFormat.getPercentInstance().format(analyze.confidence)
            )
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        val analyzeHistory = historyList[position]
        holder.bind(analyzeHistory)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(analyzeHistory)
        }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}
