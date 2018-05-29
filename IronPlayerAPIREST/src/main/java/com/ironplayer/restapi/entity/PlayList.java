package com.ironplayer.restapi.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class PlayList implements Serializable {

	/**
	 * Stores user playlist
	 * 
	 * @author Jose
	 */
	private static final long serialVersionUID = 8511637249003484769L;

	private int id;

	private String title;

	private IronUser ironUser;

	
	public PlayList() {
		
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public IronUser getIronUser() {
		return ironUser;
	}

	public void setIronUser(IronUser ironUser) {
		this.ironUser = ironUser;
	}
	
	

}
