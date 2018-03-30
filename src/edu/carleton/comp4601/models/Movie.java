package edu.carleton.comp4601.models;

import java.util.ArrayList;
import java.util.HashMap;

import com.mongodb.BasicDBObject;

public class Movie extends BasicDBObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3292716895447573617L;
	String genre;
	String title;
	//<uname, review>
	HashMap<String, Review> reviews = new HashMap<String, Review>();
	ArrayList<String> usersAccessed = new ArrayList<String>();
	//HashMap<String, Sentiment> sentiments = new HashMap<String, Sentiment>();
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


	public HashMap<String, Review> getReviews() {
		return reviews;
	}
	
	public void setReviews(HashMap<String, Review> reviews) {
		this.reviews = reviews;
	}
	
	
	public ArrayList<String> getUsersAccessed() {
		return usersAccessed;
	}

	public void setUsersAccessed(ArrayList<String> usersAccessed) {
		this.usersAccessed = usersAccessed;
	}

	
}
