package com.fb.platform.user;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import com.fb.platform.user.manager.interfaces.UserManager;
import com.sun.jersey.api.core.InjectParam;



/**
 * @author kislaya
 *
 */
@Path("/user")
@Component
@Scope("request")
public class UserResource {
	
	 @Context
	    UriInfo uriInfo;

	 @InjectParam
	private UserManager userManager;
	 
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces("text/xml")
	public String adduser(JAXBElement<UserModel> userXML){
		UserModel userModel = userXML.getValue();
		return "<user>" + userModel.getEmail() + "</user>";
	}

	@GET
	@Path ("{key}")
	@Produces("text/xml")
	public Object getUser(@PathParam("key") String key) {
		 StringBuffer buffer = new StringBuffer();
	     buffer.append("Hello ").append(key);
	     return "<user>" + userManager.getuser(key).getName() + "</user>";
	}

}
