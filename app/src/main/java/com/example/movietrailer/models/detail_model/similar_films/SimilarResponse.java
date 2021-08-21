package com.example.movietrailer.models.detail_model.similar_films;

import java.util.List;

public class SimilarResponse{
	private int page;
	private int totalPages;
	private List<SimilarItem> results;
	private int totalResults;

	public int getPage(){
		return page;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public List<SimilarItem> getResults(){
		return results;
	}

	public int getTotalResults(){
		return totalResults;
	}
}