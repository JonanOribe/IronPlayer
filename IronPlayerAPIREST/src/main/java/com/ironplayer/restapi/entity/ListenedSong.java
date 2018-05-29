package com.ironplayer.restapi.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Store listened song info
 * 
 * @author Jose
 *
 */
@Entity
public class ListenedSong implements Serializable {

	private static final long serialVersionUID = 4512214088324603346L;

	private String id;

	private String title;

	private int listenedCounter;

	private Date lastListenedDate;

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getListenedCounter() {
		return listenedCounter;
	}

	public void setListenedCounter(int listenedCounter) {
		this.listenedCounter = listenedCounter;
	}

	public Date getLastListenedDate() {
		return lastListenedDate;
	}

	public void setLastListenedDate(Date lastListenedDate) {
		this.lastListenedDate = lastListenedDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
