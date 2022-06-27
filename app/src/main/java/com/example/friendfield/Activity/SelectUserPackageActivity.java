package com.example.friendfield.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.friendfield.Adapter.BuyPlanAdapter;
import com.example.friendfield.R;

public class SelectUserPackageActivity extends AppCompatActivity {
    ImageView ic_back;
    RecyclerView recycler_buy_plan;
    String[] plan_name = {"FOR 999 USERS", "FOR 9999 USERS", "FOR 99999 USERS"};
    String[] plan_notification = {"69 FOR 999 USERS","299 FOR 9999 USERS","1799 FOR 99999 USERS"};
    String[] plan_sms = {"399 FOR 999 USERS","1999 FOR 9999 USERS","16999 FOR 99999 USERS"};
    String[] plan_email = {"89 FOR 999 USERS","8399 FOR 9999 USERS","3399 FOR 99999 USERS"};
    String[] plan_all = {"475 FOR 999 USERS","2299 FOR 9999 USERS","18999 FOR 99999 USERS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecte_user_package);

        ic_back = findViewById(R.id.ic_back);
        recycler_buy_plan = findViewById(R.id.recycler_buy_plan);

        recycler_buy_plan.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        BuyPlanAdapter buyPlanAdapter = new BuyPlanAdapter(this, plan_name, plan_notification, plan_sms, plan_email,plan_all);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycler_buy_plan.setLayoutManager(manager);
        recycler_buy_plan.setAdapter(buyPlanAdapter);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SelectUserPackageActivity.this,ChooseUserPromotionActivity.class));
        finish();
    }
}