package edu.carleton.comp4601.models;

import com.mongodb.BasicDBObject;

public class Sentiment extends BasicDBObject  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8287894178721567230L;
	// [1, 0.5, 0, -0.5, 1]
	/* Multiply these values by the numbers in the sentiment row, 
	then add them and see how close it is to the original total for 
	a scale on sentiment*/
	int[] numbers;
	double sentiment;
	String userMovieIds;

	public Sentiment(String userMovieIds, int[] numbers){
		this.userMovieIds = userMovieIds;
		this.sentiment = this.createSentimentFromArray(numbers);
	}
	
	public Sentiment(String userMovieIds, double sentiment){ //useful if no sentiments found.
		this.userMovieIds = userMovieIds;
		this.sentiment = sentiment;
	}

	public double createSentimentFromArray(int[] numbers){ //always len 5 if correct
		double sentiment = 0; //neutral is zero
		int total = numbers[0] + numbers[1] + numbers[2] + numbers[3] + numbers[4];
		if(numbers.length >= 5){
			sentiment += numbers[0] * 1; //v. positive
			sentiment += numbers[1] * 0.5; //positive
			sentiment += numbers[2] * 0; //neutral
			sentiment += numbers[3] * -0.5; //negative
			sentiment += numbers[4] * -1; //v.negative
		}
		if(total > 0){
			sentiment = sentiment / total;
		}
		return sentiment;
	}	
	
	public int[] getNumbers() {
		return numbers;
	}


	public void setNumbers(int[] numbers) {
		this.numbers = numbers;
	}


	public double getSentiment() {
		return sentiment;
	}

	public void setSentiment(double sentiment) {
		this.sentiment = sentiment;
	}
	
	public String getUserMovieIds() {
		return userMovieIds;
	}

	public void setUserMovieIds(String userMovieIds) {
		this.userMovieIds = userMovieIds;
	}
}
