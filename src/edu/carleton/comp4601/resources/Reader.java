package edu.carleton.comp4601.resources;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import edu.carleton.comp4601.dao.SentimentStore;
import edu.carleton.comp4601.dao.UserStore;
import edu.carleton.comp4601.models.Movie;
import edu.carleton.comp4601.models.Review;
import edu.carleton.comp4601.models.Sentiment;
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

		//Testing correct
		/*File rdir = new File("C:/Users/IBM_ADMIN/workspace/COMP4601A2/resources/reviews");
		File[] rdirectoryListing = rdir.listFiles();
		int count = 0;
		String prevName = "";
		if (rdirectoryListing != null) {
			for (File f : rdirectoryListing) {
				String[] fnamearr = f.getName().split("-");
				if(fnamearr.length > 0){
					String name = fnamearr[0];
					if(!name.equals(prevName)){
						System.out.println("\nUser: " + name+ "\n");
						count++;
					}
					prevName = name;
				}
			}
		}
		
		System.out.println("Count of users who have reviewed: " + count);*/
		
		//TODO: change on k machine
		File dir = new File("C:/Users/IBM_ADMIN/workspace/COMP4601A2/resources/pages");
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File f : directoryListing) {
				String genre = genres[random.nextInt(2 - 0 + 1) + 0];
				if(f.canRead()){
					Document doc = Jsoup.parse(f, "UTF-8");
					Element title = doc.select("title").first();

					HashMap<String, Review> reviewMap = new HashMap<String, Review>();
					Elements elements = doc.body().select("*");

					String review = "";
					String prevUser = "";
					//meta name="score"
					boolean first = true;
					File userDetailedReviewFile;
					//CHECK IF SENTIMENT
					//CHECK REVIEWS 
					ArrayList<String> usersAccessed = new ArrayList<String>();
					for (Element element : elements) {
						if(element.tagName().equals("a")){
							//new user
							if(!first){
								Sentiment s = SentimentStore.getInstance().find(prevUser + "-" + title.ownText());
								if(s == null){
									s = new Sentiment(prevUser + "-" + title.ownText() + ".html", 0); //neutral default
								}
								Review r = new Review(0, review, s);
								userDetailedReviewFile = new File("C:/Users/IBM_ADMIN/workspace/COMP4601A2/resources/reviews/"+prevUser + "-" + title.ownText() +".html");
								//System.out.println("C:/Users/IBM_ADMIN/workspace/COMP4601A2/resources/reviews/"+prevUser + "-" + title +".html");
								if(userDetailedReviewFile.canRead()){
									Document userDetailedReviewDoc = Jsoup.parse(userDetailedReviewFile, "UTF-8");
									//System.out.println(userDetailedReviewDoc.html());

									Elements metaTags = userDetailedReviewDoc.getElementsByTag("meta");
									
									for (Element metaTag : metaTags) {
										  String name = metaTag.attr("name");
										  String content = metaTag.attr("content");
										  if(name.equals("score")){
											  r.setScore(Double.parseDouble(content));
											  reviewMap.put(prevUser, r);
										  }
									}
								}
								//usersAccessed.add(prevUser);
							} else {
								first = false;
							}
							prevUser = element.ownText();
							//System.out.println("usr2: " + prevUser);
							review = "";
						} else if (element.tagName().equals("p")){
							review += element.ownText();
						}
					}
					
					
					//System.out.println("title: " + title.ownText());
					Movie m = MovieStore.getInstance().createMovie(title.ownText(), genre);
					m.setReviews(reviewMap);
					//m.setUsersAccessed(usersAccessed);
				}
		    }
		} else {
			System.out.println("error getting movies");
		}

		System.out.println("Done getting movies...");
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
		
		System.out.println("Done getting users...");

	}
	

	public void readSentiments() throws IOException{
		File csv = new File("C:/Users/IBM_ADMIN/workspace/COMP4601A2/resources/sentiment-reviews-individual.csv");
		BufferedReader reader = new BufferedReader(new FileReader(csv));
		String line = null;
		int lineNum = 0;
		while((line = reader.readLine()) != null){
			if(lineNum > 0){
				String[] cols = line.split(",");
				if(cols.length >= 6){
					String movieUserId = cols[0];
					int[] data = { 
							Integer.parseInt(cols[1]), 
							Integer.parseInt(cols[2]), 
							Integer.parseInt(cols[3]), 
							Integer.parseInt(cols[4]), 
							Integer.parseInt(cols[5])
						};
					SentimentStore.getInstance().createSentiment(movieUserId, data);
				}
			}
			lineNum++;
		}
		reader.close();
		System.out.print("Done getting sentiments...");
	}

}
