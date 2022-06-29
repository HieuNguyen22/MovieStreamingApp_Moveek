package com.example.projectmoveek.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.projectmoveek.MovieInfoActivity;
import com.example.projectmoveek.R;
import com.example.projectmoveek.SignInActivity;
import com.example.projectmoveek.model.ItemModel;
import com.google.firebase.auth.FirebaseAuth;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder>{
    private Context mContext;
    private List<ItemModel> mList;
    private ViewPager2 viewPager;

    private FirebaseAuth mAuth;
    private String fidUser;

    // IP tro
//   private String ip = "192.168.0.111";
    // IP nha
    private String ip = "192.168.0.109";

    private String urlLike = "http://" + ip + "/android_moveek/get_data_like.php";

    public SliderAdapter(Context mContext, ViewPager2 viewPager) {
        this.mContext = mContext;
        this.viewPager = viewPager;
    }

    public void setData(List<ItemModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item_container, parent, false);

        return new SliderAdapter.SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null)
            fidUser = mAuth.getCurrentUser().getUid();

        ItemModel item = mList.get(position);

        holder.tvTitleMovie.setText(item.getTitle());
        holder.tvInfoMovie.setText(item.getInfo());
        holder.setImage(item);

        if (position == mList.size() - 2){
            viewPager.post(runnable);
        }

        // On event click
        holder.sliderLayout.setOnClickListener(new View.OnClickListener() {
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
        if (mList != null)
            return mList.size();
        return 0;
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitleMovie, tvInfoMovie;
        private ImageView imageView;
        private LinearLayout sliderLayout;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_slide_holder);
            tvTitleMovie = itemView.findViewById(R.id.tv_movie_title);
            tvInfoMovie = itemView.findViewById(R.id.tv_movie_info);
            sliderLayout = itemView.findViewById(R.id.imageSlider);
        }

        void setImage(ItemModel sliderItem){
             Glide.with(mContext).load(sliderItem.getPoster()).into(imageView);
//            Glide.with(mContext).load("https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/movie_poster%2Fdoctor_strange_poster.jpg?alt=media&token=9622bd8c-9db2-4cab-961f-51e02c0a9932").into(imageView);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mList.addAll(mList);
            notifyDataSetChanged();
        }
    };
}
