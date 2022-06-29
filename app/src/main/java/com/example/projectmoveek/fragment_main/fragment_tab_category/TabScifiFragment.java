package com.example.projectmoveek.fragment_main.fragment_tab_category;

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

public class TabScifiFragment extends Fragment {
    private RecyclerView rcvScifi;
    private ItemAdapter itemAdapter;
    private List<ItemModel> mList;
    private DatabaseHelper databaseHelper;
    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_category_scifi, container, false);

        rcvScifi = root.findViewById(R.id.rcv_category_scifi);
        mAuth = FirebaseAuth.getInstance();
        mList = new ArrayList<>();

        itemAdapter = new ItemAdapter(getContext(), new IClickOnItemListener() {
            @Override
            public void onClickItem(ItemModel item) {
                updateData();
            }
        });

        // Add data to recycler view
        setRecyclerView();

        return root;
    }

    private void setRecyclerView() {
        databaseHelper = new DatabaseHelper(getContext());
        if (mAuth.getCurrentUser() != null)
            mList = databaseHelper.getAllMoviesItem(itemAdapter);
        else
            mList = databaseHelper.getAllMoviesItem(itemAdapter);

        itemAdapter.setData(mList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        rcvScifi.setLayoutManager(gridLayoutManager);
        rcvScifi.setAdapter(itemAdapter);
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
