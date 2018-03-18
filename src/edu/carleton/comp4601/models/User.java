package edu.carleton.comp4601.models;

import java.util.ArrayList;

public class User {
	String	name;
	ArrayList<Double> dimensions = new ArrayList<Double>();
	
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
	
	
}
