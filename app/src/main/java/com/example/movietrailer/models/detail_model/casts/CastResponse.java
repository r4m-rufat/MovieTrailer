package com.example.movietrailer.models.detail_model.casts;

import java.util.List;

public class CastResponse{
	private List<CastItem> cast;
	private int id;
	private List<CrewItem> crew;

	public List<CastItem> getCast(){
		return cast;
	}

	public int getId(){
		return id;
	}

	public List<CrewItem> getCrew(){
		return crew;
	}
}