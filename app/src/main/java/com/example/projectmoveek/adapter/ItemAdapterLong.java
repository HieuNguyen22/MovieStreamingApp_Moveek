package com.example.projectmoveek.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAdapterLong extends RecyclerView.Adapter<ItemAdapterLong.ItemViewLongHolder> implements Filterable {

    private Context mContext;
    private IClickOnItemListener iClickOnItemListener;
    private List<ItemModel> mList;
    private List<ItemModel> mListOld;
    private DatabaseHelper databaseHelper;
    private FirebaseAuth mAuth;
    private String fidUser;

    // IP tro
//    private String ip = "192.168.0.102";
    // IP nha
//    private String ip = "192.168.0.109";
    // IP dt
    private String ip = "192.168.43.134";

    private String urlLike = "http://" + ip + "/android_moveek/get_data_like.php";
    private String urlDeleteLike = "http://" + ip + "/android_moveek/delete_data_like.php";
    private String urlInsertLike = "http://" + ip + "/android_moveek/insert_data_like.php";
    private String urlUser = "http://" + ip + "/android_moveek/get_data_user.php";

    public ItemAdapterLong(Context mContext, IClickOnItemListener listener) {
        this.mContext = mContext;
        this.iClickOnItemListener = listener;
    }

    public void setData(List<ItemModel> list){
        this.mList = list;
        this.mListOld = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewLongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_holder_long, parent, false);

        return new ItemViewLongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewLongHolder holder, int position) {
        databaseHelper = new DatabaseHelper(mContext);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null)
            fidUser = mAuth.getCurrentUser().getUid();

        ItemModel item = mList.get(position);
        if(item == null) return;

        // Set data
        holder.tvTitle.setText(item.getTitle());
        Glide.with(mContext).load(item.getPoster()).into(holder.imgPoster);
        holder.tvYear .setText(item.getYear());
        holder.tvLength .setText(item.getLength());
        holder.tvClass .setText(item.getType());
        holder.tvGenre .setText(item.getGenre());
        holder.tvImdb .setText(item.getImdb()+"");

        // Check favor if logged in
        if(mAuth.getCurrentUser() != null) {
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
                                    if (fidUser.equals(responseFID) && responseMID == (mList.get(holder.getAdapterPosition()).getId())) {
                                        holder.imgFavor.setImageResource(R.drawable.ic_round_favorite_full_24);
//                                    Toast.makeText(mContext, "Lay response like thanh cong", Toast.LENGTH_SHORT).show();
                                    }
                                    // Test
//                                Log.e("GET POSITION", (holder.getAdapterPosition() + 1) + "   " + responseMID);
//                                Log.e("GET FID", fidUser + "   " + responseFID);
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
                                            if (fidUser.equals(responseFID) && responseMID == mList.get(holder.getAdapterPosition()).getId()) {
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
                                                params.put("m_id", mList.get(holder.getAdapterPosition()).getId() + "");

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
                                                int posItem = mList.get(holder.getAdapterPosition()).getId();
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
        holder.viewHolderLong.setOnClickListener(new View.OnClickListener() {
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
        if(mList != null)
            return mList.size();
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()){
                    mList = mListOld;
                } else {
                    List<ItemModel> listTmp = new ArrayList<>();
                    for (ItemModel item : mListOld) {
                        if (item.getTitle().toLowerCase().contains(strSearch.toLowerCase())){
                            listTmp.add(item);
                        }
                    }

                    mList = listTmp;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mList = (List<ItemModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ItemViewLongHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout viewHolderLong;
        private ImageView imgPoster, imgFavor, imgPlay;
        private TextView tvTitle, tvYear, tvLength, tvClass, tvGenre, tvImdb;

        public ItemViewLongHolder(@NonNull View itemView) {
            super(itemView);

            viewHolderLong = itemView.findViewById(R.id.holder_item_long);
            imgPoster = itemView.findViewById(R.id.holder_poster);
            imgFavor = itemView.findViewById(R.id.img_favor);
            imgPlay = itemView.findViewById(R.id.btn_play);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvYear = itemView.findViewById(R.id.tv_year);
            tvLength = itemView.findViewById(R.id.tv_length);
            tvClass = itemView.findViewById(R.id.tv_class);
            tvGenre = itemView.findViewById(R.id.tv_genre);
            tvImdb = itemView.findViewById(R.id.tv_imdb);
        }
    }
}
