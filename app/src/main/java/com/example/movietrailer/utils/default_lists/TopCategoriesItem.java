package com.example.movietrailer.utils.default_lists;

public enum TopCategoriesItem{

    DISCOVER("Discover"),
    POPULAR("Popular"),
    NOW_PLAYING("Now Playing"),
    UP_COMING("Upcoming"),
    TOP_RATED("Top Rated");

    private String value;

    TopCategoriesItem(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
