package com.example.movietrailer.internal_storage;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private SharedPreferences sharedPreferences;

    public PreferenceManager(Context context){
        sharedPreferences = context.getSharedPreferences("name",Context.MODE_PRIVATE);
    }

    public void putBoolean(String key, boolean value){

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(key, value);
        editor.apply();

    }

    public boolean getBoolean(String key){

        return sharedPreferences.getBoolean(key, false);

    }

}
