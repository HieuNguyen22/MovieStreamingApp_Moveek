package com.example.projectmoveek.fragment_main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectmoveek.controller.DatabaseHelper;
import com.example.projectmoveek.R;
import com.example.projectmoveek.adapter.ItemAdapterLong;
import com.example.projectmoveek.model.ItemModel;
import com.example.projectmoveek.my_interface.IClickOnItemListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    private RecyclerView rcvFavorite;
    private ItemAdapterLong itemAdapter;
    private List<ItemModel> mList;
    private List<ItemModel> mListTest;
    private DatabaseHelper databaseHelper;
    private FirebaseAuth mAuth;
    private String fidUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_favorite, container, false);

        rcvFavorite = root.findViewById(R.id.rcv_favorite);
        mAuth = FirebaseAuth.getInstance();
        mList = new ArrayList<>();
        mListTest = new ArrayList<>();

        if(mAuth.getCurrentUser() != null)
            fidUser = mAuth.getCurrentUser().getUid();

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

    private void updateData() {
        mList.clear();
        setRecyclerView();
        itemAdapter.notifyDataSetChanged();
    }

    private void setRecyclerView() {
        if (mAuth.getCurrentUser() != null)
            mList = databaseHelper.getMoviesFavor(itemAdapter, fidUser);
        else
            mList = databaseHelper.getAllMoviesItemLong(itemAdapter);

        itemAdapter.setData(mList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);

        rcvFavorite.setLayoutManager(gridLayoutManager);
        rcvFavorite.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }
}