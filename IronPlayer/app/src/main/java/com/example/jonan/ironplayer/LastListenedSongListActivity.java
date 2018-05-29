package com.example.jonan.ironplayer;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ironplayer.android.util.Constants;
import com.ironplayer.android.util.YoutubeTrackPOJO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LastListenedSongListActivity extends AppCompatActivity {

    private ListView lv;
    private List<YoutubeTrackPOJO> youtubeTrackPOJOList;
    private TextView llslTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_listened_song_list);
        llslTitle=(TextView)findViewById(R.id.llslTitle);
        lv = (ListView)findViewById(R.id.lvllsl);
        youtubeTrackPOJOList=new ArrayList<YoutubeTrackPOJO>();
        getLastListenedSongList();
        // Initializing a new String Array
        String[] fruits = new String[]{};

        // Create a List from String Array elements
        final List<String> fruits_list = new ArrayList<String>(Arrays.asList(fruits));

        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, fruits_list);
        // Assign adapter to ListView
        lv.setAdapter(arrayAdapter);


        // ListView Item Click Listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) lv.getItemAtPosition(position);

                String youtubeVideoId=youtubeTrackPOJOList.get(position).getVideoId();

                // Show Alert
                //Toast.makeText(getApplicationContext(),
                        //youtubeVideoId, Toast.LENGTH_LONG)
                        //.show();
                addSongToListenedSongList(youtubeTrackPOJOList.get(position).getVideoId(),youtubeTrackPOJOList.get(position).getTitle());
                Intent i=new Intent(getApplicationContext(),MainActivity_Youtube.class);
                i.putExtra("youtubeVideoId",youtubeVideoId);
                startActivity(i);


            }

        });

        fruits_list.clear();
        for (int i = 0; i < youtubeTrackPOJOList.size(); i++) {
            fruits_list.add(youtubeTrackPOJOList.get(i).getTitle());
        }
        llslTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pasar_a_MainActivity_Principal= new Intent(LastListenedSongListActivity.this,YoutubeActivity.class);// Anteriormente MainActivity_Youtube
        }
        });

    }

    public boolean getLastListenedSongList() {
        boolean rightsearchOnYoutube = true;

        String urlApiREST = Constants.URL_IRON_PLAYER_API_RESTFULL+"/playListService/getLastListenedSongList";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(urlApiREST);
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            String json = new String();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            json = response.toString();


            try {


                Log.e("log_tag", "Error in convert String" + json.toString());

                JSONObject json_data = new JSONObject(json);

                {

                    String data = json_data.getString("songInfoList");

                    JSONArray json_data1 = new JSONArray(data);
                    youtubeTrackPOJOList.clear();
                    for (int i = 0; i < json_data1.length(); i++) {
                        json_data = json_data1.getJSONObject(i);
                        YoutubeTrackPOJO youtubeTrackPOJO = new YoutubeTrackPOJO();
                        youtubeTrackPOJO.setVideoId(json_data.getString("videoId"));
                        youtubeTrackPOJO.setUrl(json_data.getString("url"));
                        youtubeTrackPOJO.setTitle(json_data.getString("title"));
                        youtubeTrackPOJO.setThumbnail(json_data.getString("thumbnail"));
                        youtubeTrackPOJO.setListenedCounter(json_data.getInt("listenedCounter"));
                        youtubeTrackPOJOList.add(youtubeTrackPOJO);

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            rightsearchOnYoutube = false;
        } catch (IOException e) {
            e.printStackTrace();
            rightsearchOnYoutube = false;
        } catch (Exception e) {
            rightsearchOnYoutube = false;
            e.printStackTrace();
        }

        return rightsearchOnYoutube;
    }

    public boolean addSongToListenedSongList(String songId, String title) {
        boolean rightAddSongToListenedSongList = true;

        String urlApiREST = Constants.URL_IRON_PLAYER_API_RESTFULL + "/playListService/addSongToListenedSongList/" + songId + "/" + title.replaceAll(" ","%20");
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(urlApiREST);
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            String json = new String();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            json = response.toString();

            JSONObject jsonObject = new JSONObject(json);


            Log.d("JSON  code", jsonObject.get("code").toString());
            Log.d("JSON  description", jsonObject.get("description").toString());

            if (jsonObject.get("code").toString().equals(Constants.OK)) {
                rightAddSongToListenedSongList = true;
            } else
                rightAddSongToListenedSongList = true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rightAddSongToListenedSongList;
    }

}
