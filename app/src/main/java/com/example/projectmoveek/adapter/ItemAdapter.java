package com.example.projectmoveek.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.projectmoveek.controller.DatabaseHelper;
import com.example.projectmoveek.MovieInfoActivity;
import com.example.projectmoveek.R;
import com.example.projectmoveek.SignInActivity;
import com.example.projectmoveek.model.ItemModel;
import com.example.projectmoveek.my_interface.IClickOnItemListener;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Context mContext;
    private IClickOnItemListener iClickOnItemListener;
    private List<ItemModel> mListItem;
    private DatabaseHelper databaseHelper;
    private FirebaseAuth mAuth;
    private String fidUser;

    // IP tro
//   private String ip = "192.168.0.111";
    // IP nha
//    private String ip = "192.168.0.109";

    private String url = "https://moveekhye.000webhostapp.com/";
    private String urlLike = url + "get_data_like.php";
    private String urlDeleteLike = url + "delete_data_like.php";
    private String urlInsertLike = url + "insert_data_like.php";
    private String urlGetUser = url + "get_data_user.php";

    public ItemAdapter(Context mContext, IClickOnItemListener listener) {
        this.mContext = mContext;
        this.iClickOnItemListener = listener;
    }

    public void setData(List<ItemModel> list) {
        this.mListItem = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        databaseHelper = new DatabaseHelper(mContext);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null)
            fidUser = mAuth.getCurrentUser().getUid();

        ItemModel item = mListItem.get(position);
        if (item == null) return;

        // Set data
        holder.title.setText(item.getTitle());
        Glide.with(mContext).load(item.getPoster()).into(holder.holderPoster);
        holder.movieInfo.setText(item.getInfo());

        // Check favor if logged in
        if (mAuth.getCurrentUser() != null){
            // Set favor
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlLike, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject object = response.getJSONObject(i);
                                    // Movie ID
                                    int responseMID = object.getInt("M_ID");
                                    // User FID
                                    String responseFID = object.getString("U_FID");

                                    // Check Movie ID and FID
                                    if (fidUser.equals(responseFID) && responseMID == (holder.getAdapterPosition() + 1)) {
                                        holder.imgFavor.setImageResource(R.drawable.ic_round_favorite_full_24);
//                                    Toast.makeText(mContext, "Lay response like thanh cong", Toast.LENGTH_SHORT).show();
                                    }

                                    // Test
                                    Log.e("GET POSITION", (holder.getAdapterPosition() + 1) + "   " + responseMID);
                                    Log.e("GET FID", fidUser + "   " + responseFID);
                                } catch (JSONException e) {
                                    Toast.makeText(mContext, "Loi JSON" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("AAA", e.getMessage());
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(mContext, "Loi JSON" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            requestQueue.add(jsonArrayRequest);

            // Catch event click favor
            holder.imgFavor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestQueue requestQueueClick = Volley.newRequestQueue(mContext);
                    JsonArrayRequest requestClick = new JsonArrayRequest(Request.Method.GET, urlLike, null,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    boolean checkIsLiked = false;

                                    // Check is liked?
                                    for (int i = 0; i < response.length(); i++) {
                                        try {
                                            JSONObject object = response.getJSONObject(i);
                                            // Movie ID
                                            int responseMID = object.getInt("M_ID");
                                            // User FID
                                            String responseFID = object.getString("U_FID");
                                            // Check

                                            // Test
                                            Log.e("GET POSITION1", (holder.getAdapterPosition() + 1) + "   " + responseMID);
                                            Log.e("GET FID1", fidUser + "   " + responseFID);

                                            // Check already favor?
                                            if (fidUser.equals(responseFID) && responseMID == (holder.getAdapterPosition() + 1)) {
                                                checkIsLiked = true;
                                                break;
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    // Add to favor
                                    if (checkIsLiked == false) {
                                        RequestQueue requestQueueInsert = Volley.newRequestQueue(mContext);
                                        StringRequest stringRequestInsert = new StringRequest(Request.Method.POST, urlInsertLike,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if (response.trim().equals("success")) {
                                                            holder.imgFavor.setImageResource(R.drawable.ic_round_favorite_full_24);
                                                            Toast.makeText(mContext, "Da them vao favorite", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(mContext, "Them vao favorite that bai", Toast.LENGTH_SHORT).show();
                                                            Log.e("BBB", response);
                                                        }

                                                        // Update recycler view
                                                        iClickOnItemListener.onClickItem(item);
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {

                                                    }
                                                }
                                        ) {
                                            @Nullable
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("fidUser", fidUser);
                                                params.put("m_id", (holder.getAdapterPosition() + 1) + "");

                                                return params;
                                            }
                                        };

                                        requestQueueInsert.add(stringRequestInsert);
                                    }
                                    // Remove from favor
                                    else {
                                        RequestQueue requestQueueDelete = Volley.newRequestQueue(mContext);
                                        StringRequest stringRequestDelete = new StringRequest(Request.Method.POST, urlDeleteLike,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        if (response.trim().equals("success")) {
                                                            holder.imgFavor.setImageResource(R.drawable.ic_round_favorite_24);
                                                            Toast.makeText(mContext, "Da xoa khoi favorite", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(mContext, "Xoa khoi favorite that bai", Toast.LENGTH_SHORT).show();
                                                            Log.e("BBB", response);
                                                        }

                                                        // Update recycler view
                                                        iClickOnItemListener.onClickItem(item);
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
                                                int posItem = holder.getAdapterPosition() + 1;
                                                params.put("m_id", posItem + "");
                                                params.put("fidUser", fidUser);

                                                return params;
                                            }
                                        };

                                        requestQueueDelete.add(stringRequestDelete);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(mContext, "Loi JSON" + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                    requestQueueClick.add(requestClick);

                }
            });
        }

        // On event click
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // test
                Toast.makeText(mContext, item.getId()+"", Toast.LENGTH_SHORT).show();

                // Check favor
                RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlLike, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                // load activity
                                if(mAuth.getCurrentUser() == null) {
                                    Intent intent = new Intent(mContext, SignInActivity.class);
                                    mContext.startActivity(intent);
                                } else {
                                    boolean check = false;
                                    for (int i = 0; i < response.length(); i++) {
                                        try {
                                            JSONObject object = response.getJSONObject(i);
                                            // Movie ID
                                            int responseMID = object.getInt("M_ID");
                                            // User FID
                                            String responseFID = object.getString("U_FID");

                                            // Check Movie ID and FID
                                            if (fidUser.equals(responseFID) && responseMID == item.getId()) {
                                                check = true;
                                                break;
                                            }
                                        } catch (JSONException e) {
                                            Toast.makeText(mContext, "Loi JSON" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.e("AAA", e.getMessage());
                                        }
                                    }
                                    Intent intent = new Intent(mContext, MovieInfoActivity.class);
                                    intent.putExtra("id", item.getId());
                                    intent.putExtra("favor", check);
                                    mContext.startActivity(intent);
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(mContext, "Loi JSON" + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                requestQueue.add(jsonArrayRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListItem != null)
            return mListItem.size();
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView holderPoster;
        private ImageView imgFavor;
        private TextView title;
        private TextView movieInfo;
        private ConstraintLayout viewHolder;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            viewHolder = itemView.findViewById(R.id.holder_item);
            holderPoster = itemView.findViewById(R.id.holder_poster);
            imgFavor = itemView.findViewById(R.id.img_favor);
            title = itemView.findViewById(R.id.tv_movie_title);
            movieInfo = itemView.findViewById(R.id.tv_movie_info);
        }
    }

}
