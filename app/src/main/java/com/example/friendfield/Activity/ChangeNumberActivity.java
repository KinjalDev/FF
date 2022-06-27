package com.example.friendfield.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.friendfield.MainActivity;
import com.example.friendfield.R;

public class ChangeNumberActivity extends AppCompatActivity {

    ImageView back_arrow;
    EditText edt_old_number, edt_new_number;
    AppCompatButton btn_change_number;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_number);

        back_arrow = findViewById(R.id.back_arrow);
        edt_old_number = findViewById(R.id.edt_old_number);
        edt_new_number = findViewById(R.id.edt_new_number);
        btn_change_number = findViewById(R.id.btn_change_number);

        btn_change_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeNumberDialog();
            }
        });

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void ChangeNumberDialog() {

        Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(ChangeNumberActivity.this).inflate(R.layout.change_number_dialog, null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView dialog_close = dialog.findViewById(R.id.close);
        AppCompatButton dialog_skip = dialog.findViewById(R.id.dialog_no);
        AppCompatButton dialog_continue = dialog.findViewById(R.id.dialog_yes);

        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChangeNumberActivity.this, "Change Number Successfully!!!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ChangeNumberActivity.this,SettingActivity.class));
        finish();
    }
}