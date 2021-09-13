package com.example.movietrailer.activities.home;

import static com.example.movietrailer.utils.bottom_navigation.BottomNavigationBarSetupKt.setUpBottomNavigationView;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.movietrailer.R;
import com.example.movietrailer.utils.bottom_navigation.BottomNavigationBarItems;

public class HomeActivity extends AppCompatActivity {

    private MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottom_navigation_view);

        bottomNavigation.show(BottomNavigationBarItems.FILMS.ordinal(), true);
        setUpBottomNavigationView(bottomNavigation, HomeActivity.this);


    }

}