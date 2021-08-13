package com.example.movietrailer.utils.bottom_navigation

import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.movietrailer.R

fun setUpBottomNavigationView(bottomNavigation: MeowBottomNavigation) {
    bottomNavigation.add(
        MeowBottomNavigation.Model(
            BottomNavigationBarItems.WISHLIST.ordinal,
            R.drawable.ic_favorite
        )
    )
    bottomNavigation.add(
        MeowBottomNavigation.Model(
            BottomNavigationBarItems.FILMS.ordinal,
            R.drawable.ic_films
        )
    )
    bottomNavigation.add(
        MeowBottomNavigation.Model(
            BottomNavigationBarItems.ACCOUNT.ordinal,
            R.drawable.ic_account
        )
    )
    bottomNavigation.setOnClickMenuListener { model: MeowBottomNavigation.Model ->
        when (model.icon) {
            R.drawable.ic_favorite -> {
            }
            R.drawable.ic_films -> {
            }
            R.drawable.ic_account -> {
            }
        }
        null
    }
    bottomNavigation.show(BottomNavigationBarItems.FILMS.ordinal, true)
}