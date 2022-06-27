package com.example.friendfield.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.friendfield.R;

public class PromotionPlanActivity extends AppCompatActivity {

    ImageView ic_back;
    AppCompatButton btn_create_notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_plan);

        ic_back = findViewById(R.id.ic_back);
        btn_create_notification = findViewById(R.id.btn_create_notification);

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_create_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PromotionPlanActivity.this, ChooseUserPromotionActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PromotionPlanActivity.this,PromotionActivity.class));
        finish();
    }
}