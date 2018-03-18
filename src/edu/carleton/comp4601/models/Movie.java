package edu.carleton.comp4601.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Movie {
	String genre;
	String title;
	//<uid, review>
	HashMap<String, String> reviews = new HashMap<String, String>();
	//a shows who, then p shows review.
	
	public Movie(String title, String genre) {
		this.genre = genre;
		this.title = title;
	}
	
	public String getGenre() {
		return genre;
	}


	public void setGenre(String genre) {
		this.genre = genre;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public HashMap<String, String> getReviews() {
		return reviews;
	}
	
	public void setReviews(HashMap<String, String> reviews) {
		this.reviews = reviews;
	}
	
}
