package com.ironplayer.restapi.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ironplayer.restapi.entity.IronUser;
import com.ironplayer.restapi.persistence.PersistenceLayer;
import com.ironplayer.restapi.pojo.StandardJSonResponsePOJO;
import com.ironplayer.restapi.util.Constants;
import com.ironplayer.restapi.util.SendEmailTLS;

@Path("/login")
public class LoginService {

	@GET
	@Path("/addNewIronUser/{varX}/{varY}")
	@Produces(MediaType.APPLICATION_JSON)

	public StandardJSonResponsePOJO addNewIronUser(@PathParam("varX") String login,
			@PathParam("varY") String password) {
		PersistenceLayer ps = new PersistenceLayer();
		StandardJSonResponsePOJO sjr = ps.addNewUser(login, password);
		return sjr;

	}

	@GET
	@Path("/checkLoginIronUser/{varX}/{varY}")
	@Produces(MediaType.APPLICATION_JSON)

	public StandardJSonResponsePOJO checkLoginIronUser(@PathParam("varX") String login,
			@PathParam("varY") String password) {
		PersistenceLayer ps = new PersistenceLayer();
		StandardJSonResponsePOJO sjr = ps.checkLoginIronUser(login, password);
		return sjr;

	}

	@GET
	@Path("/recoverPassword/{varX}")
	@Produces(MediaType.APPLICATION_JSON)

	public StandardJSonResponsePOJO recoverPassword(@PathParam("varX") String login) {
		PersistenceLayer ps = new PersistenceLayer();
		IronUser ironUser = ps.getIronUserByLogin(login);
		StandardJSonResponsePOJO sjr = new StandardJSonResponsePOJO();
		if (ironUser == null) {
			sjr.setCode(Constants.ERROR);
			sjr.setDescription(Constants.ERROR_USER_NOT_FOUND);
		} else {
			sjr.setCode(Constants.OK);
			sjr.setDescription(Constants.OK_SENT_RECOVER_PASS);
			SendEmailTLS sendEmailTLS = new SendEmailTLS();
			sendEmailTLS.send(ironUser.getLogin(), ironUser.getPassword());
		}

		return sjr;

	}

	@GET
	@Path("/deleteIronUser/{varX}")
	@Produces(MediaType.APPLICATION_JSON)

	public StandardJSonResponsePOJO deleteIronUser(@PathParam("varX") String login) {
		PersistenceLayer ps = new PersistenceLayer();
		ps.deleteIronUser(login);
		StandardJSonResponsePOJO sjr = new StandardJSonResponsePOJO();
		sjr.setCode(Constants.OK);
		sjr.setDescription(Constants.OK_REMOVED_USER);

		return sjr;

	}

}