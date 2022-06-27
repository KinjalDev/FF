package com.example.friendfield.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.friendfield.Adapter.ProductDisplayAdapter;
import com.example.friendfield.MainActivity;
import com.example.friendfield.Model.BusinessInfo.BusinessInfoRegisterModel;
import com.example.friendfield.Model.Product.ProductDetailsModel;
import com.example.friendfield.Model.Product.ProductModel;
import com.example.friendfield.MyApplication;
import com.example.friendfield.R;
import com.example.friendfield.Utils.Const;
import com.example.friendfield.Utils.Constans;
import com.example.friendfield.Utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {
    ImageView ic_back;
    ImageView img_business;
    TextView txt_business_name;
    TextView txt_business_des;
    RelativeLayout edt_business_img;
    LinearLayout lin_empty_view;
    LinearLayout lin_not_empty;
    ImageView iv_add_new_product;
    RecyclerView recycle_add_new_product;
    Button btn_add_product;
    Button btn_save;
    TextView txt_skip_for_now;
    LinearLayout lin_add_product;
    Context context;
    ArrayList<ProductDetailsModel> productDetailsModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        context = this;
        ic_back = findViewById(R.id.ic_back);
        img_business = findViewById(R.id.img_business);
        txt_business_name = findViewById(R.id.txt_business_name);
        txt_business_des = findViewById(R.id.txt_business_des);
        edt_business_img = findViewById(R.id.edt_business_img);
        lin_empty_view = findViewById(R.id.lin_empty_view);
        lin_not_empty = findViewById(R.id.lin_not_empty);
        iv_add_new_product = findViewById(R.id.iv_add_new_product);
        recycle_add_new_product = findViewById(R.id.recycle_add_new_product);
        btn_add_product = findViewById(R.id.btn_add_product);
        btn_save = findViewById(R.id.btn_save);
        txt_skip_for_now = findViewById(R.id.txt_skip_for_now);
        lin_add_product = findViewById(R.id.lin_add_product);

        recycle_add_new_product.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (Const.bitmap_business_profile_image != null) {
            img_business.setImageBitmap(Const.bitmap_business_profile_image);
        }

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddProductActivity.class));
//                finish();
            }
        });

        lin_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddProductActivity.class));
//                finish();
            }
        });

        txt_skip_for_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        edt_business_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BusinessProfileActivity.class).putExtra("edit_profile", getResources().getString(R.string.edit_business_profile)));
//                finish();
            }
        });


//        getBusinessInfo();
//        getProductItem();


    }

    public void getBusinessInfo() {
        FileUtils.DisplayLoading(ProductActivity.this);
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constans.fetch_business_info, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    FileUtils.DismissLoading(ProductActivity.this);

                    Log.e("BusinessInfo", response.toString());

                    BusinessInfoRegisterModel businessInfoRegisterModel = new Gson().fromJson(response.toString(), BusinessInfoRegisterModel.class);

//                    Double latitiude = businessInfoRegisterModel.getBusinessInfoModel().getLatitude();
//                    Double logitude = businessInfoRegisterModel.getBusinessInfoModel().getLongitude();

                    txt_business_name.setText(businessInfoRegisterModel.getBusinessInfoModel().getName());
                    txt_business_des.setText(businessInfoRegisterModel.getBusinessInfoModel().getDescription());

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    FileUtils.DismissLoading(ProductActivity.this);
                    Log.e("Error", error.toString());
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

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            FileUtils.DismissLoading(ProductActivity.this);
            e.printStackTrace();

        }
    }

    public void getProductItem() {
//        FileUtils.DisplayLoading(ProductActivity.this);
        JsonObjectRequest jsonObjectRequest = null;
        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constans.add_product, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

//                    FileUtils.DismissLoading(ProductActivity.this);

                    Log.e("LLL_add_pro_dis-->", response.toString());
                    if (!productDetailsModelArrayList.isEmpty()) {
                        productDetailsModelArrayList.clear();
                    }

                    try {
                        JSONArray data_array = response.getJSONArray("data");

                        for (int i = 0; i < data_array.length(); i++) {
                            JSONObject jsonObject = data_array.getJSONObject(i);

                            ProductDetailsModel productDetailsModel = new Gson().fromJson(jsonObject.toString(), ProductDetailsModel.class);
                            productDetailsModelArrayList.add(productDetailsModel);

                        }

                        Log.e("LLL_product_listsize-->", String.valueOf(productDetailsModelArrayList.size()));

                        if (!productDetailsModelArrayList.isEmpty()) {
                            lin_not_empty.setVisibility(View.VISIBLE);
                            lin_empty_view.setVisibility(View.GONE);

                            btn_add_product.setVisibility(View.GONE);
                            txt_skip_for_now.setVisibility(View.GONE);
                            btn_save.setVisibility(View.VISIBLE);

                        } else {
                            lin_empty_view.setVisibility(View.VISIBLE);
                            lin_not_empty.setVisibility(View.GONE);

                            btn_add_product.setVisibility(View.VISIBLE);
                            txt_skip_for_now.setVisibility(View.VISIBLE);
                            btn_save.setVisibility(View.GONE);

                        }

                        ProductDisplayAdapter productDisplayAdapter = new ProductDisplayAdapter(ProductActivity.this, productDetailsModelArrayList);
                        recycle_add_new_product.setAdapter(productDisplayAdapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    FileUtils.DismissLoading(ProductActivity.this);
                    Log.e("LLL_pro_Error", error.toString());
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

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
//            FileUtils.DismissLoading(ProductActivity.this);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBusinessInfo();
        getProductItem();

    }

    @Override
    public void onBackPressed() {
        finish();

    }
}