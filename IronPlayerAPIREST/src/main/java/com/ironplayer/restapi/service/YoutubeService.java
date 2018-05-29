package com.ironplayer.restapi.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ironplayer.restapi.businesslogic.YoutubeSearch;
import com.ironplayer.restapi.pojo.YoutubeSearchResultPOJO;

/**
 * RESTfull service to get result from Youtube API
 * 
 * @author Jose
 *
 */
@Path("/youtube")
public class YoutubeService {

	@GET
	@Path("/searchOnYoutube/{varX}")
	@Produces(MediaType.APPLICATION_JSON)

	public YoutubeSearchResultPOJO searchOnYoutube(@PathParam("varX") String searchTerm) {
		YoutubeSearch youtubeSearch = new YoutubeSearch();
		YoutubeSearchResultPOJO youtubeSearchResultPOJO = youtubeSearch.search(searchTerm);
		return youtubeSearchResultPOJO;
	}
}
