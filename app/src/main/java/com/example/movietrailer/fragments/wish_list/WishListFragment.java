package com.example.movietrailer.fragments.wish_list;

import static com.example.movietrailer.utils.bottom_navigation.BottomNavigationBarSetupKt.setUpBottomNavigationView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.movietrailer.R;
import com.example.movietrailer.utils.bottom_navigation.BottomNavigationBarItems;

public class WishListFragment extends Fragment {

    private MeowBottomNavigation bottomNavigation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);
        getWidgets(view);
        bottomNavigation.show(BottomNavigationBarItems.WISHLIST.ordinal(), true);
        setUpBottomNavigationView(bottomNavigation, view);
        return view;
    }

    private void getWidgets(View view){

        bottomNavigation = view.findViewById(R.id.bottom_navigation_view);

    }
}