package com.ironplayer.restapi.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Store IronUser
 * 
 * @author Jose
 *
 */
@Entity
public class IronUser implements Serializable{
	private static final long serialVersionUID = 2954171459794226384L;

	private String login;
	
	private String password;

	@Id
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
