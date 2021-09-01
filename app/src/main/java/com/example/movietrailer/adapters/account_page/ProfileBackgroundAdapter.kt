package com.example.movietrailer.adapters.account_page

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.movietrailer.R
import com.example.movietrailer.utils.default_lists.getProfileBackgroundColorHashMap
import de.hdodenhof.circleimageview.CircleImageView

@SuppressLint("ResourceType")
class ProfileBackgroundAdapter(context: Context, colorList: List<String>, selectedItem: String, listener: OnClickColorListener): RecyclerView.Adapter<ProfileBackgroundAdapter.ViewHolder>() {

    private val context: Context = context
    private val colorList: List<String> = colorList
    private val listener: OnClickColorListener = listener
    private var selectedItem: String = selectedItem

    fun updateSelectedItem(newItem: String){
        this.selectedItem = newItem
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_profile_background_recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.circleBackground.setImageResource(getProfileBackgroundColorHashMap()[colorList[position]]!!)
        if (selectedItem == colorList[position]){
            holder.selectedItemIcon.visibility = View.VISIBLE
        }else{
            holder.selectedItemIcon.visibility = View.INVISIBLE
        }
        holder.itemView.setOnClickListener {
            listener.onClickedItemCallBack(colorList[position])
        }

    }

    override fun getItemCount(): Int {
        return colorList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val circleBackground: CircleImageView = itemView.findViewById(R.id.circle_backgroundProfile)
        val selectedItemIcon: ImageView = itemView.findViewById(R.id.selectedBackgroundIcon)

    }

    interface OnClickColorListener{
        fun onClickedItemCallBack(color: String)
    }

}