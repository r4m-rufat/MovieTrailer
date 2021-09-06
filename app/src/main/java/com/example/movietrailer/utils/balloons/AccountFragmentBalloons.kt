package com.example.movietrailer.utils.balloons

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.movietrailer.R
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon

@SuppressLint("Range")
fun showEditBalloon(context: Context, editName: ImageView, message: String, onClick: () -> Unit) {

    val balloon = createBalloon(context) {
        arrowSize = 30
        arrowOrientation = ArrowOrientation.BOTTOM
        isVisibleArrow = true
        width = BalloonSizeSpec.WRAP
        height = BalloonSizeSpec.WRAP
        textSize = 14f
        arrowPosition = 0.9f
        cornerRadius = 15f
        text = message
        textColor = ContextCompat.getColor(context, R.color.white)
        backgroundColor = Color.parseColor("#004D6E")
        balloonAnimation = BalloonAnimation.ELASTIC
        iconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_edit)
        iconColor = ContextCompat.getColor(context, R.color.white)
        iconWidth = 50
        iconHeight = 50
        marginRight = 10
        paddingLeft = 15
        paddingTop = 15
        paddingBottom = 15
        paddingRight = 15
    }

    balloon.setOnBalloonClickListener {
        balloon.dismiss()
        onClick()
    }

    balloon.showAlignTop(editName)

}