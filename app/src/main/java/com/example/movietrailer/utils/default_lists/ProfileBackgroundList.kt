package com.example.movietrailer.utils.default_lists

import com.example.movietrailer.R

fun getProfileBackgroundColorHashMap(): LinkedHashMap<String, Int>{

    val linkedHashMap = LinkedHashMap<String, Int>()

    linkedHashMap["#FF6F00"] = R.color.profile_1
    linkedHashMap["#FF0000"] = R.color.profile_2
    linkedHashMap["#FFFF00"] = R.color.profile_3
    linkedHashMap["#00CC00"] = R.color.profile_4
    linkedHashMap["#00CCCC"] = R.color.profile_5
    linkedHashMap["#0066CC"] = R.color.profile_6
    linkedHashMap["#0000CC"] = R.color.profile_7
    linkedHashMap["#6600CC"] = R.color.profile_8
    linkedHashMap["#CC00CC"] = R.color.profile_9
    linkedHashMap["#CC0066"] = R.color.profile_10
    linkedHashMap["#404040"] = R.color.profile_11
    linkedHashMap["#000033"] = R.color.profile_12
    linkedHashMap["#330033"] = R.color.profile_13

    return linkedHashMap

}

fun getProfileBackgroundList(): List<String>{

    var list = mutableListOf<String>()

    for (i in getProfileBackgroundColorHashMap()){
        list.add(i.key)
    }

    return list
}