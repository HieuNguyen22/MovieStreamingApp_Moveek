package com.example.projectmoveek.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.projectmoveek.fragment_main.CategoryFragment;
import com.example.projectmoveek.fragment_main.FavoriteFragment;
import com.example.projectmoveek.fragment_main.HomeFragment;
import com.example.projectmoveek.fragment_main.ProfileFragment;
import com.example.projectmoveek.fragment_main.YourMovFragment;

public class MainVPAdapter extends FragmentStatePagerAdapter {

    public MainVPAdapter(FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new FavoriteFragment();
            case 2:
                return new YourMovFragment();
            case 3:
                return new CategoryFragment();
            case 4:
                return new ProfileFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
