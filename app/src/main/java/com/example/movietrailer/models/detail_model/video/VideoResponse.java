package com.example.movietrailer.models.detail_model.video;

import java.util.List;

public class VideoResponse{
	private int id;
	private List<ResultsItem> results;

	public int getId(){
		return id;
	}

	public List<ResultsItem> getResults(){
		return results;
	}
}