package edu.carleton.comp4601.models;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;

import com.mongodb.BasicDBObject;

import sun.misc.BASE64Encoder;

public class Movie extends BasicDBObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3292716895447573617L;
	String genre;
	String title;
	//<uname, review>
	HashMap<String, Review> reviews = new HashMap<String, Review>();
	ArrayList<String> usersAccessed = new ArrayList<String>();
	//HashMap<String, Sentiment> sentiments = new HashMap<String, Sentiment>();
	//a shows who, then p shows review.

	public Movie(String title, String genre) {
		this.genre = genre;
		this.title = title;
	}
	
	public String getGenre() {
		return genre;
	}


	public void setGenre(String genre) {
		this.genre = genre;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public HashMap<String, Review> getReviews() {
		return reviews;
	}
	
	public void setReviews(HashMap<String, Review> reviews) {
		this.reviews = reviews;
	}
	
	
	public ArrayList<String> getUsersAccessed() {
		return usersAccessed;
	}

	public void setUsersAccessed(ArrayList<String> usersAccessed) {
		this.usersAccessed = usersAccessed;
	}
	
	public String grabMovieAdd(){
		try {
			String path = "C:/Users/IBM_ADMIN/workspace/COMP4601A2/adds/"+ this.genre;
			File dir = new File(path);
			File[] directoryListing = dir.listFiles();
			if (directoryListing != null) {
				Random rand = new Random();
				int fileIndex = rand.nextInt((directoryListing.length-1) - 0 + 1); //(max - min) + 1) + min;
				if(fileIndex >= 0 && fileIndex < directoryListing.length){
					File imageFile = directoryListing[fileIndex];
					if(imageFile.canRead()){
						BufferedImage image = ImageIO.read(imageFile);

						ByteArrayOutputStream bos = new ByteArrayOutputStream();
			        
						ImageIO.write(image, "png", bos);
				        byte[] imageBytes = bos.toByteArray();
				        
				        BASE64Encoder encoder = new BASE64Encoder();
			            String imageString = encoder.encode(imageBytes);
			            return "<img width='200' title='Because of you interest in "+this.genre+"' src='data:image/png;base64, "+imageString+"'/>";
					}
				}
			}
	        return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	
}
