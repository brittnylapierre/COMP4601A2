package edu.carleton.comp4601.models;

import java.util.ArrayList;

public class User {
	String	name;
	ArrayList<Double> dimensions = new ArrayList<Double>();
	ArrayList<String> reviewedMovies = new ArrayList<String>();
	int community;

	public User(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Double> getDimensions() {
		return dimensions;
	}

	public void setDimensions(ArrayList<Double> dimensions) {
		this.dimensions = dimensions;
	}
	
	public ArrayList<String> getReviewedMovies() {
		return reviewedMovies;
	}

	public void setReviewedMovies(ArrayList<String> reviewedMovies) {
		this.reviewedMovies = reviewedMovies;
	}
	
	public int getCommunity() {
		return community;
	}

	public void setCommunity(int community) {
		this.community = community;
	}
}
