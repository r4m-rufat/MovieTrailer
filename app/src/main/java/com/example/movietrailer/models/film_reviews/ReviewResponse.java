package com.example.movietrailer.models.film_reviews;

import java.util.List;

public class ReviewResponse{
	private int id;
	private int page;
	private int totalPages;
	private List<ResultsItem> results;
	private int totalResults;

	public int getId(){
		return id;
	}

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