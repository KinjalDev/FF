package com.example.friendfield.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.friendfield.Adapter.NotificationAdapter;
import com.example.friendfield.Adapter.PromotionNotificationAdapter;
import com.example.friendfield.MainActivity;
import com.example.friendfield.R;

import java.util.ArrayList;

public class MarketingNotificationActivity extends AppCompatActivity {

    RecyclerView recycler_noti;
    NotificationManagerCompat notificationManager;
    String[] txt_userName = {"Hello World", "Hello World", "Hello World", "Hello World", "Hello World"};
    ImageView hp_back_arrow;
    ArrayList<String> arraylist = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketing_notification);

        recycler_noti = findViewById(R.id.recycler_noti);
        hp_back_arrow = findViewById(R.id.hp_back_arrow);


        int adPos = 0;
        for (int i = 0; i < txt_userName.length; i++) {
            if(adPos == 1) {
                arraylist.add(null);
                adPos=0;
            }
            arraylist.add(txt_userName[i]);
            adPos++;
        }


        notificationManager = NotificationManagerCompat.from(MarketingNotificationActivity.this);

        NotificationAdapter notificationAdapter = new NotificationAdapter(this, arraylist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_noti.setLayoutManager(linearLayoutManager);
        recycler_noti.setAdapter(notificationAdapter);

        hp_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(MarketingNotificationActivity.this, MainActivity.class));
        finish();
    }
}