package edu.carleton.comp4601.resources;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import edu.carleton.comp4601.dao.MovieStore;
import edu.carleton.comp4601.dao.SentimentStore;
import edu.carleton.comp4601.dao.UserStore;
import edu.carleton.comp4601.models.Movie;
import edu.carleton.comp4601.models.Review;
import edu.carleton.comp4601.models.Sentiment;
import edu.carleton.comp4601.models.User;

public class DBReadWriter {
	
	public static void writeAll(){
		writeSentimentsToDB();
		writeMoviesToDB();
		writeUsersToDB();
	}
	
	public static void readAll(){
		readSentimentsFromDB();
		readMoviesFromDB();
		readUsersFromDB();
	}
	
	public static void writeMoviesToDB(){
		try {
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			DB database = mongoClient.getDB("recommendDB");
			DBCollection movieCollection = database.getCollection("movies");
			movieCollection.setObjectClass(Movie.class);
			for(Enumeration<Movie> m = MovieStore.getInstance().getMovies().elements(); m.hasMoreElements();){
				Movie movie  = m.nextElement();
				BasicDBObject newDocument = new BasicDBObject();
				newDocument.put("genre", movie.getGenre());
				newDocument.put("title", movie.getTitle());
				BasicDBList access = new BasicDBList();
				for(String a :  movie.getUsersAccessed()){
					access.add(a);
				}
				newDocument.put("usersAccessed", access);
			    BasicDBList reviews = new BasicDBList();
				for(Map.Entry<String, Review> r : movie.getReviews().entrySet()){
					BasicDBObject rDocO = new BasicDBObject();
					BasicDBObject rDocI = new BasicDBObject();
					rDocI.put("score", r.getValue().getScore());
					rDocI.put("review", r.getValue().getReview());
					BasicDBObject sDoc = new BasicDBObject();
					sDoc.put("sentiment", r.getValue().getSentiment().getSentiment());
					sDoc.put("userMovieIds", r.getValue().getSentiment().getUserMovieIds());
					rDocI.put("sentiment", sDoc);
					rDocO.put(r.getKey(), rDocI);
					reviews.add(rDocO);
				}
				newDocument.put("reviews", reviews);
				movieCollection.insert(newDocument);
			}
			mongoClient.close();
			System.out.println("Added movies to db!");
		}
		catch (Exception e) {
			System.out.println("Error adding movies to db");	
		}
	}
	
	public static void writeUsersToDB(){
		try {
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			DB database = mongoClient.getDB("recommendDB");
			DBCollection userCollection = database.getCollection("users");
			userCollection.setObjectClass(User.class);
			for(Enumeration<User> u = UserStore.getInstance().getUsers().elements(); u.hasMoreElements();){
				User user  = u.nextElement();
				BasicDBObject newDocument = new BasicDBObject();
				newDocument.put("name", user.getName());
				BasicDBList dims = new BasicDBList();
				for(Double dim : user.getDimensions()){
					dims.add(dim);
				}
				newDocument.put("dimensions", dims);
				newDocument.put("reviewedMovies", user.getReviewedMovies());
				newDocument.put("community", user.getCommunity());
				userCollection.insert(newDocument);
			}
			mongoClient.close();
			System.out.println("Added users to db!");
		}
		catch (Exception e) {
			System.out.println("Error adding users to db");	
		}
	}
	
	public static void writeSentimentsToDB(){
		try {
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			DB database = mongoClient.getDB("recommendDB");
			DBCollection sentimentCollection = database.getCollection("sentiments");
			sentimentCollection.setObjectClass(Sentiment.class);
			for(Enumeration<Sentiment> s = SentimentStore.getInstance().getSentiments().elements(); s.hasMoreElements();){
				Sentiment sentiment  = s.nextElement();
				BasicDBObject newDocument = new BasicDBObject();
				newDocument.put("sentiment", sentiment.getSentiment());
				newDocument.put("userMovieIds", sentiment.getUserMovieIds());
				sentimentCollection.insert(newDocument);
			}
			mongoClient.close();
			System.out.println("Added sentiments to db!");
		}
		catch (Exception e) {
			System.out.println("Error adding sentiments to db");	
		}
	}
	
	public static void readMoviesFromDB(){
		try {
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			DB database = mongoClient.getDB("recommendDB");
			DBCollection movieCollection = database.getCollection("movies");
			DBCursor cursor = movieCollection.find();//.sort(new BasicDBObject("_id", -1));
			while(cursor.hasNext()){
				BasicDBObject b = (BasicDBObject) cursor.next();
				String title = b.getString("title");
				String genre = b.getString("genre");
				ArrayList<String> usersAccessed = (ArrayList<String>) b.get("usersAccessed");
				HashMap<String, Review> reviews = new HashMap<String, Review>();
				List<BasicDBObject> rs = (List<BasicDBObject>) b.get("reviews");//
				for(BasicDBObject r : rs){
					for(String k : r.keySet()){
						BasicDBObject rBDBObj = (BasicDBObject) r.get(k);
						
						BasicDBObject sBDBObj = (BasicDBObject) rBDBObj.get("sentiment");
						String userMovieIds = sBDBObj.getString("userMovieIds");
						Double sentiment = sBDBObj.getDouble("sentiment");
						Sentiment s = new Sentiment(userMovieIds, sentiment);

						Double score = rBDBObj.getDouble("score");
						String review = rBDBObj.getString("sentiment");
						Review nr = new Review(score, review, s);
						reviews.put(k, nr);
					}
				}
				Movie m = new Movie(title, genre);
				m.setUsersAccessed(usersAccessed);
				m.setReviews(reviews);
			 	MovieStore.getInstance().add(m);
			}
		    mongoClient.close();
		    System.out.println("Done reading in movies.");
		} catch (Exception e) {
		    System.out.println("Error reading in movies.");
		}
	}
	
	public static void readUsersFromDB(){
		try {
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			DB database = mongoClient.getDB("recommendDB");
			DBCollection userCollection = database.getCollection("users");
			DBCursor cursor = userCollection.find();
			while(cursor.hasNext()){
				BasicDBObject b = (BasicDBObject) cursor.next();
				String name = b.getString("name");
				User u = new User(name);
				ArrayList<Double> dimensions = (ArrayList<Double>) b.get("dimensions");
				ArrayList<String> reviewedMovies = (ArrayList<String>) b.get("reviewedMovies");//new ArrayList<String>();
				int community = b.getInt("community");
				u.setDimensions(dimensions);
				u.setReviewedMovies(reviewedMovies);
				u.setCommunity(community);
				UserStore.getInstance().add(u);
			}
		    mongoClient.close();
		    System.out.println("Done reading in users.");
		} catch (Exception e) {
		    System.out.println("Error reading in users." + e);
		}
	}

	public static void  readSentimentsFromDB(){
		try {
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			DB database = mongoClient.getDB("recommendDB");
			DBCollection sentimentCollection = database.getCollection("sentiments");
			DBCursor cursor = sentimentCollection.find();//.sort(new BasicDBObject("_id", -1)).limit(1);
			while(cursor.hasNext()){
				BasicDBObject b = (BasicDBObject) cursor.next();
				String userMovieIds = b.getString("userMovieIds");
				Double sentiment = b.getDouble("sentiment");
				Sentiment s = new Sentiment(userMovieIds, sentiment);
			 	SentimentStore.getInstance().add(s);
			}
		    mongoClient.close();
		    System.out.println("Done reading in sentiments.");
		} catch (Exception e) {
		    System.out.println("Error reading in sentiments.");
		    System.out.println(e);
		}
	}
}
