package com.example.movietrailer.models.person.cast_images;

import java.util.List;

public class CastImagesResponse{
	private List<ProfilesItem> profiles;
	private int id;

	public List<ProfilesItem> getProfiles(){
		return profiles;
	}

	public int getId(){
		return id;
	}
}