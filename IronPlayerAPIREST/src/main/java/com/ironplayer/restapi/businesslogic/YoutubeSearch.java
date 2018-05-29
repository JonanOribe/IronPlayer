package com.ironplayer.restapi.businesslogic;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.ironplayer.restapi.pojo.YoutubeSearchResultPOJO;
import com.ironplayer.restapi.pojo.YoutubeVideoDetailPOJO;

/**
 * Business class to search by term on Youtube API
 * 
 * @author Jose
 *
 */

public class YoutubeSearch {

	/**
	 * Define a global variable that identifies the name of a file that contains the
	 * developer's API key.
	 */
	private static final String PROPERTIES_FILENAME = "youtube.properties";

	/**
	 * Define a global instance of a Youtube object, which will be used to make
	 * YouTube Data API requests.
	 */
	private static YouTube youtube;

	/**
	 * Initialize a YouTube object to search for videos on YouTube. Then display the
	 * name and thumbnail image of each video in the result set.
	 *
	 * @param args
	 *            command line args.
	 */

	public YoutubeSearch() {
		// TODO Auto-generated constructor stub
	}

	public YoutubeSearchResultPOJO search(String searchTerm) {
		YoutubeSearchResultPOJO youtubeSearchResultPOJO  = new YoutubeSearchResultPOJO();
		
		// Read the developer key from the properties file.
		Properties properties = new Properties();
		try {
			InputStream in = YoutubeSearch.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
			properties.load(in);

		} catch (IOException e) {
			System.err.println(
					"There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause() + " : " + e.getMessage());
			System.exit(1);
		}

		try {
			// This object is used to make YouTube Data API requests. The last
			// argument is required, but since we don't need anything
			// initialized when the HttpRequest is initialized, we override
			// the interface and provide a no-op function.
			youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
				public void initialize(HttpRequest request) throws IOException {
				}
			}).setApplicationName("youtube-cmdline-search-sample").build();

			// Prompt the user to enter a query term.

			// Define the API request for retrieving search results.
			YouTube.Search.List search = youtube.search().list("id,snippet");

			// Set your developer key from the {{ Google Cloud Console }} for
			// non-authenticated requests. See:
			// {{ https://cloud.google.com/console }}
			String apiKey = properties.getProperty("youtube.apikey");
			long numberOfReturnedVideo = Long.parseLong(properties.getProperty("youtube.number.of.returned.video"));
			search.setKey(apiKey);
			search.setQ(searchTerm);

			// Restrict the search results to only include videos. See:
			// https://developers.google.com/youtube/v3/docs/search/list#type
			search.setType("video");

			// To increase efficiency, only retrieve the fields that the
			// application uses.
			search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
			search.setMaxResults(numberOfReturnedVideo);

			// Call the API and print results.
			SearchListResponse searchResponse = search.execute();
			List<SearchResult> searchResultList = searchResponse.getItems();
			if (searchResultList != null) {
				prettyPrint(searchResultList.iterator(), searchTerm, properties.getProperty("youtube.url"),
						numberOfReturnedVideo);
				youtubeSearchResultPOJO=buildYoutubeVideoDetailPojo(searchResultList.iterator(), searchTerm,
						properties.getProperty("youtube.url"));

			}
		} catch (GoogleJsonResponseException e) {
			System.err.println(
					"There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
		} catch (IOException e) {
			System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
			e.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return youtubeSearchResultPOJO;
	}

	private void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query, String youtubeUrl,
			long numberOfReturnedVideo) {

		System.out.println("\n=============================================================");
		System.out.println("   First " + numberOfReturnedVideo + " videos for search on \"" + query + "\".");
		System.out.println("=============================================================\n");

		if (!iteratorSearchResults.hasNext()) {
			System.out.println(" There aren't any results for your query.");
		}

		while (iteratorSearchResults.hasNext()) {

			SearchResult singleVideo = iteratorSearchResults.next();
			ResourceId rId = singleVideo.getId();

			// Confirm that the result represents a video. Otherwise, the
			// item will not contain a video ID.
			if (rId.getKind().equals("youtube#video")) {
				Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

				System.out.println(" Video Id: " + rId.getVideoId());
				System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
				System.out.println(" Thumbnail: " + thumbnail.getUrl());
				System.out.println(" URL: " + youtubeUrl + rId.getVideoId());
				System.out.println("\n-------------------------------------------------------------\n");
			}
		}
	}

	private  YoutubeSearchResultPOJO buildYoutubeVideoDetailPojo(Iterator<SearchResult> iteratorSearchResults,
			String query, String youtubeUrl) {
		YoutubeSearchResultPOJO youtubeSearchResultPOJO  = new YoutubeSearchResultPOJO();
		while (iteratorSearchResults.hasNext()) {
			YoutubeVideoDetailPOJO youtubeVideoDetailPOJO = new YoutubeVideoDetailPOJO();
			SearchResult singleVideo = iteratorSearchResults.next();
			ResourceId rId = singleVideo.getId();

			// Confirm that the result represents a video. Otherwise, the
			// item will not contain a video ID.
			if (rId.getKind().equals("youtube#video")) {
				Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
				youtubeVideoDetailPOJO.setVideoId(rId.getVideoId());
				youtubeVideoDetailPOJO.setTitle(singleVideo.getSnippet().getTitle());
				youtubeVideoDetailPOJO.setThumbnail(thumbnail.getUrl());
				youtubeVideoDetailPOJO.setUrl(youtubeUrl + rId.getVideoId());
				youtubeSearchResultPOJO.getSongInfoList().add(youtubeVideoDetailPOJO);
			}
		}

		return youtubeSearchResultPOJO;
	}

}
