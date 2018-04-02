package edu.carleton.comp4601.resources;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import edu.carleton.comp4601.models.User;





//Creates communities from profiled users
/*
 * {	
		initialize	population;	
		evaluate	population;	
		while	TerminationCriteriaNotSatsfied
		{	
			select parents for reproduction;	
			perform	recombination and mutation;	
			evaluate population;	
		}	
	}	
	
	use jgap https://karczmarczuk.users.greyc.fr/TEACH/IAD/java/jgap_tutorial.html
 * */
public class Communitizer {
	
	private int no_clusters = 5;
	private int no_features=20;
	private int no_users;
	private KmeansUser[] users;
	private boolean changed;
	
	
	public Communitizer(int no_users) {
		super();
		this.no_users = no_users;
		this.users= new KmeansUser[no_users];
	}

	private KmeansUser createCenter(int v){
		KmeansUser u=new KmeansUser("center",v,this.no_clusters);
		for (int j = 0; j < v; j++) {//accounts for total reviews in genre average rating and average sentiment 
			int top=1;
			int bottom=1;
//			if(j%3==0){
//				top=2;
//				bottom=0;
//			}else 
				if(j%2==0){
				top=5;
				bottom=0;
			}else{
				top=1;
				bottom=-1;
				
			}
			double random = ThreadLocalRandom.current().nextDouble(bottom, top);
			u.features[j]= random;
		}
		
		return u;		
	}
	
	private KmeansUser moveCenter(int v,KmeansUser u){
		//User u=new User("center",v,2);
		double[] totals= new double[this.no_features];
		int count=0;
		for(KmeansUser ur :users){
			if(ur.cluster==v){
				count++;
				for (int j = 0; j < no_features; j++) {
					totals[j]+=ur.features[j] ;
				}
			}
		}
		if(count!=0){
		for (int j = 0; j < no_features; j++) {
			u.features[j]=totals[j]/count ;
		}
		}
		
		return u;		
	}
	public void addUser(User u,int i){
		users[i]=new KmeansUser(u);
		System.out.println("Added user");
	}
	public int getCommuityForUser(int i){
		return users[i].cluster;
	}
	public void algorithm() {

		
		int k=5;
		KmeansUser[]centers = new KmeansUser[this.no_clusters];
		centers[0]=null;
		int itterations=0;
		changed=true;
		System.out.println("in the algorithm");
		for(int i=0;i<this.no_clusters;i++){
			centers[i]=createCenter(this.no_features);
		}
		while (changed) {
			System.out.println("*******************************"+itterations+"**********");
			changed=false;
			itterations++;
				for(int i=0;i<this.no_clusters;i++){
					centers[i]=this.moveCenter(i, centers[i]);
				}
			// Your code here
			//change centers
			for(KmeansUser u :users){
				double min=10.0;
				int minIndex=0;
				for(int i=0;i<this.no_clusters;i++){
					double d = distance(u,centers[i]);
					u.distance[i]=d;
					if(d<min){
						min=d;
						minIndex=i;
					}
					
				}
				u.update();
				u.cluster=minIndex;
				if(u.changed()){
					changed=true;
				}
			}	
			for(int i=0;i<this.no_clusters;i++){
				int count=0;
				for(KmeansUser u :users){
					if(u.cluster==i){
						count++;
					}
					//System.out.println(u.distance[i]);
				}
				
				System.out.println("cluster "+i+"has: "+count+" points");
			}
		}
		
		for(int i=0;i<this.no_clusters;i++){
			int count=0;
			for(KmeansUser u :users){
				if(u.cluster==i){
					count++;
				}
				//System.out.println(u.distance[i]);
			}
			
			System.out.println("cluster "+i+"has: "+count+" points");
		}
		
	}
	private double getDistances(){
		double total=0.0;
		for(KmeansUser u:users){
			total+=u.distance[u.cluster];
		}
		return total;
	}
	
	/* 
	 * Computes distance between two users
	 * Could implement this on User too.
	 */
	private double distance(KmeansUser a, KmeansUser b) {
		double rtn = 0.0;
		// Assumes a and b have same number of features
		for (int i = 0; i < a.features.length; i++) {
			rtn += (a.features[i] - b.features[i])
					* (a.features[i] - b.features[i]);
		}
		return Math.sqrt(rtn);
	}
	private class KmeansUser {
		public double[] features;
		public double[] distance;
		public String name;
		public int cluster;
		public int last_cluster;

		public KmeansUser(String name, int noFeatures, int noClusters) {
			this.name = name;
			this.features = new double[no_features];
			this.distance = new double[no_clusters];
			this.cluster = -1;
			this.last_cluster = -2;
		}
		//make kmeans user with normal user
		public KmeansUser(edu.carleton.comp4601.models.User u) {
			this.name = u.getName();
			this.features = new double[no_features];
			this.distance = new double[no_clusters];
			this.cluster = -1;
			this.last_cluster = -2;
			ArrayList<Double> d = u.getDimensions();
			int j=0;
			int k=1;
			for(int i=2;i<d.size();i+=2){
				if(k%3!=0){
				this.features[j]=d.get(i);
				j++;
				}
				k++;
			}
		}

		// Check if cluster association has changed.
		public boolean changed() {
			return last_cluster != cluster;
		}
		
		// Update the saved cluster from iteration to iteration
		public void update() {
			last_cluster = cluster;
		}

		public String toString() {
			StringBuffer b = new StringBuffer(name);
			for (int i = 0; i < features.length; i++) {
				b.append(' ');
				b.append(features[i]);
			}
			return b.toString();
		}
	}

}

