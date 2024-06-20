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

class HistoryAdapter(private var historyList: ArrayList<Analyze>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val analyzeHistoryImg: ImageView = item.findViewById(R.id.analyze_history_image)
        val analyzeHistoryType: TextView = item.findViewById(R.id.analyze_history_type)
//        val analyzeHistoryScore: TextView = item.findViewById(R.id.analyze_history_score)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val analyzeHistory = historyList[position]
        holder.analyzeHistoryImg.setImageURI(Uri.parse(analyzeHistory.uri))
        holder.analyzeHistoryType.text = analyzeHistory.type
//        holder.analyzeHistoryScore.text = holder.itemView.context.getString(
//            R.string.analyze_score,
//            NumberFormat.getPercentInstance().format(analyzeHistory.confidence).toString()
//        )
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}