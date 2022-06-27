package com.example.friendfield.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.friendfield.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ChooseUserPromotionActivity extends AppCompatActivity {

    CheckBox chk_exiting_user, chk_app_user;
    AppCompatButton btn_continue;
    ImageView ic_back, ic_close, img_select_package;
    RelativeLayout select_user;
    LinearLayout ll_existing_user, ll_upload_file, ll_txt, ll_phone_email, ll_sample_txt, ll_app_users;
    TextView txt_email_id, txt_ph_no, txt_file_name, txt_user;
    File myFile;
    Uri uri;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user_promotion);

        chk_exiting_user = findViewById(R.id.chk_existing_user);
        chk_app_user = findViewById(R.id.chk_app_user);
        btn_continue = findViewById(R.id.btn_continue);
        ic_back = findViewById(R.id.ic_back);
        ll_existing_user = findViewById(R.id.ll_existing_user);
        ll_upload_file = findViewById(R.id.ll_upload_file);
        ic_close = findViewById(R.id.ic_close);
        txt_email_id = findViewById(R.id.txt_email_id);
        txt_ph_no = findViewById(R.id.txt_ph_no);
        txt_file_name = findViewById(R.id.txt_file_name);
        ll_txt = findViewById(R.id.ll_txt);
        ll_phone_email = findViewById(R.id.ll_phone_email);
        ll_sample_txt = findViewById(R.id.ll_sample_txt);
        select_user = findViewById(R.id.select_user);
        ll_app_users = findViewById(R.id.ll_app_users);
        img_select_package = findViewById(R.id.img_select_package);
        spinner = findViewById(R.id.cat_spinner);
        txt_user = findViewById(R.id.txt_user);

        Intent intent = getIntent();
        String package_txt = intent.getStringExtra("package_name");
        txt_user.setText(package_txt);

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        chk_exiting_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk_exiting_user.isChecked()) {
                    ll_existing_user.setVisibility(View.VISIBLE);
                    ll_app_users.setVisibility(View.GONE);
                    chk_app_user.setChecked(false);
                } else {
                    ll_existing_user.setVisibility(View.GONE);
                }
            }
        });

        chk_app_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk_app_user.isChecked()) {
                    ll_app_users.setVisibility(View.VISIBLE);
                    ll_existing_user.setVisibility(View.GONE);
                    chk_exiting_user.setChecked(false);
                } else {
                    ll_app_users.setVisibility(View.GONE);
                }
            }
        });

        img_select_package.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseUserPromotionActivity.this, SelectUserPackageActivity.class));
                finish();
            }
        });

        select_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseUserPromotionActivity.this, SelectUserActivity.class));
                finish();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseUserPromotionActivity.this, PublishDateTimeActivity.class));
                finish();
            }
        });

        ll_upload_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ChooseUserPromotionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChooseUserPromotionActivity.this, new String[]{
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
                        Toast.makeText(ChooseUserPromotionActivity.this, getResources().getString(R.string.file_delete_successfully), Toast.LENGTH_SHORT).show();
                        ll_upload_file.setVisibility(View.VISIBLE);
                        ll_sample_txt.setVisibility(View.VISIBLE);
                        ll_phone_email.setVisibility(View.GONE);
                        ll_txt.setVisibility(View.GONE);
                        ic_close.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(ChooseUserPromotionActivity.this, getResources().getString(R.string.file_not_delete), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ll_sample_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ChooseUserPromotionActivity.this, R.style.CustomBottomSheetDialogTheme);
                View inflate = LayoutInflater.from(ChooseUserPromotionActivity.this).inflate(R.layout.bottom_dialog, null);
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

        String[] user = getResources().getStringArray(R.array.business_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, user);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
//        spin.setOnItemSelectedListener(this);
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

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (myFile != null) {
            ic_close.setVisibility(View.VISIBLE);
            ll_upload_file.setVisibility(View.GONE);
        } else if (myFile == null) {
            Toast.makeText(this, getResources().getString(R.string.please_upload_file), Toast.LENGTH_SHORT).show();
        }

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    uri = data.getData();
                    String uriString = uri.toString();
                    myFile = new File(uri.getPath());
                    String displayName = null;
                    onReadClick(uri);

                    if (uriString.startsWith("content://")) {
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
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                        Log.d("nameeeee>>>>  ", displayName);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
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
                    System.out.print("" + c_2);

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
        startActivity(new Intent(ChooseUserPromotionActivity.this, PromotionPlanActivity.class));
        finish();
    }
}