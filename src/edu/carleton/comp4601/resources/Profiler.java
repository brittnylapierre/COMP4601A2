package edu.carleton.comp4601.resources;
/*
 * Profiles users based on the criteria:
 * # of movies per genre viewed ..., Average rating per genre.
*/


import java.util.HashMap;

import edu.carleton.comp4601.dao.MovieStore;
import edu.carleton.comp4601.dao.UserStore;
import edu.carleton.comp4601.models.Movie;
import edu.carleton.comp4601.models.User;

public class Profiler {

	String[] genres = {"horror", "action", "romance"};
	
	public Profiler() {}
	
	public void profileUsers(){
		for(User user : UserStore.getInstance().getUsers().values()){
			//ArrayList<Double> dimensions = new ArrayList<Double>();
			//[numhorror, avghorror, numaction, avgaction, numromance, avggromance]
			
			/* for all users
			 * 	for all reviewed movies
			 *   find user's review in movie reviews list
			 *   check genre
			 *   keep track of aggregate rating score
			 *   keep track of # of movies of this genre reviewed
			 * */
		}
	}
}
