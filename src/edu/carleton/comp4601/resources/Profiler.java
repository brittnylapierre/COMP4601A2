package edu.carleton.comp4601.resources;
/*
 * Profiles users based on the criteria:
 * # of movies per genre viewed ..., Average rating per genre.
*/


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import edu.carleton.comp4601.dao.MovieStore;
import edu.carleton.comp4601.dao.UserStore;
import edu.carleton.comp4601.models.Movie;
import edu.carleton.comp4601.models.Review;
import edu.carleton.comp4601.models.User;

public class Profiler {

	String[] genres = {"horror", "action", "romance"};
	
	public Profiler() {}
	
	public void profileUsers(){
		//ArrayList<Double> dimensions = new ArrayList<Double>();
		//[numhorror, avghorror, numaction, avgaction, numromance, avggromance]
		
		/* for all users
		 * 	for all reviewed movies
		 *   find user's review in movie reviews list
		 *   check genre
		 *   keep track of aggregate rating score for the movies of this genre
		 *   keep track of # of movies of this genre reviewed/viewed
		 *   
		 *   for genre in genre array, grap the data form the hashmap for the user
		 * */
		
		for(Enumeration<User> us = UserStore.getInstance().getUsers().elements(); us.hasMoreElements();){
			User user  = us.nextElement();
			ArrayList<String> revMovies = user.getReviewedMovies();

			String name = user.getName();
			//if(name.equals("A1EGDRTJYF3AZK")){
				
			
			//System.out.print("USER " +  name);
			//System.out.print("\n");
			
			HashMap<String, Double> userData = new HashMap<String, Double>();
			for(String genre : genres){
				userData.put(genre+"-count", 0.0);
				userData.put(genre+"-agg-score", 0.0);
				userData.put(genre+"-views", 0.0);
			}
			
			for(String movieTitle : revMovies){
				Movie movie = MovieStore.getInstance().find(movieTitle);
				//System.out.println(movieTitle);
				//System.out.println(movie);
				if(movie != null){
					Review userReview = movie.getReviews().get(name);
					String genre = movie.getGenre();
					

					//System.out.print("/nusers: " +  movie.getReviews().keySet().toString());
					
					if(movie.getReviews().keySet().contains(name)){
						
						//System.out.println("/n Movie" + movieTitle + " Genre " + genre + " num reviews " + movie.getReviews().size() + " user: " + name + " ur "  + userReview);
						
						
						if(userReview != null){
							Double count = userData.get(genre+"-count");//userReview
							Double aggScore = userData.get(genre+"-agg-score");//userReview
							
							count += 1;
							
							aggScore = aggScore+userReview.getScore();
							
							//System.out.println(genre + " " + count + " " + aggScore);
							
							userData.replace(genre+"-count", count);
							userData.replace(genre+"-agg-score", aggScore);
						}
					}
					
					Double views = userData.get(genre+"-views");//userReview
					views += 1;
					userData.replace(genre+"-views", views);
					
				}
			}
			
			ArrayList<Double> userDataArray = new ArrayList<Double>();
			
			for(String genre : genres){
				Double count = userData.get(genre+"-count");//userReview
				Double aggScore = userData.get(genre+"-agg-score");//userReview
				Double views = userData.get(genre+"-views");//userReview

				Double avgScore = 0.0;
				if(count > 0){
					avgScore = aggScore / count;
				}
				//userData.put(genre+"-avg-score", avgScore);
				userDataArray.add(count);
				userDataArray.add(aggScore);
				userDataArray.add(avgScore);
				userDataArray.add(views);
				
				//System.out.println(genre + " " + count + " " + aggScore + " " + avgScore + " " + views);
			}
			
			user.setDimensions(userDataArray);
			
			System.out.println("User: " + name + userDataArray.toString());
			//}
		}
	}
}
