package com.example.friendfield.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.friendfield.Adapter.BlockeContactAdapter;
import com.example.friendfield.R;

public class BlockedContactActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    int[] user_img = {R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user,R.drawable.ic_user};
    String[] contact = {"+91 8596741236","+91 8596741236","+91 8596741236","+91 8596741236","+91 8596741236","+91 8596741236","+91 8596741236","+91 8596741236","+91 8596741236","+91 8596741236"};
    ImageView ic_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_contact);

        recyclerView = findViewById(R.id.recyclerView);
        ic_back = findViewById(R.id.ic_back_arrow);
        BlockeContactAdapter blockeContactAdapter = new BlockeContactAdapter(this,user_img,contact);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(blockeContactAdapter);

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BlockedContactActivity.this,SettingActivity.class));
        finish();
    }
}