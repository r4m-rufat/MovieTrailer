package com.example.movietrailer.converters

fun convertValueToBudget(budget: Int): String{

    return when(budget.toString().length){
        in 7..9 -> "${(budget/1000000)}M"
        in 3..6 -> "${(budget/1000)}K"
        else -> "${(budget/1000000000)}B"
    }

}