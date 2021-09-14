package com.example.movietrailer.activities.home;

import static com.example.movietrailer.utils.bottom_navigation.BottomNavigationBarSetupKt.setUpBottomNavigationView;
import static com.example.movietrailer.utils.snackbar.SnackbarSetupKt.snackBarSetup;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.movietrailer.R;
import com.example.movietrailer.utils.bottom_navigation.BottomNavigationBarItems;
import com.example.movietrailer.utils.check_connection.CheckConnectionAsynchronously;
import com.google.android.material.snackbar.Snackbar;

public class HomeActivity extends AppCompatActivity {

    private MeowBottomNavigation bottomNavigation;
    private RelativeLayout rel_home;
    private Context context;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = HomeActivity.this;

        CheckConnectionAsynchronously.INSTANCE.init(context);

        bottomNavigation = findViewById(R.id.bottom_navigation_view);
        rel_home = findViewById(R.id.rel_home);

        bottomNavigation.show(BottomNavigationBarItems.FILMS.ordinal(), true);
        setUpBottomNavigationView(bottomNavigation, HomeActivity.this);

        snackbar = Snackbar.make(rel_home, "", Snackbar.LENGTH_INDEFINITE);

        snackBarSetup(HomeActivity.this, snackbar);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CheckConnectionAsynchronously.INSTANCE.observe(HomeActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean connection) {
                        if (connection){
                            snackbar.dismiss();
                        }else{
                            snackbar.show();
                        }
                    }
                });
            }
        }, 1000);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}