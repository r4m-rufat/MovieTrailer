package com.example.movietrailer.dialogs

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.movietrailer.R

fun showClearAllHistoryDialog(context: Context, onCLick: () -> Unit){

    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.dialog_clear_all_history)

    val cancelButton = dialog.findViewById<RelativeLayout>(R.id.rel_cancel)
    val clearButton = dialog.findViewById<RelativeLayout>(R.id.rel_clear)
    val closeButton = dialog.findViewById<ImageView>(R.id.ic_closeDialog)

    cancelButton.setOnClickListener {
        dialog.dismiss()
    }

    closeButton.setOnClickListener {
        dialog.dismiss()
    }

    clearButton.setOnClickListener {
        onCLick()
        dialog.dismiss()
    }

    dialog.show()

}