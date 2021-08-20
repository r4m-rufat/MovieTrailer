package com.example.movietrailer.utils.converters

fun convertSecondToTime(time: Int): String{

    val minute = time / 60
    val second = time % 60

    val minuteString = when (minute) {
        0 -> "00"
        in 1..9 -> "0$minute"
        else -> "$minute"
    }

    val secondString = when (second) {
        0 -> "00"
        in 1..9 -> "0$second"
        else -> "$second"
    }

    return "$minuteString:$secondString"

}