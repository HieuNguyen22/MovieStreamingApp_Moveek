package com.example.projectmoveek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.projectmoveek.adapter.ItemAdapterLong;
import com.example.projectmoveek.controller.DatabaseHelper;
import com.example.projectmoveek.model.ItemModel;
import com.example.projectmoveek.my_interface.IClickOnItemListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView rcvSearch;
    private ItemAdapterLong itemAdapter;
    private List<ItemModel> mList;
    private DatabaseHelper databaseHelper;
    private FirebaseAuth mAuth;
    private EditText edtSearch;
    private ImageView imgBack;

    // IP tro
    private String ip = "192.168.0.102";
    // IP nha
//    private String ip = "192.168.0.109";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        overridePendingTransition(R.anim.slide_up_trans,R.anim.slide_down_trans);

        rcvSearch = findViewById(R.id.rcv_search);
        edtSearch = findViewById(R.id.edt_search);
        imgBack = findViewById(R.id.img_back);
        mAuth = FirebaseAuth.getInstance();
        mList = new ArrayList<>();

        databaseHelper = new DatabaseHelper(this);

        itemAdapter = new ItemAdapterLong(this, new IClickOnItemListener() {
            @Override
            public void onClickItem(ItemModel item) {
                updateData();
            }
        });

        setRecyclerView();

        // Catch event search
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strKeyword = edtSearch.getText().toString();
                itemAdapter.getFilter().filter(strKeyword);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Catch event back
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_up_trans,R.anim.slide_down_trans);
            }
        });

    }

    private void setRecyclerView() {
        if (mAuth.getCurrentUser() != null)
            mList = databaseHelper.getAllMoviesItemLong(itemAdapter);
        else
            mList = databaseHelper.getAllMoviesItemLong(itemAdapter);

        itemAdapter.setData(mList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);

        rcvSearch.setLayoutManager(gridLayoutManager);
        rcvSearch.setAdapter(itemAdapter);
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