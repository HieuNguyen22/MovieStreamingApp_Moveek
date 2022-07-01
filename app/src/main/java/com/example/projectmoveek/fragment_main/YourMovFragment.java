package com.example.projectmoveek.fragment_main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectmoveek.controller.DatabaseHelper;
import com.example.projectmoveek.R;
import com.example.projectmoveek.adapter.ItemAdapterLong;
import com.example.projectmoveek.model.ItemModel;
import com.example.projectmoveek.my_interface.IClickOnItemListener;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class YourMovFragment extends Fragment {

    private RecyclerView rcvYourMov;
    private ItemAdapterLong itemAdapter;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_your_mov, container, false);

        rcvYourMov = root.findViewById(R.id.rcv_your_mov);
        mAuth = FirebaseAuth.getInstance();
        mList = new ArrayList<>();

        databaseHelper = new DatabaseHelper(getContext());

        itemAdapter = new ItemAdapterLong(getContext(), new IClickOnItemListener() {
            @Override
            public void onClickItem(ItemModel item) {
                updateData();
            }
        });

        setRecyclerView();

        return root;
    }

    private void setRecyclerView() {
        if (mAuth.getCurrentUser() != null){
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGetMovies, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            mList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject object = response.getJSONObject(i);
                                    // Path storage
                                    File fileDownload = new File("/storage/emulated/0/Android/data/com.example.projectmoveek/files/Download");
                                    File[] listFileMovie = fileDownload.listFiles();

                                    // Check if movie has been already downloaded?
                                    for (File fileMovie : listFileMovie) {
                                        String nameFile = fileMovie.getName();
                                        String[] nameParts = nameFile.split("_");
                                        String fileID = nameParts[0];
                                        if(object.getInt("ID") == Integer.parseInt(fileID.trim())){
                                            mList.add(new ItemModel(object.getInt("ID"), object.getString("Title"), object.getString("Poster")
                                                    , object.getString("Year")
                                                    , object.getString("Length")
                                                    , object.getString("Type")
                                                    , object.getString("Genre")
                                                    , object.getString("Description")
                                                    , object.getString("Trailer")
                                                    , object.getDouble("Imdb")));
                                        }
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getContext(), "Loi JSON" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            itemAdapter.notifyDataSetChanged();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("AAA", "Loi phan hoi" + error);
                            Toast.makeText(getContext(), "Loi phan hoi " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
            requestQueue.add(jsonArrayRequest);
        }
        else
            mList = databaseHelper.getAllMoviesItemLong(itemAdapter);

        itemAdapter.setData(mList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);

        rcvYourMov.setLayoutManager(gridLayoutManager);
        rcvYourMov.setAdapter(itemAdapter);
    }

    private void updateData() {
        mList.clear();
        setRecyclerView();
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }
}