package com.example.friendfield.Activity;

import static com.android.volley.Request.Method.GET;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.friendfield.Model.Product.ProductModel;
import com.example.friendfield.MyApplication;
import com.example.friendfield.R;
import com.example.friendfield.Utils.Const;
import com.example.friendfield.Utils.Constans;
import com.example.friendfield.Utils.FileUtils;
import com.example.friendfield.Utils.NumberTextWatcher;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {
    ImageView ic_back;
    LinearLayout lin_add_images;
    ImageView img_add_image;
    EditText edt_pro_name;
    EditText edt_pro_price;
    EditText edt_pro_des;
    EditText edt_offer;
    EditText edt_pro_code;
    Button btn_save_product;
    RequestQueue queue;
    Context context;
    String edit_pro;
    TextView txt_title;
    String proId;
    RelativeLayout rl_ads;
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        context = this;
        queue = Volley.newRequestQueue(AddProductActivity.this);


        ic_back = findViewById(R.id.ic_back);
        lin_add_images = findViewById(R.id.lin_add_images);
        img_add_image = findViewById(R.id.img_add_image);
        edt_pro_name = findViewById(R.id.edt_pro_name);
        edt_pro_price = findViewById(R.id.edt_pro_price);
        edt_pro_des = findViewById(R.id.edt_pro_des);
        edt_offer = findViewById(R.id.edt_offer);
        edt_pro_code = findViewById(R.id.edt_pro_code);
        btn_save_product = findViewById(R.id.btn_save_product);
        txt_title = findViewById(R.id.txt_title);
        rl_ads = findViewById(R.id.rl_ads);

        edit_pro = getIntent().getStringExtra("Edit_Pro");
        proId = getIntent().getStringExtra("pro_id");

        if (edit_pro != null) {
            txt_title.setText(edit_pro);
            getProductDetails(proId);
        } else {
            txt_title.setText(getResources().getString(R.string.add_product));
        }

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        img_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(AddProductActivity.this)
                        .crop()
//                        .galleryOnly()
                        .maxResultSize(1080, 1080)
                        .start(PICK_IMAGE);
            }
        });

//        edt_pro_price.addTextChangedListener(new NumberTextWatcher(edt_pro_price, "#,###"));

        btn_save_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_pro_name.getText().toString().equals("")) {
                    edt_pro_name.setError(getResources().getString(R.string.please_enter_pro_name));

                } else if (edt_pro_price.getText().toString().equals("")) {
                    edt_pro_price.setError(getResources().getString(R.string.please_enter_pro_price));

                } else if (edt_pro_des.getText().toString().equals("")) {
                    edt_pro_des.setError(getResources().getString(R.string.please_enter_pro_des));

                } else if (edt_offer.getText().toString().equals("")) {
                    edt_offer.setError(getResources().getString(R.string.please_enter_pro_offer));

                } else if (edt_pro_code.getText().toString().equals("")) {
                    edt_pro_code.setError(getResources().getString(R.string.please_enter_pro_pcode));

                } else {
                    FileUtils.DisplayLoading(context);
                    if (edit_pro != null) {
//                        if (edt_offer.getText().toString().contains("% Off")) {
//                            updateProductInfo(proId, edt_pro_name.getText().toString().trim(), edt_pro_price.getText().toString().trim(), edt_pro_des.getText().toString().trim(), edt_offer.getText().toString().trim(), edt_pro_code.getText().toString().trim());

//                        } else {
                        updateProductInfo(proId, edt_pro_name.getText().toString().trim(), edt_pro_price.getText().toString().trim(), edt_pro_des.getText().toString().trim(), edt_offer.getText().toString().trim(), edt_pro_code.getText().toString().trim());

//                        }
                    } else {
                        addProductInfo(edt_pro_name.getText().toString().trim(), edt_pro_price.getText().toString().trim(), edt_pro_des.getText().toString().trim(), edt_offer.getText().toString().trim(), edt_pro_code.getText().toString().trim());
                    }
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
//                    Const.bitmap_business_profile_image = bitmap;
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

    public void addProductInfo(String pro_name, String pro_price, String pro_des, String pro_offer, String pro_itemcode) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", pro_name);
        params.put("price", pro_price);
        params.put("description", pro_des);
        params.put("offer", pro_offer);
        params.put("itemCode", pro_itemcode);

        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constans.add_product, new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    FileUtils.DismissLoading(context);
                    Log.e("LLL_add_pro-->", response.toString());

                    ProductModel productModel = new Gson().fromJson(response.toString(), ProductModel.class);

//                    startActivity(new Intent(getApplicationContext(), ProductActivity.class));
                    finish();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    FileUtils.DismissLoading(context);
                    Log.e("LLL_add_pro_err-->", error.toString());
                    error.printStackTrace();

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
            queue.add(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProductInfo(String id, String pro_name, String pro_price, String pro_des, String pro_offer, String pro_itemcode) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", pro_name);
        params.put("price", pro_price);
        params.put("description", pro_des);
        params.put("offer", pro_offer);
        params.put("itemCode", pro_itemcode);

        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, Constans.add_product + "/" + id, new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    FileUtils.DismissLoading(context);
                    Log.e("LLL_add_pro-->", response.toString());

                    ProductModel productModel = new Gson().fromJson(response.toString(), ProductModel.class);

//                    startActivity(new Intent(getApplicationContext(), ProductActivity.class));
                    finish();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    FileUtils.DismissLoading(context);
                    Log.e("LLL_add_pro_err-->", error.toString());
                    error.printStackTrace();

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
            queue.add(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getProductDetails(String id) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(GET, Constans.fetch_single_product + id, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    ProductModel productModel = new Gson().fromJson(response.toString(), ProductModel.class);

                    edt_pro_name.setText(productModel.getProductDetailsModel().getName());
                    edt_pro_price.setText(String.valueOf(productModel.getProductDetailsModel().getPrice()));
                    edt_offer.setText(productModel.getProductDetailsModel().getOffer());
                    edt_pro_des.setText(productModel.getProductDetailsModel().getDescription());
                    edt_pro_code.setText(productModel.getProductDetailsModel().getItemCode());


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

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

            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
//        startActivity(new Intent(getApplicationContext(), ProductActivity.class));
        finish();
    }
}