package com.example.movietrailer.converters

fun convertMinuteToTimeFormat(time: Int): String{

    val hour = time / 60
    val minute = time % 60

    val hourString = when (hour) {
        0 -> "00"
        in 1..9 -> "0$hour"
        else -> "$hour"
    }

    val minuteString = when (minute) {
        0 -> "00"
        in 1..9 -> "0$minute"
        else -> "$minute"
    }

    return "$hourString:$minuteString:00"

}