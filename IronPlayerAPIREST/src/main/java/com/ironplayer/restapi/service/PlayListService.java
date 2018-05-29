package com.ironplayer.restapi.service;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ironplayer.restapi.entity.ListenedSong;
import com.ironplayer.restapi.persistence.PersistenceLayer;
import com.ironplayer.restapi.pojo.StandardJSonResponsePOJO;
import com.ironplayer.restapi.pojo.YoutubeSearchResultPOJO;
import com.ironplayer.restapi.pojo.YoutubeVideoDetailPOJO;
import com.ironplayer.restapi.util.Constants;

@Path("/playListService")
public class PlayListService {

	private static String YOUTUBE_URL_TEMPLATE = "https://www.youtube.com/watch?v=";

	// Example:
	@GET
	@Path("/createNewPlaylist/{varX}/{varY}")
	@Produces(MediaType.APPLICATION_JSON)
	public StandardJSonResponsePOJO createNewPlaylist(@PathParam("varX") String login,
			@PathParam("varY") String playlistName) {
		PersistenceLayer ps = new PersistenceLayer();
		boolean result = ps.createNewPlaylist(login, playlistName);
		StandardJSonResponsePOJO sjr = new StandardJSonResponsePOJO();
		if (result) {
			sjr.setCode(Constants.OK);
			sjr.setDescription(Constants.OK_PLAYLIST_CREATED);
		} else {
			sjr.setCode(Constants.ERROR);
			sjr.setDescription(Constants.ERROR_ON_CREATE_PLAYLIST);
		}
		return sjr;

	}

	// Example:
	@GET
	@Path("/deletePlaylist/{varX}/{varY}")
	@Produces(MediaType.APPLICATION_JSON)
	public StandardJSonResponsePOJO deletePlaylist(@PathParam("varX") String login,
			@PathParam("varY") String playListName) {
		PersistenceLayer ps = new PersistenceLayer();
		ps.deletePlayList(login, playListName);
		StandardJSonResponsePOJO sjr = new StandardJSonResponsePOJO();

		sjr.setCode(Constants.OK);
		sjr.setDescription(Constants.OK_PLAYLIST_CREATED);

		return sjr;

	}

	// Example:
	@GET
	@Path("/addSontToPlaylist/{varX}/{varY}/{varZ}/{varA}/{varB}")
	@Produces(MediaType.APPLICATION_JSON)
	public StandardJSonResponsePOJO addSontToPlaylist(@PathParam("varX") String login,
			@PathParam("varY") String playListName, @PathParam("varZ") String thumbnail,
			@PathParam("varA") String title, @PathParam("varB") String videoId) {
		PersistenceLayer ps = new PersistenceLayer();
		ps.addSontToPlaylist(login, playListName, YOUTUBE_URL_TEMPLATE + videoId, thumbnail, title, videoId);
		StandardJSonResponsePOJO sjr = new StandardJSonResponsePOJO();
		sjr.setCode(Constants.OK);
		sjr.setDescription(Constants.OK_PLAYLIST_CREATED);

		return sjr;

	}

	@GET
	@Path("/addSongToListenedSongList/{varX}/{varY}")
	@Produces(MediaType.APPLICATION_JSON)
	public StandardJSonResponsePOJO addSongToListenedSongList(@PathParam("varX") String id,
			@PathParam("varY") String title) {
		PersistenceLayer ps = new PersistenceLayer();
		ps.addSongToListenedSongList(id, title);
		StandardJSonResponsePOJO sjr = new StandardJSonResponsePOJO();
		sjr.setCode(Constants.OK);
		sjr.setDescription(Constants.OK_ADD_SONG_TO_LISTENED_SONG_LIST);

		return sjr;

	}

	@GET
	@Path("/getMostListenedSongList")
	@Produces(MediaType.APPLICATION_JSON)
	public YoutubeSearchResultPOJO getMostListenedSongList() {
		String sql = "select l from ListenedSong l order by l.listenedCounter desc";
		PersistenceLayer ps = new PersistenceLayer();
		ArrayList<ListenedSong> moreListedSongList = ps.getMoreListenedSong(sql);
		YoutubeSearchResultPOJO youtubeSearchResultPOJO = new YoutubeSearchResultPOJO();
		for (ListenedSong listenedSong : moreListedSongList) {
			YoutubeVideoDetailPOJO youtubeVideoDetailPOJO = new YoutubeVideoDetailPOJO();
			youtubeVideoDetailPOJO.setVideoId(listenedSong.getId());
			youtubeVideoDetailPOJO.setTitle(listenedSong.getTitle());
			youtubeVideoDetailPOJO.setListenedCounter(listenedSong.getListenedCounter());
			youtubeSearchResultPOJO.getSongInfoList().add(youtubeVideoDetailPOJO);
		}
		return youtubeSearchResultPOJO;

	}

	@GET
	@Path("/getLastListenedSongList")
	@Produces(MediaType.APPLICATION_JSON)
	public YoutubeSearchResultPOJO getLastListenedSongList() {
		String sql = "select l from ListenedSong l order by l.lastListenedDate desc";
		PersistenceLayer ps = new PersistenceLayer();
		ArrayList<ListenedSong> moreListedSongList = ps.getMoreListenedSong(sql);
		YoutubeSearchResultPOJO youtubeSearchResultPOJO = new YoutubeSearchResultPOJO();
		for (ListenedSong listenedSong : moreListedSongList) {
			YoutubeVideoDetailPOJO youtubeVideoDetailPOJO = new YoutubeVideoDetailPOJO();
			youtubeVideoDetailPOJO.setVideoId(listenedSong.getId());
			youtubeVideoDetailPOJO.setTitle(listenedSong.getTitle());
			youtubeVideoDetailPOJO.setListenedCounter(listenedSong.getListenedCounter());
			youtubeSearchResultPOJO.getSongInfoList().add(youtubeVideoDetailPOJO);
		}
		return youtubeSearchResultPOJO;

	}
}
