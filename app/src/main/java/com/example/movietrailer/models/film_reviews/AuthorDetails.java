package com.example.movietrailer.models.film_reviews;

import com.google.gson.annotations.SerializedName;

public class AuthorDetails{

	@SerializedName("avatar_path")
	private String avatarPath;
	private String name;
	private int rating;
	private String username;

	public String getAvatarPath(){
		return avatarPath;
	}

	public String getName(){
		return name;
	}

	public int getRating(){
		return rating;
	}

	public String getUsername(){
		return username;
	}
}
