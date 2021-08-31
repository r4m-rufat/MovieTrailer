package com.example.movietrailer.utils.default_lists

fun getGenreFilterHashMap(): LinkedHashMap<String, Int>{

    val hashMap = LinkedHashMap<String, Int>()

    hashMap["Adventure"] = 12
    hashMap["Fantasy"] = 14
    hashMap["Animation"] = 16
    hashMap["Horror"] = 27
    hashMap["Comedy"] = 35
    hashMap["Romance"] = 10749
    hashMap["History"] = 36
    hashMap["Thriller"] = 53
    hashMap["Crime"] = 80
    hashMap["Documentary"] = 99
    hashMap["Science"] = 878
    hashMap["Music"] = 10402
    hashMap["Family"] = 10751
    hashMap["War"] = 10752

    return hashMap

}

fun getGenresFilerList(): List<String>{

    var list = mutableListOf<String>()

    for (i in getGenreFilterHashMap()){
        list.add(i.key)
    }

    return list

}