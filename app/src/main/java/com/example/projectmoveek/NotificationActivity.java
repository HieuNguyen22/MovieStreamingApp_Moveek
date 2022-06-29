package com.example.projectmoveek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.projectmoveek.adapter.NotifyAdapter;
import com.example.projectmoveek.controller.DatabaseHelper;
import com.example.projectmoveek.model.NotifyModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView rcvNotify;
    private NotifyAdapter notifyAdapter;
    private List<NotifyModel> mList;
    private DatabaseHelper databaseHelper;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        rcvNotify = findViewById(R.id.rcv_noti);
        notifyAdapter = new NotifyAdapter(this);
        mList = new ArrayList<>();

        // Add data
        addDataRCV();

        // Set up recycler view
        notifyAdapter.setData(mList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        rcvNotify.setLayoutManager(gridLayoutManager);
        rcvNotify.setAdapter(notifyAdapter);
        notifyAdapter.notifyDataSetChanged();

    }

    private void addDataRCV() {
        mList.add(new NotifyModel("Cap nhat", "There's one day, you'll miss me. So live a life, you will remember", "22/07/2001 20:11:22"));
        mList.add(new NotifyModel("Tai phim thanh cong", "There's one day, you'll miss me. So live a life, you will remember", "22/07/2001 20:11:22"));
        mList.add(new NotifyModel("Loi cai dat", "There's one day, you'll miss me. So live a life, you will remember", "22/07/2001 20:11:22"));
        mList.add(new NotifyModel("Loi cai dat", "There's one day, you'll miss me. So live a life, you will remember", "22/07/2001 20:11:22"));
        mList.add(new NotifyModel("Loi cai dat", "There's one day, you'll miss me. So live a life, you will remember", "22/07/2001 20:11:22"));
        mList.add(new NotifyModel("Loi cai dat", "There's one day, you'll miss me. So live a life, you will remember", "22/07/2001 20:11:22"));
        mList.add(new NotifyModel("Loi cai dat", "There's one day, you'll miss me. So live a life, you will remember", "22/07/2001 20:11:22"));
        mList.add(new NotifyModel("Loi cai dat", "There's one day, you'll miss me. So live a life, you will remember", "22/07/2001 20:11:22"));
        mList.add(new NotifyModel("Loi cai dat", "There's one day, you'll miss me. So live a life, you will remember", "22/07/2001 20:11:22"));
        mList.add(new NotifyModel("Loi cai dat", "There's one day, you'll miss me. So live a life, you will remember", "22/07/2001 20:11:22"));
        mList.add(new NotifyModel("Loi cai dat", "There's one day, you'll miss me. So live a life, you will remember", "22/07/2001 20:11:22"));
        mList.add(new NotifyModel("Loi cai dat", "There's one day, you'll miss me. So live a life, you will remember", "22/07/2001 20:11:22"));
        mList.add(new NotifyModel("Loi cai dat", "There's one day, you'll miss me. So live a life, you will remember", "22/07/2001 20:11:22"));
        mList.add(new NotifyModel("Loi cai dat", "There's one day, you'll miss me. So live a life, you will remember", "22/07/2001 20:11:22"));
        mList.add(new NotifyModel("Loi cai dat", "There's one day, you'll miss me. So live a life, you will remember", "22/07/2001 20:11:22"));
        mList.add(new NotifyModel("Loi cai dat", "There's one day, you'll miss me. So live a life, you will remember", "22/07/2001 20:11:22"));
    }
}