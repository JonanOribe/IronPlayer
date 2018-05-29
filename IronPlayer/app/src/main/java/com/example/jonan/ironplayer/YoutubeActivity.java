package com.example.jonan.ironplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class YoutubeActivity extends Activity {

    private List<YoutubeTrackPOJO> youtubeTrackPOJOList;

    private EditText editTextYoutubeFind;

    private Button mostListenedBtn;
    private Button lastListenedBtn;
    private String credenciales_usuario;
    private String pass_usuario;
    private ImageButton btnPref;
    private ImageButton imageBtnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        credenciales_usuario = getIntent().getStringExtra("SESSION_ID");
        pass_usuario= getIntent().getStringExtra("SESSION_PASS");
        editTextYoutubeFind = (EditText) findViewById(R.id.editTextYoutubeFind);
        youtubeTrackPOJOList = new ArrayList<YoutubeTrackPOJO>();
        mostListenedBtn=(Button) findViewById(R.id.mlbtn);
        lastListenedBtn=(Button) findViewById(R.id.llbtn);
        btnPref=(ImageButton) findViewById(R.id.imageButton);
        imageBtnLogout=(ImageButton) findViewById(R.id.imageBtnLogout);
        // Get reference of widgets from XML layout
        final ListView lv = (ListView) findViewById(R.id.lv);
        final Button btn = (Button) findViewById(R.id.buttonYoutubeFind);

        // Initializing a new String Array
        String[] fruits = new String[]{};

        // Create a List from String Array elements
        final List<String> fruits_list = new ArrayList<String>(Arrays.asList(fruits));

        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, fruits_list);

        // DataBind ListView with items from ArrayAdapter


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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextYoutubeFind.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Introduce un término de búsqueda", Toast.LENGTH_LONG).show();
                } else {
                    searchOnYoutube(editTextYoutubeFind.getText().toString());
                    fruits_list.clear();
                    for (int i = 0; i < youtubeTrackPOJOList.size(); i++) {
                        fruits_list.add(youtubeTrackPOJOList.get(i).getTitle());
                    }
                }

                arrayAdapter.notifyDataSetChanged();
            }
        });

        mostListenedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),MostListenedSongListActivity.class);
                startActivity(i);
            }
        });
        lastListenedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),LastListenedSongListActivity.class);
                startActivity(i);
            }
        });

btnPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pasar_a_MainActivity_Principal= new Intent(YoutubeActivity.this,MainActivityConfiguracion.class);// Anteriormente MainActivity_Youtube
                pasar_a_MainActivity_Principal.putExtra("SESSION_ID",credenciales_usuario);
                pasar_a_MainActivity_Principal.putExtra("SESSION_PASS", pass_usuario);

                startActivity(pasar_a_MainActivity_Principal);
            }
        });

        imageBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pasar_a_MainActivity_Principal= new Intent(YoutubeActivity.this,LoginActivity.class);// Anteriormente MainActivity_Youtube
                pasar_a_MainActivity_Principal.putExtra("SESSION_ID","");
                pasar_a_MainActivity_Principal.putExtra("SESSION_PASS", "");
                startActivity(pasar_a_MainActivity_Principal);
            }
        });

    }

    public boolean searchOnYoutube(String parameter) {
        boolean rightsearchOnYoutube = true;

        String urlApiREST = Constants.URL_IRON_PLAYER_API_RESTFULL+"/youtube/searchOnYoutube/" + parameter.replaceAll(" ","%20");
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