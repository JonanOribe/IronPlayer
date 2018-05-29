package com.ironplayer.android.util;

/**
 * Store info from Youtube API
 *
 * @author Jose
 */
public class YoutubeTrackPOJO {

    public YoutubeTrackPOJO() {

    }

    private String videoId;

    private String title;

    private String thumbnail;

    private String url;

    private int listenedCounter;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setListenedCounter(int listenedCounter) {
        this.listenedCounter = listenedCounter;
    }

    public int getListenedCounter() {

        return listenedCounter;
    }
}
