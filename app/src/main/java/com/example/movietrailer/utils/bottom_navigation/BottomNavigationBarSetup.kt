package com.example.movietrailer.utils.bottom_navigation

import androidx.navigation.Navigation
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.movietrailer.R
import com.example.movietrailer.activities.home.HomeActivity

fun setUpBottomNavigationView(bottomNavigation: MeowBottomNavigation, activity: HomeActivity) {
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
                Navigation.findNavController(activity, R.id.main_nav_graph).navigate(R.id.action_to_wishListFragment)
            }
            R.drawable.ic_films -> {
                Navigation.findNavController(activity, R.id.main_nav_graph).navigate(R.id.action_to_filmsFragment)
            }
            R.drawable.ic_account -> {
                Navigation.findNavController(activity, R.id.main_nav_graph).navigate(R.id.action_to_viewAccountFragment)
            }
        }
    }
}