package com.example.friendfield.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.friendfield.MainActivity;
import com.example.friendfield.MyApplication;
import com.example.friendfield.R;
import com.kyleduo.switchbutton.SwitchButton;

public class SettingActivity extends AppCompatActivity {

    ImageView ic_back_arrow;
    AppCompatButton btn_clear_data;
    SwitchButton noti_switch;
    RelativeLayout chat_backup,block_contact,change_number,contact_us,help,conversation,sign_out;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ic_back_arrow = findViewById(R.id.ic_back_arrow);
        btn_clear_data = findViewById(R.id.btn_clear_data);
        noti_switch = findViewById(R.id.noti_switch);
        block_contact =findViewById(R.id.block_contact);
        chat_backup =findViewById(R.id.chat_backup);
        change_number = findViewById(R.id.change_number);
        contact_us = findViewById(R.id.contact_us);
        help = findViewById(R.id.help);
        conversation = findViewById(R.id.conversation);
        sign_out = findViewById(R.id.sign_out);

        ic_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        chat_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this,BlockedContactActivity.class));
                finish();
            }
        });

        block_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this,BlockedContactActivity.class));
                finish();
            }
        });

        change_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this,ChangeNumberActivity.class));
                finish();
            }
        });

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this,Contact_Us_Activity.class));
                finish();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this,HelpActivity.class));
                finish();
            }
        });

        conversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this,ConversationActivity.class));
                finish();
            }
        });

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyApplication.setuserActive(getApplicationContext(),false);
                Intent intent =new Intent(SettingActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingActivity.this, MainActivity.class));
        finish();
    }
}