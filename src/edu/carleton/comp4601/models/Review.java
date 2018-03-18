package edu.carleton.comp4601.models;

public class Review {
	double score;
	String review;
	
	public Review(double score, String review){
		this.score = score;
		this.review = review;
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
}
