package com.example.movietrailer.adapters.account_page

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movietrailer.R
import com.example.movietrailer.db.History
import com.example.movietrailer.utils.constants.IMAGE_BEGIN_URL

class HistoryAdapter(context: Context): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private val context: Context = context
    private var historyList: List<History> = mutableListOf()

    fun updateHistoryList(newList: List<History>){
        historyList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_history_recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.filmTitle.text = historyList[position].filmTitle
        holder.filmGenres.text = historyList[position].filmGenres
        holder.filmPopularity.text = historyList[position].filmRating.toString()

        Glide.with(context).load(IMAGE_BEGIN_URL + historyList[position].filmImage).into(holder.filmImage)
    }

    override fun getItemCount(): Int {
        if (historyList != null){
            return historyList.size
        }
        return 0
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val filmTitle: TextView = itemView.findViewById(R.id.txt_film_titleHistory)
        val filmGenres: TextView = itemView.findViewById(R.id.txt_film_genresHistory)
        val filmImage: ImageView = itemView.findViewById(R.id.film_historyImage)
        val filmPopularity: TextView = itemView.findViewById(R.id.txt_popularityHistory)
    }

}