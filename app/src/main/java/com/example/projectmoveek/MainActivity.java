package com.example.projectmoveek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.projectmoveek.adapter.MainVPAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottom_nav;
    private ViewPager viewPager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottom_nav = findViewById(R.id.bottom_nav);
        viewPager = findViewById(R.id.view_pager);
        mAuth = FirebaseAuth.getInstance();

        // Navigate by navigation
        bottom_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.nav_favor:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.nav_your_movies:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.nav_category:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.nav_profile:
                        viewPager.setCurrentItem(4);
                        break;
                    default:
                        viewPager.setCurrentItem(0);
                        break;
                }
                return true;
            }
        });

        // Navigate by ViewPager
        setUpViewPager();

    }

    private void setUpViewPager() {
        MainVPAdapter mainVPAdapter = new MainVPAdapter(getSupportFragmentManager()
                , FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(mainVPAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottom_nav.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;
                    case 1:
                        bottom_nav.getMenu().findItem(R.id.nav_favor).setChecked(true);
                        break;
                    case 2:
                        bottom_nav.getMenu().findItem(R.id.nav_your_movies).setChecked(true);
                        break;
                    case 3:
                        bottom_nav.getMenu().findItem(R.id.nav_category).setChecked(true);
                        break;
                    case 4:
                        bottom_nav.getMenu().findItem(R.id.nav_profile).setChecked(true);
                        break;
                    default:
                        bottom_nav.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

}