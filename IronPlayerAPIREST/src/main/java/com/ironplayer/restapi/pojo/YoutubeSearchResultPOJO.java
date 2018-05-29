package com.ironplayer.restapi.pojo;

import java.util.ArrayList;

public class YoutubeSearchResultPOJO {

	private ArrayList<YoutubeVideoDetailPOJO> songInfoList;

	public YoutubeSearchResultPOJO() {
		this.setSongInfoList(new ArrayList<YoutubeVideoDetailPOJO>());
	}

	public ArrayList<YoutubeVideoDetailPOJO> getSongInfoList() {
		return songInfoList;
	}

	public void setSongInfoList(ArrayList<YoutubeVideoDetailPOJO> songInfoList) {
		this.songInfoList = songInfoList;
	}
}
