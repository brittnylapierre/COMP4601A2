package edu.carleton.comp4601.resources;


import java.util.ArrayList;
import java.util.Random;


import edu.carleton.comp4601.dao.MovieStore;
import edu.carleton.comp4601.dao.UserStore;
import edu.carleton.comp4601.models.Movie;
import edu.carleton.comp4601.models.User;

//Reads in the users from the user html pages
//Reads in the movies from the movies pages, and tags them with a genre
public class Reader {
	
	Random random = new Random();  //for now
	String[] genres = {"horror", "action", "romance"};
	
	public void readMovies(){
		/*
		 * <head><title>078062565X</title></head>
		 * <body><a href="../users/A29QA79VLQGHY6.html">A29QA79VLQGHY6</a><br/>
		 * <p>Ray Harryhausen never forgot...</p>
		 * 
		 * ...
		 * 
		 * </body>
		 * */
		//genres

		//
		String title = "";
		String genre = genres[random.nextInt(2 - 0 + 1) + 0];

		Movie m = MovieStore.getInstance().createMovie(title, genre);
	}
	
	
	public void readUsers(){
		/*
		 * 
		 * */

		String name = "";

		User m = UserStore.getInstance().createUser(name);
	}

}
