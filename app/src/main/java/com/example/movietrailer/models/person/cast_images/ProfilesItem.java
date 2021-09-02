package com.example.movietrailer.models.person.cast_images;

import com.google.gson.annotations.SerializedName;

public class ProfilesItem{
	private double aspectRatio;
	@SerializedName("file_path")
	private String filePath;
	private double voteAverage;
	private int width;
	private Object iso6391;
	private int voteCount;
	private int height;

	public double getAspectRatio(){
		return aspectRatio;
	}

	public String getFilePath(){
		return filePath;
	}

	public double getVoteAverage(){
		return voteAverage;
	}

	public int getWidth(){
		return width;
	}

	public Object getIso6391(){
		return iso6391;
	}

	public int getVoteCount(){
		return voteCount;
	}

	public int getHeight(){
		return height;
	}
}
