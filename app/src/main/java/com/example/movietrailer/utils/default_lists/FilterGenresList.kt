package com.example.movietrailer.utils.default_lists

fun getGenreFilterHashMap(): LinkedHashMap<Int, String>{

    val hashMap = LinkedHashMap<Int, String>()
    hashMap[12] = "Adventure"
    hashMap[14] = "Fantasy"
    hashMap[16] = "Animation"
    hashMap[27] = "Horror"
    hashMap[35] = "Comedy"
    hashMap[10749] = "Romance"
    hashMap[36] = "History"
    hashMap[53] = "Thriller"
    hashMap[80] = "Crime"
    hashMap[99] = "Documentary"
    hashMap[878] = "Science"
    hashMap[10402] = "Music"
    hashMap[10751] = "Family"
    hashMap[10752] = "War"

    return hashMap

}

fun getGenresFilerList(): List<String>{

    var list = mutableListOf<String>()

    for (i in getGenreFilterHashMap()){
        list.add(i.value)
    }

    return list

}