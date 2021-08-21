package com.example.movietrailer.utils.canditions

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.movietrailer.R
import com.mikhaellopez.circularprogressbar.CircularProgressBar

fun setProgressIndicatorColor(rate: Int, circularProgressBar: CircularProgressBar, context: Context){

    when {
        rate >= 70 -> {
            circularProgressBar.progressBarColor = ContextCompat.getColor(context, R.color.green)
        }
        rate in 50..69 -> {
            circularProgressBar.progressBarColor = ContextCompat.getColor(context, R.color.progress_yellow)
        }
        rate in 30..49 -> {
            circularProgressBar.progressBarColor = ContextCompat.getColor(context, R.color.progress_orange)
        }
        rate in 1..29 -> {
            circularProgressBar.progressBarColor = ContextCompat.getColor(context, R.color.progress_red)
        }
        rate == 0 -> {
            circularProgressBar.progressBarColor = ContextCompat.getColor(context, R.color.gray)
        }
    }

}