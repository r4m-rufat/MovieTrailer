package com.example.movietrailer.converters

import com.example.movietrailer.models.detail_model.details.GenresItem

fun convertGenresListToString(list: List<GenresItem>): String{

    var string = ""
    try {
        when(list.size){

            0 -> string

            1 -> {
                string = list[0].name
            }

            else -> {
                for (item in list.indices){

                    string += if (item == 0){
                        list[item].name
                    }else if (item == 3){
                        break
                    } else{
                        " | ${list[item].name}"
                    }

                }
            }

        }
    }catch (e: NullPointerException){

        e.printStackTrace()

    }

    return string

}