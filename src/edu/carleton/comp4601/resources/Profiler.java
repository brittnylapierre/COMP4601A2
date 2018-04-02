package edu.carleton.comp4601.resources;
/*
 * Profiles users based on the criteria:
 * # of movies per genre viewed ..., Average rating per genre.
*/


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;

import edu.carleton.comp4601.dao.MovieStore;
import edu.carleton.comp4601.dao.UserStore;
import edu.carleton.comp4601.models.Movie;
import edu.carleton.comp4601.models.Review;
import edu.carleton.comp4601.models.Sentiment;
import edu.carleton.comp4601.models.User;

public class Profiler {

	String[] genres = {"horror", "history", "romance","comedy","action","Documentary","Family","Sci-fi","Adventure","mystery"};
	
	public Profiler() {}
	
	public void profileUsers() throws FileNotFoundException{
		//TODO: change path on k machine
		//PrintWriter pw = new PrintWriter(new File("C:/Users/IBM_ADMIN/workspace/COMP4601A2/user_profiles.csv"));
        //StringBuilder sb = new StringBuilder();
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
			
			HashMap<String, Double> userData = new HashMap<String, Double>();
			for(String genre : genres){
				userData.put(genre+"-count", 0.0);
				userData.put(genre+"-agg-score", 0.0);
				userData.put(genre+"-views", 0.0);
				userData.put(genre+"-agg-sentiment", 0.0);
			}
			
			for(String movieTitle : revMovies){
				Movie movie = MovieStore.getInstance().find(movieTitle);
				//System.out.println(movieTitle);
				//System.out.println(movie);
				if(movie != null){
					Review userReview = movie.getReviews().get(name);
					String genre = movie.getGenre();
					
					if(movie.getReviews().keySet().contains(name)){
						if(userReview != null){
							Double count = userData.get(genre+"-count");//userReview
							Double aggScore = userData.get(genre+"-agg-score");//userReview
							Double aggSentiment = userData.get(genre+"-agg-sentiment");//userReview
							
							count += 1;
							
							aggScore = aggScore+userReview.getScore();
							aggSentiment = aggSentiment+userReview.getSentiment().getSentiment();
							
							userData.replace(genre+"-count", count);
							userData.replace(genre+"-agg-score", aggScore);
							userData.replace(genre+"-agg-sentiment", aggSentiment);
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
				Double aggSentiment = userData.get(genre+"-agg-sentiment");//userReview

				Double avgScore = 0.0;
				Double avgSentiment = 0.0;
				if(count > 0){
					avgScore = aggScore / count;
					avgSentiment = aggSentiment / count;
				}
				//userData.put(genre+"-avg-score", avgScore);
				userDataArray.add(count); //num reviews
				userDataArray.add(aggScore); //agg review score
				userDataArray.add(avgScore); //avg review score
				userDataArray.add(aggSentiment); //agg review sentiment
				userDataArray.add(avgSentiment); //avg review sentiment
				userDataArray.add(views); //number genre views
				
				//System.out.println(genre + " " + count + " " + aggScore + " " + avgScore + " " + views);
			}
			
			user.setDimensions(userDataArray);
			
			
			//System.out.println("User: " + name + userDataArray.toString());
			//}
			
			//sb.append(name + ",");
	        //sb.append(userDataArray.toString());
			/*int curr = 0;
	        for(Double dataPoint : userDataArray){
	        	if(curr != userDataArray.size()-1){
		        	sb.append(Double.toString(dataPoint) + ",");
	        	} else {
		        	sb.append(Double.toString(dataPoint));
	        	}
	        	curr++;
	        }*/
	        //sb.append('\n');
		}
		/*System.out.println(sb.toString());
        pw.write(sb.toString());
        pw.close();*/
		System.out.println("Done profiling.");
	}
}
