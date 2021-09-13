package com.example.movietrailer.utils.icon_setup

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.movietrailer.R
import com.example.movietrailer.internal_storage.PreferenceManager

fun setDefaultMovieIcon(context: Context, filmImage: ImageView){

    val preferenceManager = PreferenceManager(context)
    filmImage.scaleType = ImageView.ScaleType.CENTER_INSIDE

    if (preferenceManager.getBoolean("dark_mode")){
        filmImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_logo_white))
    }else{
        filmImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_logo))
    }

}