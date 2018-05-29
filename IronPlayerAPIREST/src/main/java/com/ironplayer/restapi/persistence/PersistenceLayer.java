package com.ironplayer.restapi.persistence;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.PathParam;

import com.ironplayer.restapi.entity.IronUser;
import com.ironplayer.restapi.entity.ListenedSong;
import com.ironplayer.restapi.entity.PlayList;
import com.ironplayer.restapi.entity.StoredVideoOnPlaylist;
import com.ironplayer.restapi.pojo.StandardJSonResponsePOJO;
import com.ironplayer.restapi.util.Constants;

public class PersistenceLayer {

	EntityManagerFactory emFactoryObj;
	EntityManager entityMgr;

	public PersistenceLayer() {
		emFactoryObj = Persistence.createEntityManagerFactory("IronPlayerPU");
		entityMgr = emFactoryObj.createEntityManager();

	}

	/**
	 * Add new user
	 * 
	 * @param login
	 * @param password
	 * @return
	 */
	public StandardJSonResponsePOJO addNewUser(String login, String password) {
		StandardJSonResponsePOJO stJPojo = new StandardJSonResponsePOJO();

		try {
			if (ironUserExists(login)) {
				stJPojo.setCode(Constants.ERROR);
				stJPojo.setDescription(Constants.ERROR_USER_EXISTS);
			}

			else {
				entityMgr.getTransaction().begin();
				IronUser ironUser = new IronUser();
				ironUser.setLogin(login);
				ironUser.setPassword(password);
				entityMgr.merge(ironUser);
				entityMgr.getTransaction().commit();
				entityMgr.clear();
				stJPojo.setCode(Constants.OK);
				stJPojo.setDescription(Constants.OK_NEW_USER);
				System.out.println("Record Successfully Inserted In The Database");
			}
		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
			e.printStackTrace();
		}
		return stJPojo;
	}

	/**
	 * Check right user and password: user can access to IronPlayer APP
	 * 
	 * @param login
	 * @param password
	 * @return
	 */
	public StandardJSonResponsePOJO checkLoginIronUser(String login, String password) {
		StandardJSonResponsePOJO stJPojo = new StandardJSonResponsePOJO();
		try {
			String sql = "select i from IronUser i where login='" + login + "' and password='" + password + "'";
			Query query = entityMgr.createQuery(sql, IronUser.class);
			ArrayList<IronUser> result = (ArrayList<IronUser>) query.getResultList();
			if (result.size() > 0) {
				stJPojo.setCode(Constants.OK);
				stJPojo.setDescription(Constants.OK_LOGIN_USER);
			} else {
				stJPojo.setCode(Constants.ERROR);
				stJPojo.setDescription(Constants.ERROR_LOGIN_USER);
			}

		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
			e.printStackTrace();
		}

		return stJPojo;

	}

	/**
	 * Check user already exists
	 * 
	 * @param login
	 * @return
	 */
	public boolean ironUserExists(String login) {
		IronUser foundIronUser = entityMgr.find(IronUser.class, login);
		if (foundIronUser != null) {
			return true;
		} else
			return false;
	}

	public IronUser getIronUserByLogin(String login) {
		IronUser foundIronUser = entityMgr.find(IronUser.class, login);
		return foundIronUser;
	}

	public boolean createNewPlaylist(String login, String playlistName) {
		try {
			IronUser ironUser = entityMgr.find(IronUser.class, login);
			if (getPlayListByUserAndName(login, playlistName).size() > 0) {
				return false;
			} else {

				entityMgr.getTransaction().begin();
				PlayList playList = new PlayList();
				playList.setIronUser(ironUser);
				playList.setTitle(playlistName);
				entityMgr.merge(playList);
				entityMgr.getTransaction().commit();
				entityMgr.clear();
			}
		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void deletePlayList(String login, String playListName) {
		try {
			String sql = "select p from PlayList p where ironUser.login='" + login + "' and title='" + playListName
					+ "'";
			Query query = entityMgr.createQuery(sql, PlayList.class);
			ArrayList<PlayList> result = (ArrayList<PlayList>) query.getResultList();
			entityMgr.getTransaction().begin();
			PlayList playList = result.get(0);
			entityMgr.remove(playList);
			entityMgr.getTransaction().commit();
			entityMgr.clear();

		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void addSontToPlaylist(String login, String playListName, String songURL, String thumbnail, String title,
			String videoId) {
		try {
			String sql = "select p from PlayList p where ironUser.login='" + login + "' and title='" + playListName
					+ "'";
			Query query = entityMgr.createQuery(sql, PlayList.class);
			ArrayList<PlayList> result = (ArrayList<PlayList>) query.getResultList();
			entityMgr.getTransaction().begin();
			PlayList playList = result.get(0);
			StoredVideoOnPlaylist storedVideoOnPlaylist = new StoredVideoOnPlaylist();
			storedVideoOnPlaylist.setPlayList(playList);
			storedVideoOnPlaylist.setThumbnail(thumbnail);
			storedVideoOnPlaylist.setTitle(title);
			storedVideoOnPlaylist.setUrl(songURL);
			storedVideoOnPlaylist.setVideoId(videoId);
			entityMgr.merge(storedVideoOnPlaylist);
			entityMgr.getTransaction().commit();
			entityMgr.clear();

		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
			e.printStackTrace();
		}
	}

	public ArrayList<PlayList> getPlayListByUserAndName(String login, String playListName) {
		String sql = "select i from PlayList i where i.ironUser.login='" + login + "' and i.title='" + playListName
				+ "'";
		Query query = entityMgr.createQuery(sql, PlayList.class);
		ArrayList<PlayList> result = (ArrayList<PlayList>) query.getResultList();
		return result;

	}

	/**
	 * Remove IronUser 
	 * 
	 * @param login
	 */
	public void deleteIronUser(String login) {
		try {
			entityMgr.getTransaction().begin();
			IronUser ironUser=entityMgr.find(IronUser.class, login);
			entityMgr.remove(ironUser);
			entityMgr.getTransaction().commit();
			entityMgr.clear();
		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Add or update listed song
	 * 
	 * @param id
	 * @param title
	 */
	public void addSongToListenedSongList(String id, String title) {
		try {
			ListenedSong listenedSong = entityMgr.find(ListenedSong.class, id);
			if (listenedSong != null) {
				listenedSong.setListenedCounter(listenedSong.getListenedCounter() + 1);
			} else {
				listenedSong = new ListenedSong();
				listenedSong.setId(id);
				listenedSong.setTitle(title);
				listenedSong.setListenedCounter(1);
			}
			listenedSong.setLastListenedDate(new Date());
			entityMgr.getTransaction().begin();
			entityMgr.merge(listenedSong);
			entityMgr.getTransaction().commit();
			entityMgr.clear();

		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Get listened order by more listened
	 * 
	 * @return
	 */
	public ArrayList<ListenedSong> getMoreListenedSong(String sql) {
		ArrayList<ListenedSong> listenedSongList = new ArrayList<ListenedSong>();
		try {
			entityMgr.getTransaction().begin();
			Query query = entityMgr.createQuery(sql, ListenedSong.class);
			listenedSongList = (ArrayList<ListenedSong>) query.getResultList();
			entityMgr.clear();

		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
			e.printStackTrace();
		}
		return listenedSongList;
	}

}