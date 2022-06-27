package com.example.friendfield.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.friendfield.R;

public class ApplyCouponActivity extends AppCompatActivity {

    ImageView back_btn;
    AppCompatButton btn_30_apply,btn_50_apply,btn_80_apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_coupon);

        back_btn = findViewById(R.id.back_btn);
        btn_30_apply = findViewById(R.id.btn_30_apply);
        btn_50_apply = findViewById(R.id.btn_50_apply);
        btn_80_apply = findViewById(R.id.btn_80_apply);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_30_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_30 = new Intent(ApplyCouponActivity.this,PromotionBillActivity.class);
                i_30.putExtra("dis_30","30% Save Coupon");
                startActivity(i_30);
            }
        });

        btn_50_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_30 = new Intent(ApplyCouponActivity.this,PromotionBillActivity.class);
                i_30.putExtra("dis_30","50% Save Coupon");
                startActivity(i_30);
            }
        });

        btn_80_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_30 = new Intent(ApplyCouponActivity.this,PromotionBillActivity.class);
                i_30.putExtra("dis_30","80% Save Coupon");
                startActivity(i_30);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ApplyCouponActivity.this,PromotionBillActivity.class));
        finish();
    }
}