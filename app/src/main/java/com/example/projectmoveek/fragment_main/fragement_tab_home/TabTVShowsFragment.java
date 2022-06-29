package com.example.projectmoveek.fragment_main.fragement_tab_home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmoveek.controller.DatabaseHelper;
import com.example.projectmoveek.R;
import com.example.projectmoveek.adapter.ItemAdapter;
import com.example.projectmoveek.model.ItemModel;
import com.example.projectmoveek.my_interface.IClickOnItemListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class TabTVShowsFragment extends Fragment {

    private RecyclerView rcvRecommend, rcvPopular, rcvNew;
    private ItemAdapter itemAdapter;
    private List<ItemModel> mList;
    private DatabaseHelper databaseHelper;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_tab_tv_show, container, false);

        rcvRecommend = root.findViewById(R.id.rcv_show_recommend);
        rcvPopular = root.findViewById(R.id.rcv_show_popular);
        rcvNew = root.findViewById(R.id.rcv_show_new);

        mAuth = FirebaseAuth.getInstance();
        mList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(getContext());

        // Initialize adapter
        itemAdapter = new ItemAdapter(getContext(), new IClickOnItemListener() {
            @Override
            public void onClickItem(ItemModel item) {
                updateData();
            }
        });

        // Set up for Recyclerview
        setRecyclerView();

        return root;
    }

    private void updateData(){
        mList.clear();
        setRecyclerView();

        itemAdapter.notifyDataSetChanged();
    }

    private void setRecyclerView(){
        if (mAuth.getCurrentUser() != null)
            mList = databaseHelper.getAllMoviesItem(itemAdapter);
        else
            mList = databaseHelper.getAllMoviesItem(itemAdapter);

        itemAdapter.setData(mList);

        GridLayoutManager gridLayoutManagerRecommend = new GridLayoutManager(getContext(),1,RecyclerView.HORIZONTAL, false);
        GridLayoutManager gridLayoutManagerPopular = new GridLayoutManager(getContext(),1,RecyclerView.HORIZONTAL, false);
        GridLayoutManager gridLayoutManagerNew = new GridLayoutManager(getContext(),1,RecyclerView.HORIZONTAL, false);

        rcvRecommend.setLayoutManager(gridLayoutManagerRecommend);
        rcvRecommend.setAdapter(itemAdapter);

        rcvPopular.setLayoutManager(gridLayoutManagerPopular);
        rcvPopular.setAdapter(itemAdapter);

        rcvNew.setLayoutManager(gridLayoutManagerNew);
        rcvNew.setAdapter(itemAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }
}
