package com.example.movietrailer.utils.default_lists;

public enum TopCategoriesItem{

    POPULAR("popular"),
    TOP_RATED("top_rated"),
    UP_COMING("up_coming"),
    NOW_PLAYING("now_playing");

    private String value;

    TopCategoriesItem(String value) {
        this.value = value;
    }
}
