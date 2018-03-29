package edu.carleton.comp4601.models;

public class Review {
	double score;
	String review;
	Sentiment sentiment;

	public Review(double score, String review, Sentiment sentiment){
		this.score = score;
		this.review = review;
		this.sentiment = sentiment;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}
	
	public Sentiment getSentiment() {
		return sentiment;
	}

	public void setSentiment(Sentiment sentiment) {
		this.sentiment = sentiment;
	}
}
