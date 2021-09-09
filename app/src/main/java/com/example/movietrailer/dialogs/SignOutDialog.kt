package com.example.movietrailer.dialogs

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.movietrailer.R

fun signOutDialog(context: Context, onClickSignOut: () -> Unit){

    val dialog = Dialog(context, R.style.DialogStyle)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.dialog_sign_out_account)

    val cancelButton = dialog.findViewById<RelativeLayout>(R.id.rel_cancel)
    val signOutButton = dialog.findViewById<RelativeLayout>(R.id.rel_singOut)
    val closeButton = dialog.findViewById<ImageView>(R.id.ic_closeDialog)

    cancelButton.setOnClickListener {
        dialog.dismiss()
    }

    closeButton.setOnClickListener {
        dialog.dismiss()
    }

    signOutButton.setOnClickListener {
        onClickSignOut()
        dialog.dismiss()
    }

    dialog.show()

}