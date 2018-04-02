package edu.carleton.comp4601.resources;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import edu.carleton.comp4601.dao.MovieStore;
import edu.carleton.comp4601.dao.UserStore;
import edu.carleton.comp4601.models.Movie;
import edu.carleton.comp4601.models.User;
import sun.misc.BASE64Encoder;

@Path("/rs")
public class Recommender {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
		@Context
		UriInfo uriInfo;
		@Context
		Request request;

		private String name;
		private String ROOT = "C:/Users/IBM_ADMIN/workspace/COMP4601A2/";
		//String ROOT= "/Users/kellymaclauchlan/code/mobile/a2/COMP4601A2/";
		String[] genres = {"horror", "history", "romance","comedy","action","Documentary","Family","Sci-fi","Adventure","mystery"};
		

		public Recommender() {
			name = "Brittny and Kelly RS";
		}
		
		@GET
		@Produces(MediaType.TEXT_HTML)
		public String sayHtml() {
			return "<html> " + "<title>" + name + "</title>" + "<body><h1>" + name
					+ "</h1></body>" + "</html> ";
		}
		
		/*
		 * You are to implement a RESTful web service /reset/{dir} using GET that initializes your system. 
		 * This service should then allow the web services in (8)-(12) below to run. This service is a testing 
		 * convenience only. Here, dir represents the directory being used as the root directory for access to 
		 * web pages and users. This will be used during testing to access testing data (the data referenced here 
		 * is training data). For example, while training the value of dir will be "training" reflecting the use 
		 * of the data in the comp4601/assignments/training directory; however, a tester might choose dir = "testing" 
		 * to access data in the comp4601/assignments/testing/pages and comp4601/assignments/testing/users directories.
		 * */
		
		@Path("reset/{dir}")
		@GET
		@Produces(MediaType.TEXT_HTML)
		public String reset(@PathParam("dir") String dir) {
			Reader reader = new Reader();
			try {
				if(dir.equals("testing")){
					reader.readSentiments();
					reader.readMovies();
					reader.readUsers();
				} else if(dir.equals("pages")) {
					reader.readMovies();
				} else if(dir.equals("sentiments")) {
					reader.readSentiments();
				} else if(dir.equals("users")) {
					reader.readUsers();
				}
				System.out.println("Done reading.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return "<html> " + "<title>" + name + " reset</title>" + "<body><h1>" + name
						+ " failed</h1></body>" + "</html> ";
			}
			
			return "<html> " + "<title>" + name + " reset</title>" + "<body><h1>" + name
					+ " reset success</h1></body>" + "</html> ";
		}
		
		
		@Path("resetdb/{dir}")
		@GET
		@Produces(MediaType.TEXT_HTML)
		public String resetDB(@PathParam("dir") String dir) {
				if(dir.equals("testing")){
					DBReadWriter.readSentimentsFromDB();
					DBReadWriter.readMoviesFromDB();
					DBReadWriter.readUsersFromDB();
				} else if(dir.equals("pages")) {
					DBReadWriter.readMoviesFromDB();
				} else if(dir.equals("sentiments")) {
					DBReadWriter.readSentimentsFromDB();
				} else if(dir.equals("users")) {
					DBReadWriter.readUsersFromDB();
				}
				System.out.println("Done reading.");
				return "<html> " + "<title>" + name + " reset</title>" + "<body><h1>" + name
						+ " done reading in from db</h1></body>" + "</html> ";
		}
		
		 /* CREATES PROFILE DATA FOR USERS
		 * You are to implement a RESTful web service /context using GET that analyzes the web pages and returns an 
		 * HTML representation of the profiles for users. The HTML returned must clearly show what features are 
		 * being used as part of the profile for a user. It is expected that the response would contain an HTML table 
		 * with one row containing information for a user.
		 * */
		
		@Path("context")
		@GET
		@Produces(MediaType.TEXT_HTML)
		public String context() {
			Profiler profiler = new Profiler();
			
			try {
				profiler.profileUsers();

				String profileTableString = "<table style=\"border: 1px solid grey;border-collapse: collapse;\">";
				profileTableString += "<th style=\"border: 1px solid grey;\">user</th>";
				for(String genre : genres){
					profileTableString += "<th style=\"border: 1px solid grey;\">" + genre + " count</th>";
					profileTableString += "<th style=\"border: 1px solid grey;\">" + genre + " aggregate score</th>";
					profileTableString += "<th style=\"border: 1px solid grey;\">" + genre + " average count</th>";
					profileTableString += "<th style=\"border: 1px solid grey;\">" + genre + " aggregate sentiment</th>";
					profileTableString += "<th style=\"border: 1px solid grey;\">" + genre + " average sentiment</th>";
					profileTableString += "<th style=\"border: 1px solid grey;\">" + genre + " movie page views</th>";
					
				}
				profileTableString += "</tr>";
				
				for(Enumeration<User> us = UserStore.getInstance().getUsers().elements(); us.hasMoreElements();){
					User user  = us.nextElement();
					profileTableString += "<tr>";
					profileTableString += "<td style=\"border: 1px solid grey;\">" + user.getName() + "</td>";
					for(Double dataPoint : user.getDimensions()){
						profileTableString += "<td style=\"border: 1px solid grey;\">" + Double.toString(dataPoint) + "</td>";
					}
					profileTableString += "</tr>";
				}
				profileTableString += "</table>";
				System.out.println(profileTableString);
				
				return "<html> " + "<title>" + name + " profiles set</title>" + "<body><h1>" + name
						+ " profiles set</h1> "+profileTableString+" </body>" + "</html> ";
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return "<html> " + "<title>" + name + " profiles not set</title>" + "<body><h1>" + name
						+ " profiles not set</h1></body>" + "</html> ";
			}
		}
		
		@Path("write")
		@GET
		@Produces(MediaType.TEXT_HTML)
		public String write() {
			DBReadWriter.writeAll();
			return "<html> " + "<title>" + name + " all objs written to db</title>" + "<body><h1>" + name
					+ " all objs written to db</h1></body>" + "</html> ";
		}
		
		/* ANALYZES THE PROFILES MADE AND CREATES COMMUNITIES
		 * KMEANS
		 * You are to implement a RESTful web service /community using GET that, if run after the /context web service, 
		 * returns an HTML representation of the communities computed for the users. The HTML must contain a table in 
		 * which each row contains the community name (C-X, X = 1,..., m) followed by a cell containing a comma delimited 
		 * list of user names. Should the community service be run before the context service an error should be generated.
		 * */
		@Path("community")
		@GET
		@Produces(MediaType.TEXT_HTML)
		public String community() {
			//TODO: implement real communities collections
			String communityString = "";
			ArrayList<ArrayList<String>> communitiesArr = new ArrayList<ArrayList<String>>();
			communitiesArr.add(new ArrayList<String>()); 
			communitiesArr.add(new ArrayList<String>()); 
			communitiesArr.add(new ArrayList<String>()); 
			communitiesArr.add(new ArrayList<String>()); 
			communitiesArr.add(new ArrayList<String>()); 
			Communitizer commune=new Communitizer(UserStore.getInstance().size());
			int i=0;
			for(Enumeration<User> us = UserStore.getInstance().getUsers().elements(); us.hasMoreElements();){
				User user  = us.nextElement();
				commune.addUser(user, i);
				i++;
				//communitiesArr.get(user.getCommunity()-1).add(user.getName()); //bc added communities as 1-3
			}
			commune.algorithm();
			i=0;
			for(Enumeration<User> us = UserStore.getInstance().getUsers().elements(); us.hasMoreElements();){
				User user  = us.nextElement();
				int community=commune.getCommuityForUser(i);
				user.setCommunity(community);//check that this is how you save
				communitiesArr.get(community).add(user.getName());
				
				i++;
				//communitiesArr.get(user.getCommunity()-1).add(user.getName()); //bc added communities as 1-3
			}
			
			int count = 1;
			for(ArrayList<String> community : communitiesArr){
				communityString += "<tr><td style=\"border: 1px solid grey; vertical-align: top;\">" + count + "</td>";
				communityString += "<td style=\"border: 1px solid grey;\">" + community.stream().collect(Collectors.joining(", ")) + "</td></tr>";
				count++;
			}
				
			return "<html> " + "<title>" + name + " communities</title>" + "<body><h1>" + name
						+ " communities</h1> <table style=\"border: 1px solid grey;border-collapse: collapse;\"><tr><th style=\"border: 1px solid grey;\">COMMUNITY</th><th style=\"border: 1px solid grey;\">USERS</th></tr>"+communityString+" </body>" + "</html> ";
		}
		
		/* 
		 * You are to implement a RESTful web service /fetch/{user}/{page} using GET that retrieves a page from the 
		 * context specified in (8) (a testing set will be specified after the submission deadline) and augments it 
		 * with advertising. Your web service must consist of 2 frames, one containing the content of the requested 
		 * page, and the other containing advertising.
		 * */
		@Path("fetch/{user}/{page}")
		@GET
		@Produces(MediaType.TEXT_HTML)
		public String fetch(@PathParam("user") String user,
							@PathParam("page") String page) {
			try {
				//Grab HTML from the dir then augment with advertisements. 
				String path = ROOT + "resources/pages/" + page + ".html";
				File moviePage = new File(path);
				if(moviePage.canRead()){
					Document movieDoc;
					movieDoc = Jsoup.parse(moviePage, "UTF-8");
					Element prependTo = movieDoc.body().children().first();
					Movie m = MovieStore.getInstance().find(page);
					User u = UserStore.getInstance().find(user);
					if(u != null && m != null){
						String userAddElementText = u.grabUserAdd();
						String movieAddElementText = m.grabMovieAdd();
						prependTo.before("<div style='position:  fixed;background:  white;width: 100%;bottom: 0;'>"
								+ "<h3>Advertisements</h3>"
								+ "<div style='display:inline-block; width:200px !important;'>"
								+ userAddElementText
								+ "<h5 style='display:inline; padding:0; width:200px !important'>Community "+(u.getCommunity()+1)+" add</h5>"
								+ "</div>"
								+ "<div style='display:inline-block; width:200px !important;'>"
								+ movieAddElementText
								+ "<h5 style='display:inline; padding:0; width:200px !important'>"+m.getGenre()+" add</h5>"
								+ "</div>"
								+ "</div>");
						return movieDoc.html();
					}
				}
				return "<html> " + "<title>" + name + " fetch</title>" + "<body><h1>" + name
						+ " 500 - error grabbing page</h1>  </body>" + "</html> ";
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return "<html> " + "<title>" + name + " fetch</title>" + "<body><h1>" + name
					+ " 404 - could not find page :-(</h1>  </body>" + "</html> ";
		}
		
		/*
		 * You are to implement a RESTful web service /advertising/{category} using GET that returns an HTML representation of the advertising category 
		 * that you have designed. Here, category would be C-X, X = 1,..., m. NOTE: It is reasonable to expect that several pieces of advertising
		 * would be provided for a particular community. This is for you to design and document.
		 * */
		@Path("advertising/{category}")
		@GET
		@Produces(MediaType.TEXT_HTML)
		public String adds(@PathParam("category") int category) {
			try {
				String path = ROOT + "adds/" + category;
				File dir = new File(path);
				File[] directoryListing = dir.listFiles();
				if (directoryListing != null) {
					Random rand = new Random();
					String adds = "";
					int fileIndex = rand.nextInt((directoryListing.length-1) - 0 + 1); 
					for(File imageFile : directoryListing){
						if(imageFile.canRead()){
							BufferedImage image = ImageIO.read(imageFile);

							ByteArrayOutputStream bos = new ByteArrayOutputStream();
				        
							ImageIO.write(image, "png", bos);
					        byte[] imageBytes = bos.toByteArray();
					        
					        BASE64Encoder encoder = new BASE64Encoder();
				            String imageString = encoder.encode(imageBytes);
				            adds += "<img width='200' src='data:image/png;base64, "+imageString+"'/>";
						}
					}
					return "<html> " + "<title>" + name + " categories</title>" + "<body><h1>" + name
							+ " Category " + category+ " Adds</h1>"+adds+"</body>" + "</html> ";
				}
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return "<html> " + "<title>" + name + " fetch</title>" + "<body><h1>" + name
					+ " 404 - could not find category adds</h1>  </body>" + "</html> ";
		}	
		
		/*
		 * You are to implement a RESTful web service /advertising/{category} using GET that returns an HTML representation of the advertising category 
		 * that you have designed. Here, category would be C-X, X = 1,..., m. NOTE: It is reasonable to expect that several pieces of advertising
		 * would be provided for a particular community. This is for you to design and document.
		 * */
		@Path("advertising-genre/{category}")
		@GET
		@Produces(MediaType.TEXT_HTML)
		public String addMovie(@PathParam("category") String category) {
			try {
				String path = ROOT + "adds/" + category;
				File dir = new File(path);
				File[] directoryListing = dir.listFiles();
				if (directoryListing != null) {
					Random rand = new Random();
					String adds = "";
					int fileIndex = rand.nextInt((directoryListing.length-1) - 0 + 1); 
					for(File imageFile : directoryListing){
						if(imageFile.canRead()){
							BufferedImage image = ImageIO.read(imageFile);

							ByteArrayOutputStream bos = new ByteArrayOutputStream();
				        
							ImageIO.write(image, "png", bos);
					        byte[] imageBytes = bos.toByteArray();
					        
					        BASE64Encoder encoder = new BASE64Encoder();
				            String imageString = encoder.encode(imageBytes);
				            adds += "<img width='200' src='data:image/png;base64, "+imageString+"'/>";
						}
					}
					return "<html> " + "<title>" + name + " categories</title>" + "<body><h1>" + name
							+ " Category " + category+ " Adds</h1>"+adds+"</body>" + "</html> ";
				}
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return "<html> " + "<title>" + name + " fetch</title>" + "<body><h1>" + name
					+ " 404 - could not find category adds</h1>  </body>" + "</html> ";
		}	
}
