package com.example.movietrailer.adapters.home_page

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.movietrailer.R
import com.example.movietrailer.utils.default_lists.getGenreFilterHashMap

class RecyclerGenresFilterAdapter(context: Context, genreList: List<String>, filterList: List<Int>, onClickedGenreItemListener: OnClickedGenreItemListener): RecyclerView.Adapter<RecyclerGenresFilterAdapter.ViewHolder>() {

    private var context: Context = context
    private var genreList: List<String> = genreList
    private val onClickedGenreItemListener: OnClickedGenreItemListener = onClickedGenreItemListener
    private var filterList: List<Int> = filterList

    fun updateFilterList(newList: List<Int>){
        this.filterList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_genres_filter_recycer_view_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: RecyclerGenresFilterAdapter.ViewHolder, position: Int) {
        holder._filter_genre.text = genreList[position]

        if (filterList.isNotEmpty()){
            if (filterList.contains(getGenreFilterHashMap()[genreList[position]])){
                holder._filter_genre.background = ContextCompat.getDrawable(context, R.drawable.background_selected_filter_genre_item)
                holder._filter_genre.setTextColor(ContextCompat.getColor(context, R.color.white))
            }else{
                holder._filter_genre.background = ContextCompat.getDrawable(context, R.drawable.background_unselected_filter_genre_item)
            }
        }

        holder.itemView.setOnClickListener {

            if (filterList.contains(getGenreFilterHashMap()[genreList[position]])){
                onClickedGenreItemListener.onDeleteGenreItemCallBack(genre = genreList[position])
            }else{
                onClickedGenreItemListener.onAddGenreItemCallBack(genre = genreList[position])
            }

        }
    }

    override fun getItemCount(): Int {
        return genreList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val _filter_genre: TextView = itemView.findViewById(R.id.txt_filter_genre)

    }

    interface OnClickedGenreItemListener{
        fun onAddGenreItemCallBack(genre: String)
        fun onDeleteGenreItemCallBack(genre: String)
    }

}