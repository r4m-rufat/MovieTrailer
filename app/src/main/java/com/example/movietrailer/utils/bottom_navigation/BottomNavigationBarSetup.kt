package com.example.movietrailer.utils.bottom_navigation

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.movietrailer.R
import com.example.movietrailer.activities.HomeActivity

fun setUpBottomNavigationView(bottomNavigation: MeowBottomNavigation, view: View) {
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
                Navigation.findNavController(view).navigate(R.id.action_to_wishListFragment)
            }
            R.drawable.ic_films -> {
                Navigation.findNavController(view).navigate(R.id.action_to_filmsFragment)
            }
            R.drawable.ic_account -> {
                Navigation.findNavController(view).navigate(R.id.action_to_viewAccountFragment)
            }
        }
    }
}