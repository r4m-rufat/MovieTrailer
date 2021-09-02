package com.example.movietrailer.models.person.cast_detail;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastDetailResponse{

	@SerializedName("also_known_as")
	private List<String> alsoKnownAs;

	private String birthday;
	private int gender;
	private String imdbId;

	@SerializedName("known_for_department")
	private String knownForDepartment;

	@SerializedName("profile_path")
	private String profilePath;
	private String biography;
	private String deathday;

	@SerializedName("place_of_birth")
	private String placeOfBirth;

	private double popularity;
	private String name;
	private int id;
	private boolean adult;
	private String homepage;

	public List<String> getAlsoKnownAs(){
		return alsoKnownAs;
	}

	public String getBirthday(){
		return birthday;
	}

	public int getGender(){
		return gender;
	}

	public String getImdbId(){
		return imdbId;
	}

	public String getKnownForDepartment(){
		return knownForDepartment;
	}

	public String getProfilePath(){
		return profilePath;
	}

	public String getBiography(){
		return biography;
	}

	public String getDeathday(){
		return deathday;
	}

	public String getPlaceOfBirth(){
		return placeOfBirth;
	}

	public double getPopularity(){
		return popularity;
	}

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public boolean isAdult(){
		return adult;
	}

	public String getHomepage(){
		return homepage;
	}
}