package com.example.movietrailer.utils.snackbar

import android.app.Activity
import android.widget.TextView
import com.example.movietrailer.R
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat
import com.example.movietrailer.internal_storage.PreferenceManager


fun snackBarSetup(
    activity: Activity,
    snackBar: Snackbar
){

    val customView = activity.layoutInflater.inflate(R.layout.snackbar_internet_connection, null);

    if (PreferenceManager(activity).getBoolean("dark_mode")){
        snackBar.view.setBackgroundColor(ContextCompat.getColor(activity, R.color.gray))
    }else{
        snackBar.view.setBackgroundColor(ContextCompat.getColor(activity, R.color.white))
    }
    val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout

    snackBarLayout.setPadding(0, 0, 0, 0)

    val dismiss = customView.findViewById(R.id.txt_dismiss) as TextView

    dismiss.setOnClickListener {
        snackBar.dismiss()
    }

    snackBarLayout.addView(customView, 0)

}