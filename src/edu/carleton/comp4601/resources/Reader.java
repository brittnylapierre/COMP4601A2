package edu.carleton.comp4601.resources;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.carleton.comp4601.dao.MovieStore;
import edu.carleton.comp4601.dao.UserStore;
import edu.carleton.comp4601.models.Movie;
import edu.carleton.comp4601.models.User;

//Reads in the users from the user html pages
//Reads in the movies from the movies pages, and tags them with a genre
public class Reader {
	
	public Reader(){
		System.out.println("initialized reader...");
	}
	
	Random random = new Random();  //for now
	String[] genres = {"horror", "action", "romance"};
	
	public void readMovies() throws IOException{
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
		String genre = genres[random.nextInt(2 - 0 + 1) + 0];

		//TODO: change on k machine
		File dir = new File("C:/Users/IBM_ADMIN/workspace/COMP4601A2/resources/pages");
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File f : directoryListing) {
				if(f.canRead()){
					Document doc = Jsoup.parse(f, "UTF-8");
					Element title = doc.select("title").first();

					HashMap<String, String> reviewMap = new HashMap<String, String>();
					Elements elements = doc.body().select("*");

					String review = "";
					String prevUser = "";
					boolean first = true;
					for (Element element : elements) {
						if(element.tagName() == "a"){
							//new user
							if(!first){
								System.out.println("Adding user: " + prevUser + " review: " + review);
								reviewMap.put(prevUser, review);
							} else {
								first = false;
							}
							prevUser = element.ownText();
							review = "";
						} else if (element.tagName() == "p"){
							review += element.ownText();
						}
					    System.out.println(element.ownText());
					}
					
					Movie m = MovieStore.getInstance().createMovie(title.text(), genre);
					m.setReviews(reviewMap);
				}
		    }
		} else {
			System.out.println("error getting movies");
		}
	}
	
	
	public void readUsers() throws IOException{
		/*
		 * <html><head><title>A1A69DJ2KPU4CH</title></head>
		 * <body><a href="../pages/B00112S8RS.html">B00112S8RS</a><br/>
		 * ... all the reviews</body>
		 * */
		//TODO: change on k machine
		File dir = new File("C:/Users/IBM_ADMIN/workspace/COMP4601A2/resources/users");
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File f : directoryListing) {
				if(f.canRead()){
					Document doc = Jsoup.parse(f, "UTF-8");
					Element name = doc.select("title").first();
					
					String userName = name.text();
					
					Elements reviewedPages = doc.select("a");
					List<String> reviewTexts = reviewedPages.eachText();
					ArrayList<String> reviewedMovies = new ArrayList<String>();
					for(String moviePage : reviewTexts){
						reviewedMovies.add(moviePage);
					}
					
					User u = UserStore.getInstance().createUser(userName);
					u.setReviewedMovies(reviewedMovies);
				}
			}
		} else {
			System.out.println("error getting users");
		}

	}

}
