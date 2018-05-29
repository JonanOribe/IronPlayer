package com.example.jonan.ironplayer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.ironplayer.android.util.Constants;

public class MainActivity_Youtube extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener ,YouTubePlayer.PlaybackEventListener{

    String claveYoutube="AIzaSyDPxPSvahcrqRyzq9SArjBkhNLzw2vQG84";
    YouTubePlayerView youtubePlayerView;
    private  String youtubeVideoId;
    Button btnShareVideo;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        youtubeVideoId= (String) getIntent().getSerializableExtra("youtubeVideoId");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__youtube);
        //Toast.makeText(getApplicationContext(), "idVideo to load: "+youtubeVideoId, Toast.LENGTH_LONG).show();
        youtubePlayerView=(YouTubePlayerView)findViewById(R.id.youtube_view);
        youtubePlayerView.initialize(claveYoutube,this);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        btnShareVideo=findViewById(R.id.btnShareVideo);
        callbackManager=CallbackManager.Factory.create();
        shareDialog=new ShareDialog(this);

        btnShareVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder youtubeURL= new StringBuilder();
                youtubeURL.append(Constants.YOUTUBE_URL_TEMPLATE);
                youtubeURL.append(youtubeVideoId);

                ShareLinkContent linkContent=new ShareLinkContent.Builder()
                        .setQuote(Constants.FACEBOOK_SAHAREE_IRON_PLAYER_MESSAGE)
                        .setContentUrl(Uri.parse(youtubeURL.toString()))
                        .build();
                if(ShareDialog.canShow(ShareLinkContent.class)){
                    shareDialog.show(linkContent);
                }
            }
        });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean fueRestaurado) {

        if(!fueRestaurado){
            //youTubePlayer.cueVideo("BtuOAnsuZBY");
            youTubePlayer.cueVideo(youtubeVideoId);
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        if(youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(this,1).show();
        }else{
            String error="Error al inicializar Youtube: "+youTubeInitializationResult.toString();
            Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode==1){
            getYoutubePlayerProvider().initialize(claveYoutube,this);
        }
    }

    protected  YouTubePlayer.Provider getYoutubePlayerProvider(){
        return youtubePlayerView;
    }

    @Override
    public void onPlaying() {

    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onStopped() {
    }

    @Override
    public void onBuffering(boolean b) {

    }

    @Override
    public void onSeekTo(int i) {

    }
}
