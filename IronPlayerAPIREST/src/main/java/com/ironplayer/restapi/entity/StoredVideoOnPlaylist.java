package com.ironplayer.restapi.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class StoredVideoOnPlaylist implements Serializable {

	/**
	 * Store video kept on user playlist
	 * 
	 * @author Jose
	 *
	 */
	private static final long serialVersionUID = -3654505019469488797L;

	public StoredVideoOnPlaylist() {

	}

	private String videoId;

	private String title;

	private String thumbnail;

	private String url;

	private PlayList playList;

	@Id
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public PlayList getPlayList() {
		return playList;
	}

	public void setPlayList(PlayList playList) {
		this.playList = playList;
	}

}
