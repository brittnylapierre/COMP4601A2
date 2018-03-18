package edu.carleton.comp4601.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;


@Path("/rs")
public class Recommender {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
		@Context
		UriInfo uriInfo;
		@Context
		Request request;

		private String name;

		public Recommender() {
			name = "Brittny and Kelly RS";
		}
		
		@GET
		public String printName() {
			return name;
		}
		
		@GET
		@Produces(MediaType.TEXT_XML)
		public String sayXML() {
			return "<?xml version=\"1.0\"?>" + "<rs> " + name + " </rs>";
		}

		@GET
		@Produces(MediaType.TEXT_HTML)
		public String sayHtml() {
			return "<html> " + "<title>" + name + "</title>" + "<body><h1>" + name
					+ "</h1></body>" + "</html> ";
		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		public String sayJSON() {
			return "{" + name + "}";
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
		
		/* 
		 * You are to implement a RESTful web service /context using GET that analyzes the web pages and returns an 
		 * HTML representation of the profiles for users. The HTML returned must clearly show what features are 
		 * being used as part of the profile for a user. It is expected that the response would contain an HTML table 
		 * with one row containing information for a user.
		 * */
		
		/*
		 * You are to implement a RESTful web service /community using GET that, if run after the /context web service, 
		 * returns an HTML representation of the communities computed for the users. The HTML must contain a table in 
		 * which each row contains the community name (C-X, X = 1,..., m) followed by a cell containing a comma delimited 
		 * list of user names. Should the community service be run before the context service an error should be generated.
		 * */
		
		/* 
		 * You are to implement a RESTful web service /fetch/{user}/{page} using GET that retrieves a page from the 
		 * context specified in (8) (a testing set will be specified after the submission deadline) and augments it 
		 * with advertising. Your web service must consist of 2 frames, one containing the content of the requested 
		 * page, and the other containing advertising.
		 * */
		
		/*
		 * You are to document the communities that you have found in analyzing the test data in the web pages and users 
		 * provided. Furthermore, you must document how you created your advertising content and the mechanism used to 
		 * retrieve it when using the /fetch/{user}/{page} service.
		 * */
		
		/* 
		 * You are to implement a RESTful web service /advertising/{category} using GET that returns an HTML 
		 * representation of the advertising category that you have designed. Here, category would be 
		 * C-X, X = 1,..., m. NOTE: It is reasonable to expect that several pieces of advertising would be provided for 
		 * a particular community. This is for you to design and document.
		 * */
}
