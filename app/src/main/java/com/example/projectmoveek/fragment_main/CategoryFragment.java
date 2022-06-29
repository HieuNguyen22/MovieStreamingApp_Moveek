package com.example.projectmoveek.fragment_main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectmoveek.R;
import com.example.projectmoveek.adapter.CategoryVPAdapter;
import com.example.projectmoveek.fragment_main.fragment_tab_category.TabAdventureFragment;
import com.example.projectmoveek.fragment_main.fragment_tab_category.TabDramaFragment;
import com.example.projectmoveek.fragment_main.fragment_tab_category.TabHorrorFragment;
import com.example.projectmoveek.fragment_main.fragment_tab_category.TabScifiFragment;
import com.google.android.material.tabs.TabLayout;

public class CategoryFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_category, container, false);

        tabLayout = root.findViewById(R.id.tab_layout);
        viewPager = root.findViewById(R.id.view_pager);

        CategoryVPAdapter adapter = new CategoryVPAdapter(getChildFragmentManager()
                , FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new TabHorrorFragment(), "Horror");
        adapter.addFragment(new TabScifiFragment(), "Sci-fi");
        adapter.addFragment(new TabAdventureFragment(), "Fantasy");
        adapter.addFragment(new TabDramaFragment(), "Drama");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }
}