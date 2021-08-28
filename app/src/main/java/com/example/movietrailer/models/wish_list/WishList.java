package com.example.movietrailer.models.wish_list;

public class WishList {

    private int filmID;
    private String filmImage;
    private String filmTitle;
    private String filmGenres;
    private double voteAverage;

    /**
     * this WishList constructor is required otherwise
     * RealtimeDatabase doesn't accept this class
     */
    public WishList() {
    }

    public WishList(int filmID, String filmImage, String filmTitle, String filmGenres, double voteAverage) {
        this.filmID = filmID;
        this.filmImage = filmImage;
        this.filmTitle = filmTitle;
        this.filmGenres = filmGenres;
        this.voteAverage = voteAverage;
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

    public String getFilmGenres() {
        return filmGenres;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

}
