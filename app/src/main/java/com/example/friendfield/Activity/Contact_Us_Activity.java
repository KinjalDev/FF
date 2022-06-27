package com.example.friendfield.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.IOException;

public class Contact_Us_Activity extends AppCompatActivity {

    EditText full_name, phone_number, email_id, description;
    AppCompatButton btn_send;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String fullname = "fullnameKey";
    public static final String phonenumber = "number";
    public static final String Email = "emailKey";
    public static final String Discription = "discription";
    ImageView ic_back,img_add_image;
    SharedPreferences.Editor editor;
    public static final int PICK_IMAGE = 1;

    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        ic_back = findViewById(R.id.ic_back_arrow);
        full_name = findViewById(R.id.full_name);
        phone_number = findViewById(R.id.phone_number);
        email_id = findViewById(R.id.email_id);
        description = findViewById(R.id.description);
        btn_send = findViewById(R.id.btn_send);
        img_add_image = findViewById(R.id.img_add_image);

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
        });

        img_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(Contact_Us_Activity.this)
                        .crop()
//                        .galleryOnly()
                        .maxResultSize(1080, 1080)
                        .start(PICK_IMAGE);
            }
        });

        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(fullname)) {
            full_name.setText(sharedPreferences.getString(fullname, ""));
        }

        if (sharedPreferences.contains(phonenumber)) {
            phone_number.setText(sharedPreferences.getString(phonenumber, ""));
        }

        if (sharedPreferences.contains(Email)) {
            email_id.setText(sharedPreferences.getString(Email, ""));
        }

        if (sharedPreferences.contains(Discription)) {
            description.setText(sharedPreferences.getString(Discription, ""));
        }

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = full_name.getText().toString();
                String p = phone_number.getText().toString();
                String e = email_id.getText().toString();
                String d = description.getText().toString();

                if (n.equals("")) {
                    full_name.setError(getResources().getString(R.string.enter_full_name));
                } else if (p.equals("")) {
                    phone_number.setError(getResources().getString(R.string.enter_phone_number));
                } else if (e.equals("")) {
                    email_id.setError(getResources().getString(R.string.enter_emailid));
                } else if (d.equals("")) {
                    description.setError(getResources().getString(R.string.enter_describe_issue));
                } else {
                    editor = sharedPreferences.edit();
                    editor.putString(fullname, n);
                    editor.putString(phonenumber, p);
                    editor.putString(Email, e);
                    editor.putString(Discription, d);
                    editor.commit();

                    full_name.setText("");
                    phone_number.setText("");
                    email_id.setText("");
                    description.setText("");

                    Toast.makeText(Contact_Us_Activity.this, "Seccessfully Add", Toast.LENGTH_SHORT).show();
                }
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
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    Const.bitmap_business_profile_image = bitmap;
                    img_add_image.setImageBitmap(bitmap);
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
        startActivity(new Intent(Contact_Us_Activity.this,SettingActivity.class));
        finish();
    }
}