package com.example.movietrailer.adapters.home_page

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.movietrailer.R

class RecyclerGenresFilterAdapter(context: Context, genreList: List<String>): RecyclerView.Adapter<RecyclerGenresFilterAdapter.ViewHolder>() {

    private var context: Context = context
    private var genreList: List<String> = genreList

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_genres_filter_recycer_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerGenresFilterAdapter.ViewHolder, position: Int) {
        holder._filter_genre.text = genreList[position]
        holder.itemView.setOnClickListener {
            holder._filter_genre.background = ContextCompat.getDrawable(context, R.drawable.background_selected_filter_genre_item)
            holder._filter_genre.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    override fun getItemCount(): Int {
        return genreList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val _filter_genre: TextView = itemView.findViewById(R.id.txt_filter_genre)

    }

}