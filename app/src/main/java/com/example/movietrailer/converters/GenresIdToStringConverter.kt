package com.example.movietrailer.converters

/**
 * genre ids like {28, 53} this type
 * should convert to 28,53 for api
 */
fun convertGenreIdsToString(list: List<Int>): String{

    var string = ""

    when(list.size){

        0 -> string = ""

        1 -> {
            string = list[0].toString()
        }

        else -> {
            for (i in list.indices){
                string += if (i == (list.size-1)){
                    "${list[i]}"
                }else{
                    "${list[i]},"
                }
            }
        }

    }

    return string

}