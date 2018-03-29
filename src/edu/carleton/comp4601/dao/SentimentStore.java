package edu.carleton.comp4601.dao;

import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.carleton.comp4601.models.Sentiment;

public class SentimentStore {
	public ConcurrentHashMap<Integer, Sentiment> getSentiments() {
		return sentiments;
	}

	public void setSentiments(ConcurrentHashMap<Integer, Sentiment> sentiments) {
		this.sentiments = sentiments;
	}

	public static void setInstance(SentimentStore instance) {
		SentimentStore.instance = instance;
	}

	private ConcurrentHashMap<Integer, Sentiment> sentiments;
	private static SentimentStore instance;
	
	public SentimentStore() {
		sentiments = new ConcurrentHashMap<Integer, Sentiment>();
	}
	
	
	public Sentiment find(String movieUserId) {
		for(Enumeration<Sentiment> ms = sentiments.elements(); ms.hasMoreElements();){
			Sentiment s = ms.nextElement();
			if(s.getUserMovieIds().contains(movieUserId)){
				return s;
			}
		}
		return null;
	}
	
	public Sentiment find(int id) {
		return sentiments.get(new Integer(id));
	}
	
	public Sentiment createSentiment(String userMovieIds, int[] numbers) {
		Sentiment s = new Sentiment(userMovieIds, numbers);
		sentiments.put(sentiments.size(), s);
		return s;
	}
	
	public Sentiment createSentiment(String userMovieIds, double sentiment) {
		Sentiment s = new Sentiment(userMovieIds, sentiment);
		sentiments.put(sentiments.size(), s);
		return s;
	}

	
	public boolean delete(int id) {
		if (find(id) != null) {
			Integer no = new Integer(id);
			sentiments.remove(no);
			return true;
		}
		else
			return false;
	}
	
	public boolean delete(String movieOrUserId) {
		int id = 0;
		boolean found = false;
		
		for(Enumeration<Sentiment> s = sentiments.elements(); s.hasMoreElements();){
			if(s.nextElement().getUserMovieIds().contains(movieOrUserId)){
				found = true;
			}
			id++;
		}
		
		if(found){
			sentiments.remove(new Integer(id));
			return true;
		} else {
			return false;
		}
	}
	
	public Map<Integer, Sentiment> getModel() {
		return sentiments;
	}
	
	public int size() {
		return sentiments.size();
	}
	
	public static SentimentStore getInstance() {
		if (instance == null)
			instance = new SentimentStore();
		return instance;
	}
}
