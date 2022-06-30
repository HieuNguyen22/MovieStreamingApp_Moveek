package com.example.projectmoveek.fragment_main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.projectmoveek.adapter.SliderAdapter;
import com.example.projectmoveek.controller.DatabaseHelper;
import com.example.projectmoveek.NotificationActivity;
import com.example.projectmoveek.SearchActivity;
import com.example.projectmoveek.adapter.HomeVPAdapter;
import com.example.projectmoveek.R;
import com.example.projectmoveek.fragment_main.fragement_tab_home.TabMovieFragment;
import com.example.projectmoveek.fragment_main.fragement_tab_home.TabSeriesFragment;
import com.example.projectmoveek.fragment_main.fragement_tab_home.TabTVShowsFragment;
import com.example.projectmoveek.model.ItemModel;
import com.example.projectmoveek.model.UserModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    // Initialize variables
    private FirebaseAuth mAuth;
    private DatabaseHelper mDbHelper;

    private ViewPager2 viewPagerSlider;
    private List<ItemModel> mListSlider;
    private SliderAdapter sliderAdapter;
    private Handler sliderHandler;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CircleImageView imgAvatar;
    private TextView tvFullname, tvBio;
    private ImageView imgNotify;
    private CardView cardViewSearch;

    // IP tro
//    private String ip = "192.168.0.102";
    // IP nha
//    private String ip = "192.168.0.109";
    // IP dt
    private String ip = "192.168.43.134";

    // URL
    private String url = "http://" + ip + "/android_moveek/get_data_user.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDbHelper = new DatabaseHelper(getContext());

        tabLayout = root.findViewById(R.id.tab_layout);
        viewPager = root.findViewById(R.id.view_pager);
        imgAvatar = root.findViewById(R.id.img_avatar);
        tvFullname = root.findViewById(R.id.tv_name);
        tvBio = root.findViewById(R.id.tv_bio);
        imgNotify = root.findViewById(R.id.img_notify);
        cardViewSearch = root.findViewById(R.id.cv_search);

        // Set up slider
        viewPagerSlider = root.findViewById(R.id.view_pager_slider);
        setUpSlider();

        // Set up TabLayout & ViewPager
        HomeVPAdapter vpAdapter = new HomeVPAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new TabMovieFragment(), "Movie");
        vpAdapter.addFragment(new TabSeriesFragment(), "Series");
        vpAdapter.addFragment(new TabTVShowsFragment(), "TV Show");

        viewPager.setAdapter(vpAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // Set up view profile
        setUpViewProfile();

        // Catch event click on search
        cardViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });


        // Catch event click on notification
        imgNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NotificationActivity.class);
                startActivity(intent);
            }
        });


        return root;
    }

    private void setUpSlider() {
        sliderAdapter = new SliderAdapter(getContext(), viewPagerSlider);
        mListSlider = new ArrayList<>();
        sliderHandler = new Handler();

        mListSlider = mDbHelper.getAllMoviesItemSlider(sliderAdapter);

        sliderAdapter.setData(mListSlider);

        viewPagerSlider.setAdapter(sliderAdapter);

        viewPagerSlider.setClipToPadding(false);
        viewPagerSlider.setClipChildren(false);
        viewPagerSlider.setOffscreenPageLimit(3);
        viewPagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(80));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPagerSlider.setPageTransformer(compositePageTransformer);

        viewPagerSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable,3000);
            }
        });
    }

    private void setUpViewProfile() {
        if (mAuth.getCurrentUser() != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject object = response.getJSONObject(i);
                                    if (object.getString("FID").equals(mAuth.getCurrentUser().getUid())) {
                                        UserModel model = new UserModel(object.getInt("ID"), object.getString("FID")
                                                , object.getString("Email")
                                                , object.getString("Password")
                                                , object.getString("Fullname")
                                                , object.getString("Phonenum")
                                                , object.getString("Avatar")
                                                , object.getString("Bio"));
                                        // Set avatar
                                        Glide.with(getContext()).load(model.getAvatar()).into(imgAvatar);
                                        // Set fullname
                                        tvFullname.setText(model.getFullname());
                                        // Set bio
                                        tvBio.setText(model.getBio());
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getContext(), "Loi json" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("AAA", "Loi phan hoi" + error);
                            Toast.makeText(getContext(), "Loi phan hoi! " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
            requestQueue.add(jsonArrayRequest);
        }
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPagerSlider.setCurrentItem(viewPagerSlider.getCurrentItem() + 1);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        setUpViewProfile();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }
}