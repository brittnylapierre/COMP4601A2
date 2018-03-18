package edu.carleton.comp4601.dao;

import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.carleton.comp4601.models.User;

public class UserStore {
	//MongoClient mongoClient;
	//DB database;
	
	public ConcurrentHashMap<Integer, User> getUsers() {
		return users;
	}

	public void setUsers(ConcurrentHashMap<Integer, User> users) {
		this.users = users;
	}

	public static void setInstance(UserStore instance) {
		UserStore.instance = instance;
	}

	private ConcurrentHashMap<Integer, User> users;
	private static UserStore instance;
	
	public UserStore() {
		users = new ConcurrentHashMap<Integer, User>();
	}
	
	public User find(int id) {
		return users.get(new Integer(id));
	}
	
	public User find(String name) {
		int id = 0;
		boolean found = false;
		
		for(Enumeration<User> us = users.elements(); us.hasMoreElements();){
			if(us.nextElement().getName() == name){
				found = true;
			}
			id++;
		}
		
		if(found){
			return users.get(new Integer(id));
		} else {
			return null;
		}
	}
	
	
	public User createUser(String name) {
		User u = new User(name);
		
		users.put(users.size(), u);
		return u;
	}

	
	public boolean delete(int id) {
		if (find(id) != null) {
			Integer no = new Integer(id);
			users.remove(no);
			return true;
		}
		else
			return false;
	}
	
	public boolean delete(String name) {
		int id = 0;
		boolean found = false;
		
		for(Enumeration<User> us = users.elements(); us.hasMoreElements();){
			if(us.nextElement().getName() == name){
				found = true;
			}
			id++;
		}
		
		if(found){
			users.remove(new Integer(id));
			return true;
		} else {
			return false;
		}
	}
	
	public Map<Integer, User> getModel() {
		return users;
	}
	
	public int size() {
		return users.size();
	}
	
	public static UserStore getInstance() {
		if (instance == null)
			instance = new UserStore();
		return instance;
	}
	
}
