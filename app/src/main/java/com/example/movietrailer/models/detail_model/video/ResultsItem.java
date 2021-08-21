package com.example.movietrailer.models.detail_model.video;

public class ResultsItem{
	private String site;
	private int size;
	private String iso31661;
	private String name;
	private boolean official;
	private String id;
	private String type;
	private String publishedAt;
	private String iso6391;
	private String key;

	public String getSite(){
		return site;
	}

	public int getSize(){
		return size;
	}

	public String getIso31661(){
		return iso31661;
	}

	public String getName(){
		return name;
	}

	public boolean isOfficial(){
		return official;
	}

	public String getId(){
		return id;
	}

	public String getType(){
		return type;
	}

	public String getPublishedAt(){
		return publishedAt;
	}

	public String getIso6391(){
		return iso6391;
	}

	public String getKey(){
		return key;
	}
}
