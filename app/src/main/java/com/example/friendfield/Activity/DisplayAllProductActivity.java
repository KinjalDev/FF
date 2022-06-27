package com.example.friendfield.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.friendfield.Adapter.AllProductDisplayAdapter;
import com.example.friendfield.Adapter.ProductDisplayAdapter;
import com.example.friendfield.Model.CreateGroupModel;
import com.example.friendfield.Model.Product.ProductDetailsModel;
import com.example.friendfield.MyApplication;
import com.example.friendfield.R;
import com.example.friendfield.Utils.Constans;
import com.example.friendfield.Utils.EqualSpacingItemDecoration;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisplayAllProductActivity extends AppCompatActivity {
    ImageView ic_back;
    RecyclerView recyclerview_product;
    ArrayList<ProductDetailsModel> productDetailsModelArrayList = new ArrayList<>();
    ArrayList<ProductDetailsModel> tempproductDetailsModelArrayList = new ArrayList<>();
    AllProductDisplayAdapter allProductDisplayAdapter;

    EditText edt_search_text;
    ImageView iv_search;
    ImageView iv_clear_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all_product);

        ic_back = findViewById(R.id.ic_back);
        recyclerview_product = findViewById(R.id.recyclerview_product);

        edt_search_text = findViewById(R.id.edt_search_text);
        iv_search = findViewById(R.id.iv_search);
        iv_clear_text = findViewById(R.id.iv_clear_text);


        recyclerview_product.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
//        recyclerview_product.addItemDecoration(new EqualSpacingItemDecoration(50, EqualSpacingItemDecoration.GRID));

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getProductItem();

        edt_search_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        edt_search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    iv_clear_text.setVisibility(View.GONE);
                    iv_search.setVisibility(View.VISIBLE);
                } else {
                    iv_clear_text.setVisibility(View.VISIBLE);
                    iv_search.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                filter(text);

                iv_search.setVisibility(View.GONE);
                iv_clear_text.setVisibility(View.VISIBLE);
            }
        });

        iv_clear_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_search_text.setText("");
                iv_clear_text.setVisibility(View.GONE);
                iv_search.setVisibility(View.VISIBLE);
            }
        });

    }

    private void filter(String text) {
        try {
            // creating a new array list to filter our data.
            ArrayList<ProductDetailsModel> filteredlist = new ArrayList<>();
            Log.e("LLL_tem_li-->", String.valueOf(tempproductDetailsModelArrayList.size()));
            // running a for loop to compare elements.
            for (ProductDetailsModel item : tempproductDetailsModelArrayList) {
                // checking if the entered string matched with any item of our recycler view.
                if (item != null) {
                    if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                        // if the item is matched we are
                        // adding it to our filtered list.
                        filteredlist.add(item);
                    }
                }
            }
            if (filteredlist.isEmpty()) {
                // if no item is added in filtered list we are
                // displaying a toast message as no data found.
                Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
            } else {
                // at last we are passing that filtered
                // list to our adapter class.
                allProductDisplayAdapter.filterList(filteredlist);
            }
        } catch (Exception e) {
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

                        int adPos = 0;
                        for (int i = 0; i < productDetailsModelArrayList.size(); i++) {
                            if (adPos == 2) {
                                tempproductDetailsModelArrayList.add(null);
                                adPos = 0;
                            }
                            tempproductDetailsModelArrayList.add(productDetailsModelArrayList.get(i));
                            adPos++;
                        }


                        allProductDisplayAdapter = new AllProductDisplayAdapter(DisplayAllProductActivity.this, tempproductDetailsModelArrayList);
                        recyclerview_product.setAdapter(allProductDisplayAdapter);

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
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }
}