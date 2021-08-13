package com.example.movietrailer.models.discover_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultsItem{

	@SerializedName("overview")
	private String overview;

	@SerializedName("original_language")
	private String originalLanguage;

	@SerializedName("original_title")
	private String originalTitle;

	@SerializedName("video")
	private boolean video;

	@SerializedName("title")
	private String title;

	@SerializedName("genre_ids")
	private List<Integer> genreIds;

	@SerializedName("poster_path")
	private String posterPath;

	@SerializedName("backdrop_path")
	private String backdropPath;

	@SerializedName("release_date")
	private String releaseDate;

	@SerializedName("popularity")
	private double popularity;

	@SerializedName("vote_average")
	private double voteAverage;

	@SerializedName("id")
	private int id;

	@SerializedName("adult")
	private boolean adult;

	@SerializedName("vote_count")
	private int voteCount;

	public String getOverview() {
		return overview;
	}

	public String getOriginalLanguage() {
		return originalLanguage;
	}

	public String getOriginalTitle() {
		return originalTitle;
	}

	public boolean isVideo() {
		return video;
	}

	public String getTitle() {
		return title;
	}

	public List<Integer> getGenreIds() {
		return genreIds;
	}

	public String getPosterPath() {
		return posterPath;
	}

	public String getBackdropPath() {
		return backdropPath;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public double getPopularity() {
		return popularity;
	}

	public double getVoteAverage() {
		return voteAverage;
	}

	public int getId() {
		return id;
	}

	public boolean isAdult() {
		return adult;
	}

	public int getVoteCount() {
		return voteCount;
	}
}