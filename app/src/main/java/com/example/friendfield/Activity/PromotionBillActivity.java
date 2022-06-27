package com.example.friendfield.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.friendfield.R;

public class PromotionBillActivity extends AppCompatActivity {

    ImageView ic_back, img_apply;
    RelativeLayout rl_apply;
    AppCompatButton btn_pay_now;
    TextView txt_date_time, txt_save, txt_remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_bill);

        ic_back = findViewById(R.id.ic_back);
        rl_apply = findViewById(R.id.rl_apply);
        btn_pay_now = findViewById(R.id.btn_pay_now);
        txt_date_time = findViewById(R.id.txt_date_time);
        txt_save = findViewById(R.id.txt_save);
        txt_remove = findViewById(R.id.txt_remove);
        img_apply = findViewById(R.id.img_apply);

        Intent intent_date = getIntent();
        String publish_date = intent_date.getStringExtra("date");
        String save = intent_date.getStringExtra("dis_30");
        txt_date_time.setText(publish_date);

        if (save != null) {
            txt_save.setVisibility(View.VISIBLE);
            txt_remove.setVisibility(View.VISIBLE);
            img_apply.setVisibility(View.GONE);
            txt_save.setText(save);
        }

        txt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_apply.setVisibility(View.VISIBLE);
                txt_save.setVisibility(View.GONE);
                txt_remove.setVisibility(View.GONE);
            }
        });

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        img_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PromotionBillActivity.this, ApplyCouponActivity.class));
                finish();
            }
        });

        btn_pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PromotionBillActivity.this, PaymentActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PromotionBillActivity.this, PublishDateTimeActivity.class));
        finish();
    }
}