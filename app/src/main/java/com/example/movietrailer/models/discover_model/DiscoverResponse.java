package com.example.movietrailer.models.discover_model;

import java.util.List;

public class DiscoverResponse{
	private int page;
	private int totalPages;
	private List<ResultsItem> results;
	private int totalResults;

	public int getPage(){
		return page;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public List<ResultsItem> getResults(){
		return results;
	}

	public int getTotalResults(){
		return totalResults;
	}
}