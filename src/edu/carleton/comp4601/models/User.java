package edu.carleton.comp4601.models;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;

import javax.imageio.ImageIO;

import com.mongodb.BasicDBObject;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

public class User extends BasicDBObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3208707355807340412L;
	String	name;
	ArrayList<Double> dimensions = new ArrayList<Double>();
	ArrayList<String> reviewedMovies = new ArrayList<String>();
	int community;

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
	
	public ArrayList<String> getReviewedMovies() {
		return reviewedMovies;
	}

	public void setReviewedMovies(ArrayList<String> reviewedMovies) {
		this.reviewedMovies = reviewedMovies;
	}
	
	public int getCommunity() {
		return community;
	}

	public void setCommunity(int community) {
		this.community = community;
	}
	
	public String grabUserAdd(){
		try {
			String path = "C:/Users/IBM_ADMIN/workspace/COMP4601A2/adds/"+ (this.community + 1);
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
			            return "<img width='200' title='Based on your profile you might enjoy this' src='data:image/png;base64, "+imageString+"'/>";
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
