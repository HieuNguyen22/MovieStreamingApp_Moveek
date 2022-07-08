package com.example.projectmoveek;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.projectmoveek.adapter.CastJoinAdapter;
import com.example.projectmoveek.controller.DatabaseHelper;
import com.example.projectmoveek.model.CastJoinMovieModel;
import com.example.projectmoveek.model.ItemModel;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieInfoActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 10;
    private TextView tvTitle, tvLength, tvDescription, tvWatchNow;
    private ImageView imgPoster, imgBack, imgFavor, imgDownload;

    private List<ItemModel> mList;
    private List<CastJoinMovieModel> mListCast;
    private CastJoinAdapter mCastAdapter;
    private RecyclerView rcvCastJoin;
    private int idMovie;
    private boolean isFavor;
    private String fidUser;

    private FirebaseAuth mAuth;

    // IP tro
    private String ip = "192.168.0.102";
    // IP nha
//    private String ip = "192.168.0.109";

    // URL
//    private String urlWeb = "https://moveekhye.000webhostapp.com/";
    private String urlWeb =  "http://" + ip + "/android_moveek/";

    private String urlGetMovie = urlWeb+"get_data_movie.php";
    private String urlGetCast = urlWeb+"get_data_cast_join_movie.php";
    private String urlDeleteLike = urlWeb+"delete_data_like.php";
    private String urlInsertLike = urlWeb+"insert_data_like.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        mAuth = FirebaseAuth.getInstance();

        // Anh xa view
        tvTitle = findViewById(R.id.tv_title_info);
        tvLength = findViewById(R.id.tv_length_info);
        tvDescription = findViewById(R.id.tv_description_info);
        tvWatchNow = findViewById(R.id.tV_watch_now);
        imgPoster = findViewById(R.id.img_poster_info);
        imgBack = findViewById(R.id.img_back);
        imgFavor = findViewById(R.id.img_favorite);
        imgDownload = findViewById(R.id.img_download_info);
        rcvCastJoin = findViewById(R.id.rcv_cast_info);

        mList = new ArrayList<>();
        mListCast = new ArrayList<>();
        mCastAdapter = new CastJoinAdapter(this);
        mAuth = FirebaseAuth.getInstance();

        fidUser = mAuth.getCurrentUser().getUid();

        // Get position
        Intent intent = getIntent();
        idMovie = intent.getIntExtra("id", 1);
        isFavor = intent.getBooleanExtra("favor", false);


        // Set data movie
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        getMovie();

        if(isFavor)
            imgFavor.setImageResource(R.drawable.ic_round_favorite_full_24);
        else
            imgFavor.setImageResource(R.drawable.ic_round_favorite_24);

        // Set up recyclerview for cast
        getCast();
        mCastAdapter.setData(mListCast);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false);
        rcvCastJoin.setLayoutManager(gridLayoutManager);
        rcvCastJoin.setAdapter(mCastAdapter);


        // __________ CATCH EVENT _____________
        // On click back
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // On click favorite
        clickFavor();

        // On click watch now
        tvWatchNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MovieInfoActivity.this, MovieStreamActivity.class);
                intent1.putExtra("pos", idMovie - 1);
                startActivity(intent1);
            }
        });

        // On click download
        imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, REQUEST_PERMISSION_CODE);
            } else {
                startDownload();
            }
        }
        else {
            startDownload();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownload();
            } else {
                Toast.makeText(this, "Permission is denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startDownload() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGetMovie, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject object = response.getJSONObject(idMovie - 1);
                            int id = object.getInt("ID");
                            String title = object.getString("Title");
                            String urlDownloadFile = object.getString("Trailer");

                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlDownloadFile));
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                            request.setTitle("Download");
                            request.setDescription("Downloading movie...");

                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                            request.setDestinationInExternalFilesDir(MovieInfoActivity.this, Environment.DIRECTORY_DOWNLOADS, id + "_" + title + "_Moveek.mp4");

                            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                            if (downloadManager != null) {
                                downloadManager.enqueue(request);
                            }

                            Toast.makeText(MovieInfoActivity.this, "Link download: " + urlDownloadFile, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            Toast.makeText(MovieInfoActivity.this, "Loi JSON" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AAA", "Loi phan hoi" + error);
                        Toast.makeText(MovieInfoActivity.this, "Loi phan hoi! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private void clickFavor() {
        imgFavor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Insert to favor
                if(!isFavor){
                    RequestQueue requestQueueInsert = Volley.newRequestQueue(MovieInfoActivity.this);
                    StringRequest stringRequestInsert = new StringRequest(Request.Method.POST, urlInsertLike,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.trim().equals("success")) {
                                        imgFavor.setImageResource(R.drawable.ic_round_favorite_full_24);
                                        Toast.makeText(MovieInfoActivity.this, "Da them vao favorite", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MovieInfoActivity.this, "Them vao favorite that bai", Toast.LENGTH_SHORT).show();
                                        Log.e("BBB", response);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MovieInfoActivity.this, "Loi", Toast.LENGTH_SHORT).show();
                                }
                            }
                    ) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("fidUser", fidUser);
                            params.put("m_id", idMovie + "");

                            return params;
                        }
                    };
                    requestQueueInsert.add(stringRequestInsert);
                }
                // Remove from favor
                else {
                    RequestQueue requestQueueDelete = Volley.newRequestQueue(MovieInfoActivity.this);
                    StringRequest stringRequestDelete = new StringRequest(Request.Method.POST, urlDeleteLike,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.trim().equals("success")) {
                                        imgFavor.setImageResource(R.drawable.ic_round_favorite_24);
                                        Toast.makeText(MovieInfoActivity.this, "Da xoa khoi favorite", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MovieInfoActivity.this, "Xoa khoi favorite that bai", Toast.LENGTH_SHORT).show();
                                        Log.e("BBB", response);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("ERROR: ", error.getMessage());
                                }
                            }
                    ) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("m_id", idMovie + "");
                            params.put("fidUser", fidUser);

                            return params;
                        }
                    };
                    requestQueueDelete.add(stringRequestDelete);
                }
            }
        });
    }

    private void getCast() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlGetCast,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                mListCast.add(new CastJoinMovieModel(object.getInt("ID"), object.getString("Name")
                                        , object.getString("Image"), object.getString("CharName")));
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MovieInfoActivity.this, "cmm" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        mCastAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AAA", "dcmm: " + idMovie +" "+ error);
                        Toast.makeText(MovieInfoActivity.this, "Loi cast! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mID", idMovie + "");

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getMovie() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGetMovie, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            JSONObject object = response.getJSONObject(idMovie - 1);

                            tvTitle.setText(object.getString("Title"));
                            tvLength.setText(object.getString("Length") + " | " + object.getString("Genre"));
                            tvDescription.setText(object.getString("Description"));

                            Glide.with(getBaseContext()).load(object.getString("Poster")).into(imgPoster);
                        } catch (JSONException e) {
                            Toast.makeText(MovieInfoActivity.this, "cmm" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AAA", "dcmm" + error);
                        Toast.makeText(MovieInfoActivity.this, "DIT CON ME MAY! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}