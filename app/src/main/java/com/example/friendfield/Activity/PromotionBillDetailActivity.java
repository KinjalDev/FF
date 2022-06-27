package com.example.friendfield.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.friendfield.R;

public class PromotionBillDetailActivity extends AppCompatActivity {

    AppCompatButton button_ok;
    TextView publish_date_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_bill_detail);

        button_ok = findViewById(R.id.button_ok);
        publish_date_time = findViewById(R.id.publish_date_time);

        Intent intent_date = getIntent();
        String publish_date = intent_date.getStringExtra("date");
        publish_date_time.setText(publish_date);

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PromotionBillDetailActivity.this, PromotionActivity.class));
                finish();
            }
        });
    }
}