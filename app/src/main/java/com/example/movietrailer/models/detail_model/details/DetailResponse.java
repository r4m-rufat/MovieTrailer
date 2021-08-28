package com.example.movietrailer.models.detail_model.details;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailResponse{
	private String originalLanguage;
	private String imdbId;
	private boolean video;
	private String title;

	@SerializedName("backdrop_path")
	private String backdropPath;

	private int revenue;
	private List<GenresItem> genres;
	private double popularity;
	private List<ProductionCountriesItem> productionCountries;
	private int id;
	private int voteCount;
	private int budget;

	@SerializedName("overview")
	private String overview;

	@SerializedName("original_title")
	private String originalTitle;

	private int runtime;

	@SerializedName("poster_path")
	private String posterPath;

	private List<SpokenLanguagesItem> spokenLanguages;
	private List<ProductionCompaniesItem> productionCompanies;

	@SerializedName("release_date")
	private String releaseDate;

	@SerializedName("vote_average")
	private double voteAverage;

	private BelongsToCollection belongsToCollection;
	private String tagline;
	private boolean adult;
	private String homepage;
	private String status;

	public String getOriginalLanguage(){
		return originalLanguage;
	}

	public String getImdbId(){
		return imdbId;
	}

	public boolean isVideo(){
		return video;
	}

	public String getTitle(){
		return title;
	}

	public String getBackdropPath(){
		return backdropPath;
	}

	public int getRevenue(){
		return revenue;
	}

	public List<GenresItem> getGenres(){
		return genres;
	}

	public double getPopularity(){
		return popularity;
	}

	public List<ProductionCountriesItem> getProductionCountries(){
		return productionCountries;
	}

	public int getId(){
		return id;
	}

	public int getVoteCount(){
		return voteCount;
	}

	public int getBudget(){
		return budget;
	}

	public String getOverview(){
		return overview;
	}

	public String getOriginalTitle(){
		return originalTitle;
	}

	public int getRuntime(){
		return runtime;
	}

	public String getPosterPath(){
		return posterPath;
	}

	public List<SpokenLanguagesItem> getSpokenLanguages(){
		return spokenLanguages;
	}

	public List<ProductionCompaniesItem> getProductionCompanies(){
		return productionCompanies;
	}

	public String getReleaseDate(){
		return releaseDate;
	}

	public double getVoteAverage(){
		return voteAverage;
	}

	public BelongsToCollection getBelongsToCollection(){
		return belongsToCollection;
	}

	public String getTagline(){
		return tagline;
	}

	public boolean isAdult(){
		return adult;
	}

	public String getHomepage(){
		return homepage;
	}

	public String getStatus(){
		return status;
	}
}