package com.example.movietrailer.models.detail_model.casts;

import com.google.gson.annotations.SerializedName;

public class CastItem{
	private int castId;
	private String character;
	private int gender;
	private String creditId;
	private String knownForDepartment;
	private String originalName;
	private double popularity;
	private String name;

	@SerializedName("profile_path")
	private String profilePath;

	private int id;
	private boolean adult;
	private int order;

	public int getCastId(){
		return castId;
	}

	public String getCharacter(){
		return character;
	}

	public int getGender(){
		return gender;
	}

	public String getCreditId(){
		return creditId;
	}

	public String getKnownForDepartment(){
		return knownForDepartment;
	}

	public String getOriginalName(){
		return originalName;
	}

	public double getPopularity(){
		return popularity;
	}

	public String getName(){
		return name;
	}

	public String getProfilePath(){
		return profilePath;
	}

	public int getId(){
		return id;
	}

	public boolean isAdult(){
		return adult;
	}

	public int getOrder(){
		return order;
	}
}
