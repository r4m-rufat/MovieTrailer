package com.example.movietrailer.models.film_reviews;

import com.google.gson.annotations.SerializedName;

public class ResultsItem{

	@SerializedName("author_details")
	private AuthorDetails authorDetails;

	private String updatedAt;
	private String author;

	@SerializedName("created_at")
	private String createdAt;

	private String id;
	private String content;
	private String url;

	public AuthorDetails getAuthorDetails(){
		return authorDetails;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getAuthor(){
		return author;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getId(){
		return id;
	}

	public String getContent(){
		return content;
	}

	public String getUrl(){
		return url;
	}
}
