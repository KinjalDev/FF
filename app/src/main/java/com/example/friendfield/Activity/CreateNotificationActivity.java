package com.example.friendfield.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.friendfield.R;
import com.example.friendfield.Utils.Const;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

public class CreateNotificationActivity extends AppCompatActivity {

    ImageView back_arrow_ic, add_image, img;
    EditText edt_notification_title, edt_notification_link;
    TextInputEditText edt_notification_des;
    AppCompatButton btn_notification_done;
    public static final int PICK_IMAGE = 1;
    Bitmap bitmap = null;
    boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);

        back_arrow_ic = findViewById(R.id.back_arrow_ic);
        add_image = findViewById(R.id.add_image);
        edt_notification_title = findViewById(R.id.edt_notification_title);
        edt_notification_des = findViewById(R.id.edt_notification_des);
        edt_notification_link = findViewById(R.id.edt_notification_link);
        btn_notification_done = findViewById(R.id.btn_notification_done);

        back_arrow_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_notification_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(new Intent(CreateNotificationActivity.this, PromotionActivity.class)));
//                finish();
                String title = edt_notification_title.getText().toString();
                String des = edt_notification_des.getText().toString();
                String link = edt_notification_link.getText().toString();

                if (title.isEmpty()) {
                    edt_notification_title.setError("Enter Notification Title");
                } else if (des.isEmpty()) {
                    edt_notification_des.setError("Enter Notification Description");
                } else if (link.isEmpty()) {
                    edt_notification_link.setError("Enter Link");
                } else {
                    System.out.println("Ans-->>"+link);
                }
            }
        });

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(CreateNotificationActivity.this)
                        .crop()
//                        .galleryOnly()
                        .maxResultSize(1080, 1080)
                        .start(PICK_IMAGE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
//        if (requestCode == RESULT_OK) {

            try {
                Uri selectedImageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    Const.bitmap_business_profile_image = bitmap;
                    add_image.setImageBitmap(bitmap);
                    img = add_image;
                    if (img != null) {
                        status = true;
                    } else {
                        status = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
//            business_profile_image.setImageURI(selectedImageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CreateNotificationActivity.this, PromotionActivity.class));
        finish();
    }
}