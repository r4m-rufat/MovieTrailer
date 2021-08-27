package com.example.movietrailer.models.wish_list;

public class WishList {

    private int filmID;
    private String filmImage;
    private String filmTitle;

    /**
     * this WishList constructor is required otherwise
     * RealtimeDatabase doesn't accept this class
     */
    public WishList() {
    }

    public WishList(int filmID, String filmImage, String filmTitle) {
        this.filmID = filmID;
        this.filmImage = filmImage;
        this.filmTitle = filmTitle;
    }

    public int getFilmID() {
        return filmID;
    }

    public String getFilmImage() {
        return filmImage;
    }

    public String getFilmTitle() {
        return filmTitle;
    }
}
