package com.example.friendfield.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
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
import com.example.friendfield.Adapter.ViewPagerAdapter;
import com.example.friendfield.Model.UserProfile.UserProfileRegisterModel;
import com.example.friendfield.MyApplication;
import com.example.friendfield.R;
import com.example.friendfield.Utils.Const;
import com.example.friendfield.Utils.Constans;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    RelativeLayout edit_img;
    TextView u_name, u_nickname;
    CircleImageView user_profile_image;
    ImageView ic_back, ic_fb, ic_insta, ic_twitter, ic_linkedin;
    private static final int PICK_IMAGE = 100;
    AppCompatButton btn_edit_profile;
    LinearLayout ll_business_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        edit_img = findViewById(R.id.edit_img);
        ic_back = findViewById(R.id.ic_back);
        user_profile_image = findViewById(R.id.user_profile_image);
        u_name = findViewById(R.id.u_name);
        u_nickname = findViewById(R.id.u_nickname);
        btn_edit_profile = findViewById(R.id.btn_edit_profile);
        ic_fb = findViewById(R.id.ic_fb);
        ic_insta = findViewById(R.id.ic_insta);
        ic_twitter = findViewById(R.id.ic_twitter);
        ic_linkedin = findViewById(R.id.ic_linkdin);
        ll_business_product = findViewById(R.id.ll_business_product);

        if (!MyApplication.isBusinessProfileRegistered(UserProfileActivity.this)) {
            ll_business_product.setVisibility(View.GONE);
        } else {
            ll_business_product.setVisibility(View.VISIBLE);
        }

        ll_business_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfileActivity.this, ProductActivity.class));
//                finish();
            }
        });

        if (Const.bitmap_profile_image != null) {
            user_profile_image.setImageBitmap(Const.bitmap_profile_image);
        }


        getApiCalling();


        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });


        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.tab_personal_info)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.tab_business_info)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.tab_my_reels)));

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, ProfileActivity.class);
                intent.putExtra("edit_p_profile", getResources().getString(R.string.edit_personal_profile));
                startActivity(intent);
//                finish();
            }
        });
    }

    private void getApiCalling() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constans.fetch_personal_info, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                UserProfileRegisterModel userProfileRegisterModel = new Gson().fromJson(response.toString(), UserProfileRegisterModel.class);
                u_name.setText(userProfileRegisterModel.getUserProfileModel().getFullName());
                u_nickname.setText(userProfileRegisterModel.getUserProfileModel().getNickName());

                if (userProfileRegisterModel.getUserProfileModel().getFacebookLink().equals("")) {
                    ic_fb.setVisibility(View.GONE);
                } else {
                    ic_fb.setVisibility(View.VISIBLE);
                }

                if (userProfileRegisterModel.getUserProfileModel().getInstagramLink().equals("")) {
                    ic_insta.setVisibility(View.GONE);
                } else {
                    ic_insta.setVisibility(View.VISIBLE);
                }

                if (userProfileRegisterModel.getUserProfileModel().getTwitterLink().equals("")) {
                    ic_twitter.setVisibility(View.GONE);
                } else {
                    ic_twitter.setVisibility(View.VISIBLE);
                }

                if (userProfileRegisterModel.getUserProfileModel().getLinkedinLink().equals("")) {
                    ic_linkedin.setVisibility(View.GONE);
                } else {
                    ic_linkedin.setVisibility(View.VISIBLE);
                }

                ic_fb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (u_name.equals("")) {
                            Toast.makeText(UserProfileActivity.this, "Enter Data", Toast.LENGTH_SHORT).show();
                        } else {
                            String url = userProfileRegisterModel.getUserProfileModel().getFacebookLink();
                            if (url.startsWith("www") || url.startsWith("https://") || url.startsWith("http://")) {
                                Uri uri = Uri.parse(url);
                                Intent i_fb = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(i_fb);

                            } else {
                                Toast.makeText(UserProfileActivity.this, "Invalid Url", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                ic_insta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = userProfileRegisterModel.getUserProfileModel().getInstagramLink();
                        if (url.startsWith("www") || url.startsWith("https://") || url.startsWith("http://")) {
                            Uri uri = Uri.parse(url);
                            Intent i_insta = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(i_insta);
                        } else {
                            Toast.makeText(UserProfileActivity.this, "Invalid Url", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                ic_twitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String url = userProfileRegisterModel.getUserProfileModel().getTwitterLink();
                        if (url.startsWith("https://") || url.startsWith("http://")) {
                            Uri uri = Uri.parse(url);
                            Intent i_twitter = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(i_twitter);
                        } else {
                            Toast.makeText(UserProfileActivity.this, "Invalid Url", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                ic_linkedin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = userProfileRegisterModel.getUserProfileModel().getLinkedinLink();
                        if (url.startsWith("https://") || url.startsWith("http://")) {
                            Uri uri = Uri.parse(url);
                            Intent i_linkedin = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(i_linkedin);
                        } else {
                            Toast.makeText(UserProfileActivity.this, "Invalid Url", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json");
                map.put("auth-token", MyApplication.getAuthToken(UserProfileActivity.this));
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(UserProfileActivity.this);
        requestQueue.add(jsonObjectRequest);
    }

    private void openGallery() {
        ImagePicker.Companion.with(UserProfileActivity.this)
                .crop()
                .maxResultSize(1080, 1080)
                .start(PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {

            try {
                Uri selectedImageUri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    user_profile_image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        super.onBackPressed();
    }
}