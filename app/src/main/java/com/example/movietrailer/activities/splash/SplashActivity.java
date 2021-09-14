package com.example.movietrailer.activities.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movietrailer.R;
import com.example.movietrailer.activities.home.HomeActivity;
import com.example.movietrailer.activities.registration.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private Animation txt_anim, image_anim;
    private ImageView image_logo;;
    private TextView app_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        app_name = findViewById(R.id.app_name);
        image_logo = findViewById(R.id.image_logo);

        txt_anim = AnimationUtils.loadAnimation(this, R.anim.splash_text_anim);
        image_anim = AnimationUtils.loadAnimation(this, R.anim.splash_logo_anim);
        app_name.startAnimation(txt_anim);
        image_logo.startAnimation(image_anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                }else{
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 2500L);

    }
}