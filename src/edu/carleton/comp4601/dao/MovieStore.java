package edu.carleton.comp4601.dao;

import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.carleton.comp4601.models.Movie;

public class MovieStore {
	
	public ConcurrentHashMap<Integer, Movie> getMovies() {
		return movies;
	}

	public void setMovies(ConcurrentHashMap<Integer, Movie> movies) {
		this.movies = movies;
	}

	public static void setInstance(MovieStore instance) {
		MovieStore.instance = instance;
	}

	private ConcurrentHashMap<Integer, Movie> movies;
	private static MovieStore instance;
	
	public MovieStore() {
		movies = new ConcurrentHashMap<Integer, Movie>();
	}
	
	public Movie find(int id) {
		return movies.get(new Integer(id));
	}
	
	public Movie createMovie(String title, String genre) {
		Movie m = new Movie(title, genre);
		
		movies.put(movies.size(), m);
		System.out.println("userz: " + movies.toString());
		return m;
	}

	
	public boolean delete(int id) {
		if (find(id) != null) {
			Integer no = new Integer(id);
			movies.remove(no);
			return true;
		}
		else
			return false;
	}
	
	public boolean delete(String title) {
		int id = 0;
		boolean found = false;
		
		for(Enumeration<Movie> ms = movies.elements(); ms.hasMoreElements();){
			if(ms.nextElement().getTitle() == title){
				found = true;
			}
			id++;
		}
		
		if(found){
			movies.remove(new Integer(id));
			return true;
		} else {
			return false;
		}
	}
	
	public Map<Integer, Movie> getModel() {
		return movies;
	}
	
	public int size() {
		return movies.size();
	}
	
	public static MovieStore getInstance() {
		if (instance == null)
			instance = new MovieStore();
		return instance;
	}
}
