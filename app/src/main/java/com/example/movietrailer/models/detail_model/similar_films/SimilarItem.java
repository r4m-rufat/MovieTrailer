package com.example.movietrailer.models.detail_model.similar_films;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SimilarItem {
	private String overview;
	private String originalLanguage;
	private String originalTitle;
	private boolean video;
	private String title;
	private List<Integer> genreIds;

	@SerializedName("poster_path")
	private String posterPath;

	private String backdropPath;
	private String releaseDate;
	private double popularity;

	@SerializedName("vote_average")
	private double voteAverage;
	
	private int id;
	private boolean adult;
	private int voteCount;

	public String getOverview(){
		return overview;
	}

	public String getOriginalLanguage(){
		return originalLanguage;
	}

	public String getOriginalTitle(){
		return originalTitle;
	}

	public boolean isVideo(){
		return video;
	}

	public String getTitle(){
		return title;
	}

	public List<Integer> getGenreIds(){
		return genreIds;
	}

	public String getPosterPath(){
		return posterPath;
	}

	public String getBackdropPath(){
		return backdropPath;
	}

	public String getReleaseDate(){
		return releaseDate;
	}

	public double getPopularity(){
		return popularity;
	}

	public double getVoteAverage(){
		return voteAverage;
	}

	public int getId(){
		return id;
	}

	public boolean isAdult(){
		return adult;
	}

	public int getVoteCount(){
		return voteCount;
	}
}