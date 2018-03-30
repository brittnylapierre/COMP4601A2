package edu.carleton.comp4601.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.stream.Collectors;

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

import edu.carleton.comp4601.dao.UserStore;
import edu.carleton.comp4601.models.User;

@Path("/rs")
public class Recommender {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
		@Context
		UriInfo uriInfo;
		@Context
		Request request;

		private String name;

		String[] genres = {"horror", "action", "romance"};

		public Recommender() {
			//reader = new Reader();
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
		/*@Path("reset")
		@GET
		@Produces(MediaType.TEXT_HTML)
		public String reset() {
			Reader reader = new Reader();
			try {
				reader.readMovies();
				reader.readUsers();
				System.out.println("done reading.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return "<html> " + "<title>" + name + " reset</title>" + "<body><h1>" + name
						+ " failed</h1></body>" + "</html> ";
			}
			return "<html> " + "<title>" + name + " reset</title>" + "<body><h1>" + name
					+ " reset success</h1></body>" + "</html> ";
		}*/
		
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
			//try {
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
			//} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return "<html> " + "<title>" + name + " reset</title>" + "<body><h1>" + name
						+ " done reading in from db</h1></body>" + "</html> ";
			//}
			
			//return "<html> " + "<title>" + name + " reset</title>" + "<body><h1>" + name
					//+ " reset success</h1></body>" + "</html> ";
		}
		
		//GENRES, NUM ACCESS PER GENRE
		//diversity of genre, volume of content consumed, 
		/* 
		 * xi = {xi1, xi2, xi2, ...}
		 *       # comedy, # horror, # romance, # action, #drama, total
		 *       
<html><head><title>0790701251</title>
<meta name="userId" content="A100JCBNALJFAW">
<meta name="profileName" content="eze543113 "EZE"">
<meta name="helpfulness" content="2/3">
<meta name="score" content="5.0">
<meta name="time" content="1157932800">
<meta name="summary" content="wow stan could sure make a powerfull movie but what else is new">
</head
><body><p>great powerfull movie this was,, i really enjoy how stan dosent have to add a score to make the drama 
more powerfull thats how good the directing is. i also enjoy the story very much. i really dont see many movies 
that show the break down people feel in boot camp. the only bad thing is it was full screen but still i love this 
film!!!</p></body></html>
		
		 CREATES PROFILE DATA FOR USERS
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
			for(Enumeration<User> us = UserStore.getInstance().getUsers().elements(); us.hasMoreElements();){
				User user  = us.nextElement();
				communitiesArr.get(user.getCommunity()-1).add(user.getName()); //bc added communities as 1-3
			}
			int count = 1;
			for(ArrayList<String> community : communitiesArr){
				communityString += "COMMUNITY " + count + ": ";
				communityString += community.stream().collect(Collectors.joining(", ")) + "<br>";
				count++;
			}
				
			return "<html> " + "<title>" + name + " communities</title>" + "<body><h1>" + name
						+ " communities</h1> "+communityString+" </body>" + "</html> ";
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
				//TODO: use kelly's path var
				String path = "C:/Users/IBM_ADMIN/workspace/COMP4601A2/resources/pages/" + page + ".html";
				File moviePage = new File(path);
				if(moviePage.canRead()){
					Document movieDoc;
					movieDoc = Jsoup.parse(moviePage, "UTF-8");
					Element body = movieDoc.body();
					User u = UserStore.getInstance().find(user);
					if(u != null){
						String addElementText = UserStore.getInstance().find(user).grabUserAdds();
						body.append("<div>"
								+ "<h3>Adverts!!!!!!!!!!!!!!!!! :D</h3>"
								+ addElementText
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
		 * You are to document the communities that you have found in analyzing the test data in the web pages and users 
		 * provided. Furthermore, you must document how you created your advertising content and the mechanism used to 
		 * retrieve it when using the /fetch/{user}/{page} service.
		 * */
		
		/* USER BASED https://sikaman.dyndns.org/courses/4601/handouts/11-Recommender_Systems_An_Introduction_Chapter02_Collaborative_recommendation
		 * You are to implement a RESTful web service /advertising/{category} using GET that returns an HTML 
		 * representation of the advertising category that you have designed. Here, category would be 
		 * C-X, X = 1,..., m. NOTE: It is reasonable to expect that several pieces of advertising would be provided for 
		 * a particular community. This is for you to design and document.
		 * */
		
		
}
