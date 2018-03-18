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
}
