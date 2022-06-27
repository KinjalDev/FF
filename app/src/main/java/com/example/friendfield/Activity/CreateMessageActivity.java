package com.example.friendfield.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.friendfield.Model.CreateMessage.CreateMessageTokenModel;
import com.example.friendfield.MyApplication;
import com.example.friendfield.R;
import com.example.friendfield.Utils.Const;
import com.example.friendfield.Utils.Constans;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class  CreateMessageActivity extends AppCompatActivity {

    ImageView ic_back_arrow, image, iv_location, ic_close;
    TextView txt_file_name, txt_ph_no, txt_email_id;
    RelativeLayout rl_upload, select_user;
    LinearLayout ll_upload, ll_upload_file, ll_sample_txt, ll_phone_email, ll_txt;
    EditText edt_notifi_title, edt_notifi_des, edt_link;
    AppCompatButton btn_create_message;
    public static final int PICK_IMAGE = 1;
    Bitmap bitmap = null;
    boolean flag = false;
    int user = 1;
    File myFile;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);

        ic_back_arrow = findViewById(R.id.ic_back_arrow);
        rl_upload = findViewById(R.id.rl_upload);
        image = findViewById(R.id.image);
        ll_upload = findViewById(R.id.ll_upload);
        ll_upload_file = findViewById(R.id.ll_upload_file);
        edt_notifi_title = findViewById(R.id.edt_notifi_title);
        edt_notifi_des = findViewById(R.id.edt_notifi_des);
        edt_link = findViewById(R.id.edt_link);
        btn_create_message = findViewById(R.id.btn_create_message);
        select_user = findViewById(R.id.select_user);
        iv_location = findViewById(R.id.iv_location);
        ll_sample_txt = findViewById(R.id.ll_sample_txt);
        txt_file_name = findViewById(R.id.txt_file_name);
        ic_close = findViewById(R.id.ic_close);
        txt_ph_no = findViewById(R.id.txt_ph_no);
        txt_email_id = findViewById(R.id.txt_email_id);
        ll_phone_email = findViewById(R.id.ll_phone_email);
        ll_txt = findViewById(R.id.ll_txt);


        ic_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rl_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(CreateMessageActivity.this)
                        .crop()
                        .maxResultSize(1080, 1080)
                        .start(PICK_IMAGE);
            }
        });

        btn_create_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_notifi_title.getText().toString().equals("")) {
                    edt_notifi_title.setError(getResources().getString(R.string.enter_noti_title));
                } else if (edt_notifi_des.getText().toString().equals("")) {
                    edt_notifi_des.setError(getResources().getString(R.string.enter_noti_description));
                } else if (edt_link.getText().toString().equals("")) {
                    edt_link.setError(getResources().getString(R.string.enter_link));
                } else if (Const.bitmap_business_profile_image == null) {
                    flag = false;
                    Toast.makeText(CreateMessageActivity.this, getResources().getString(R.string.upload_image), Toast.LENGTH_SHORT).show();
                } else {
                    if (edt_link != null) {
                        createMessagePatchApi(edt_notifi_title.getText().toString().trim(), edt_notifi_des.getText().toString().trim(), edt_link.getText().toString().trim(), flag, user);
                    } else {
                        createMessagePostApi(edt_notifi_title.getText().toString().trim(), edt_notifi_des.getText().toString().trim(), edt_link.getText().toString().trim(), flag, user);
                        Toast.makeText(CreateMessageActivity.this, getResources().getString(R.string.successfully_adding), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateMessageActivity.this, MapsLocationActivity.class));
                finish();
            }
        });

        select_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateMessageActivity.this, SelectUserActivity.class));
                finish();
            }
        });

        ll_upload_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(CreateMessageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreateMessageActivity.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    selectExcel();
                }
            }
        });

        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");

                    String fullPath = getPathFromExtSD(split);
                    File file = new File(fullPath);

                    if (file.exists()) {
                        file.delete();
                        Toast.makeText(CreateMessageActivity.this, getResources().getString(R.string.file_delete_successfully), Toast.LENGTH_SHORT).show();
                        ll_upload_file.setVisibility(View.VISIBLE);
                        ll_sample_txt.setVisibility(View.VISIBLE);
                        ll_phone_email.setVisibility(View.GONE);
                        ll_txt.setVisibility(View.GONE);
                        ic_close.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(CreateMessageActivity.this, getResources().getString(R.string.file_not_delete), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ll_sample_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(CreateMessageActivity.this, R.style.CustomBottomSheetDialogTheme);
                View inflate = LayoutInflater.from(CreateMessageActivity.this).inflate(R.layout.bottom_dialog, null);
                bottomSheetDialog.setContentView(inflate);

                ImageView bottom_img = inflate.findViewById(R.id.bottom_img);

                try {
                    InputStream ims = getAssets().open("sample_exe_file.png");
                    Drawable d = Drawable.createFromStream(ims, null);
                    bottom_img.setImageDrawable(d);
                } catch (IOException ex) {
                    return;
                }

                bottomSheetDialog.show();
            }
        });
    }

    private String getPathFromExtSD(String[] pathData) {
        final String relativePath = "/" + pathData[1];

        return relativePath;
    }

    private void selectExcel() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        startActivityForResult(intent, 1);
    }


    private void createMessagePatchApi(String text_title, String text_des, String text_link, boolean flag, int text_user) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("title", text_title);
        params.put("description", text_des);
        params.put("link", text_link);
        params.put("hasImage", String.valueOf(flag));
        params.put("userId", String.valueOf(text_user));

        JsonObjectRequest jsonObjectRequest = null;
        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, Constans.marketing_notification, new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("LL_patch_message-->", response.toString());

                    CreateMessageTokenModel createMessageTokenModel = new Gson().fromJson(response.toString(), CreateMessageTokenModel.class);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("LL_patch_message_err--> " + error.toString());
                    error.printStackTrace();
                    Toast.makeText(CreateMessageActivity.this, getResources().getString(R.string.data_not_submit)  + error, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("Content-Type", "application/json");
                    map.put("auth-token", MyApplication.getAuthToken(getApplicationContext()));
                    return map;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(CreateMessageActivity.this);
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("LL_patch_error-->", e.getMessage());
        }
    }

    private void createMessagePostApi(String txt_title, String txt_des, String txt_link, boolean flag, int txt_user) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("title", txt_title);
        params.put("description", txt_des);
        params.put("link", txt_link);
        params.put("hasImage", String.valueOf(flag));
        params.put("userId", String.valueOf(txt_user));

        JsonObjectRequest jsonObjectRequest = null;
        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constans.marketing_notification, new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("LL_message-->", response.toString());

                    CreateMessageTokenModel createMessageTokenModel = new Gson().fromJson(response.toString(), CreateMessageTokenModel.class);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("LLL_message_err--> " + error.toString());
                    error.printStackTrace();
                    Toast.makeText(CreateMessageActivity.this, getResources().getString(R.string.data_not_submit) + error, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("Content-Type", "application/json");
                    map.put("auth-token", MyApplication.getAuthToken(getApplicationContext()));
                    return map;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(CreateMessageActivity.this);
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("LLL_messageerror-->", e.getMessage());
        }
    }

    @SuppressLint("Range")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {

            try {
                Uri selectedImageUri = data.getData();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    Const.bitmap_business_profile_image = bitmap;
                    image.setImageBitmap(bitmap);
                    image.setVisibility(View.VISIBLE);
                    ll_upload.setVisibility(View.GONE);
                    flag = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.task_cancelled), Toast.LENGTH_SHORT).show();
        }

        if (myFile != null) {
            ic_close.setVisibility(View.VISIBLE);
            ll_upload_file.setVisibility(View.GONE);
        } else if (myFile == null) {
            Toast.makeText(this, getResources().getString(R.string.please_upload_file), Toast.LENGTH_SHORT).show();
        }

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                uri = data.getData();
                String uriString = uri.toString();
                myFile = new File(uri.getPath());
                String displayName = null;
                onReadClick(uri);


                Cursor cursor = null;
                try {
                    cursor = this.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.e("nameeeee>>>>", displayName);
                        txt_file_name.setText(displayName);
                        ll_txt.setVisibility(View.VISIBLE);
                        ll_phone_email.setVisibility(View.VISIBLE);
                        ll_sample_txt.setVisibility(View.GONE);

                    }
                } finally {
                    cursor.close();
                }
            }
        }
    }

    private void onReadClick(Uri uriString) {
        try {
            InputStream stream = getContentResolver().openInputStream(uriString);
            XSSFWorkbook workbook = new XSSFWorkbook(stream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();

            ArrayList<String> email = new ArrayList<>();

            int column_index_2 = 0;
            Row row = sheet.getRow(0);
            for (Cell cell : row) {
                switch (cell.getStringCellValue()) {
                    case "Email":
                        column_index_2 = cell.getColumnIndex();
                        break;
                }
            }

            for (Row r : sheet) {
                if (r.getRowNum() == 0) continue;
                Cell c_2 = r.getCell(column_index_2);
                if (c_2 != null) {
                    System.out.print("   " + c_2);

                    email.add(String.valueOf(c_2));
                    txt_email_id.setText(String.valueOf(email.size()));
                }
            }
            txt_ph_no.setText(String.valueOf(rowsCount));

        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectExcel();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CreateMessageActivity.this, MarketingNotificationActivity.class));
        finish();
    }
}