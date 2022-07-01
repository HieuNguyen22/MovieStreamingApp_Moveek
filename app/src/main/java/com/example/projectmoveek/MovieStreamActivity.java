package com.example.projectmoveek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectmoveek.controller.DatabaseHelper;
import com.example.projectmoveek.model.ItemModel;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MovieStreamActivity extends AppCompatActivity {

    //Initialize variables
    private PlayerView playerView;
    private ProgressBar progressBar;
    private ImageView imgFullscreen;
    private TextView tvTitle, tvInfo;
    private SimpleExoPlayer simpleExoPlayer;
    private boolean flag = false;
    private int position;
    private List<ItemModel> mList;
    private DatabaseHelper databaseHelper;

    private FirebaseAuth mAuth;

    // IP tro
//    private String ip = "192.168.0.111";
    // IP nha
//    private String ip = "192.168.0.109";

    private String urlWeb = "https://moveekhye.000webhostapp.com/";
    private String urlGetMovies = urlWeb + "get_data_movie.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_stream);

        mAuth = FirebaseAuth.getInstance();

        // Assign variables
        playerView = findViewById(R.id.player_view);
        progressBar = findViewById(R.id.progress_bar);
        imgFullscreen = findViewById(R.id.btn_fullscreen);
        tvTitle = findViewById(R.id.tv_title_streaming);
        tvInfo = findViewById(R.id.tv_info_streaming);

        // Get position
        Intent intent = getIntent();
        position = intent.getIntExtra("pos", 1);

        databaseHelper = new DatabaseHelper(this);

        // Get item
        getMovie();

    }

    private void getMovie() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGetMovies, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject object = response.getJSONObject(position);

                            // Get title
                            tvTitle.setText(object.getString("Title"));
                            // Get Info
                            tvInfo.setText(object.getString("Length") + " | " + object.getString("Genre"));
                            // Make activity fullscreen
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            // Get trailer url
                            String trailerURL = object.getString("Trailer");
                            Uri videoURL = Uri.parse(trailerURL);
                            // Initialize load control
                            LoadControl loadControl = new DefaultLoadControl();
                            // Initialize band width meter
                            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                            // Initialize track selector
                            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                            // Initialize simple exo player
                            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getBaseContext(), trackSelector, loadControl);
                            // Initialize data source  factory
                            DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory("exoplayer_video");
                            // Initialize extractors factory
                            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                            // Initialize media source
                            MediaSource mediaSource = new ExtractorMediaSource(videoURL, factory, extractorsFactory, null, null);

                            // Set player
                            playerView.setPlayer(simpleExoPlayer);
                            // Keep screen on
                            playerView.setKeepScreenOn(true);
                            // Prepare media
                            simpleExoPlayer.prepare(mediaSource);

                            // Play video when ready
                            simpleExoPlayer.setPlayWhenReady(true);
                            simpleExoPlayer.addListener(new Player.EventListener() {
                                @Override
                                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                                    // Check condition
                                    if(playbackState == Player.STATE_BUFFERING){
                                        // When buffering
                                        // Show progress bar
                                        progressBar.setVisibility(View.VISIBLE);
                                    } else if (playbackState == Player.STATE_READY){
                                        // When ready
                                        // Hide progress bar
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
                                }

                                @Override
                                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                                }

                                @Override
                                public void onLoadingChanged(boolean isLoading) {
                                }

                                @Override
                                public void onRepeatModeChanged(int repeatMode) {
                                }

                                @Override
                                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                                }

                                @Override
                                public void onPlayerError(ExoPlaybackException error) {
                                }

                                @Override
                                public void onPositionDiscontinuity(int reason) {
                                }

                                @Override
                                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                                }

                                @Override
                                public void onSeekProcessed() {
                                }
                            });

                            imgFullscreen.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Check condition
                                    if(flag) {
                                        // Set image fullscreen
                                        imgFullscreen.setImageResource(R.drawable.ic_baseline_fullscreen);

                                        // Set portrait orientation
                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                                        // Set flag value is false;
                                        flag = false;
                                    } else{
                                        // Set image exit fullscreen
                                        imgFullscreen.setImageResource(R.drawable.ic_round_fullscreen_exit);

                                        // Set landscape orientation
                                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                                        // Set flag value is true
                                        flag = true;
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            Toast.makeText(MovieStreamActivity.this, "cmm" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AAA", "dcmm" + error);
                        Toast.makeText(MovieStreamActivity.this, "DIT CON ME MAY! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop video when ready
        simpleExoPlayer.setPlayWhenReady(false);

        // Get playback state
        simpleExoPlayer.getPlaybackState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Play video when ready
        simpleExoPlayer.setPlayWhenReady(true);

        // Get playback state
        simpleExoPlayer.getPlaybackState();
    }

}