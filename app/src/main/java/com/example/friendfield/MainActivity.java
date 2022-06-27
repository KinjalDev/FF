package com.example.friendfield;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.friendfield.Activity.ChatingActivity;
import com.example.friendfield.Activity.CreateNewGroupActivity;
import com.example.friendfield.Activity.DisplayAllProductActivity;
import com.example.friendfield.Activity.LikeAndCommentActivity;
import com.example.friendfield.Activity.PromotionActivity;
import com.example.friendfield.Activity.RequestActivity;
import com.example.friendfield.Activity.ProfileActivity;
import com.example.friendfield.Activity.SettingActivity;
import com.example.friendfield.Activity.UserProfileActivity;
import com.example.friendfield.Fragment.CallsFragment;
import com.example.friendfield.Fragment.ChatFragment;
import com.example.friendfield.Fragment.ContactFragment;
import com.example.friendfield.Fragment.MapsFragment;
import com.example.friendfield.Model.PersonalInfo.PeronalRegisterModel;
import com.example.friendfield.Utils.Constans;
import com.example.friendfield.Utils.FileUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    TextView txt_chats, txt_find_friend, txt_calls, txt_contact_list, select;

    ImageView iv_chats;
    ImageView iv_find_friend;
    ImageView iv_calls;
    ImageView iv_contact_list;

    LinearLayout lin_chats;
    LinearLayout lin_find_friend;
    LinearLayout lin_calls;
    LinearLayout lin_contact_list;

    ImageView iv_likes;
    ImageView iv_promotion;

    TextView noti_title, noti_message;
    final String[] token = {""};
    private Intent intent;
    ColorStateList def;
    TextView user_name;
    CircleImageView user_img;
    private static final int REQUEST_CODE = 101;
    private static final int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };
    ImageView iv_business_account, popup_btn;
//    FloatingActionButton fb_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_chats = findViewById(R.id.txt_chats);
        txt_find_friend = findViewById(R.id.txt_find_friend);
        txt_calls = findViewById(R.id.txt_calls);
        txt_contact_list = findViewById(R.id.txt_contact_list);

        iv_chats = findViewById(R.id.iv_chats);
        iv_find_friend = findViewById(R.id.iv_find_friend);
        iv_calls = findViewById(R.id.iv_calls);
        iv_contact_list = findViewById(R.id.iv_contact_list);

        lin_chats = findViewById(R.id.lin_chats);
        lin_find_friend = findViewById(R.id.lin_find_friend);
        lin_calls = findViewById(R.id.lin_calls);
        lin_contact_list = findViewById(R.id.lin_contact_list);


//        select = findViewById(R.id.select);
        user_name = findViewById(R.id.user_name);
        user_img = findViewById(R.id.user_img);
        iv_business_account = findViewById(R.id.iv_business_account);
        iv_likes = findViewById(R.id.iv_likes);
        iv_promotion = findViewById(R.id.iv_promotion);
        popup_btn = findViewById(R.id.popup_btn);
        noti_title = findViewById(R.id.noti_title);
        noti_message = findViewById(R.id.noti_message);
//        fb_map = findViewById(R.id.fb_map);

        def = txt_find_friend.getTextColors();
//        iv_business_account.setVisibility(View.GONE);
        getAllPersonalData();
        Log.e("LLL_c_code-->", MyApplication.getCountryCode(getApplicationContext()));


        if (MyApplication.getuserName(getApplicationContext()).equals("")) {
            user_name.setText(MyApplication.getCountryCode(getApplicationContext()) + " " + MyApplication.getcontactNo(getApplicationContext()));
        } else {
            user_name.setText(MyApplication.getuserName(getApplicationContext()));
        }

//        if (!hasPermissions(this, PERMISSIONS)) {
//            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
//        } else {
//            if (!MyApplication.isPersonalProfileRegistered(getApplicationContext())) {
//                CustomDialog();
//            }
//        }

//        fb_map.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this,MapActivity.class));
//            }
//        });


        Log.e("LLL_auth_token-->", MyApplication.getAuthToken(getApplicationContext()));

        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyApplication.isPersonalProfileRegistered(getApplicationContext())) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));

                } else {
                    startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
                }
            }
        });

        iv_business_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, BusinessProfileActivity.class));
//                startActivity(new Intent(MainActivity.this, ProductActivity.class));
                startActivity(new Intent(MainActivity.this, DisplayAllProductActivity.class));
//                finish();
            }
        });

        iv_likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LikeAndCommentActivity.class));

            }
        });
        iv_promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PromotionActivity.class));
                finish();
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout,
                        new ChatFragment()).commit();

        lin_chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_chats.setTextColor(getResources().getColor(R.color.darkturquoise));
                txt_find_friend.setTextColor(def);
                txt_calls.setTextColor(def);
                txt_contact_list.setTextColor(def);

                iv_chats.setColorFilter(getResources().getColor(R.color.darkturquoise));
                iv_find_friend.setColorFilter(getResources().getColor(R.color.grey));
                iv_calls.setColorFilter(getResources().getColor(R.color.grey));
                iv_contact_list.setColorFilter(getResources().getColor(R.color.grey));

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout,
                                new ChatFragment()).commit();
            }
        });

        lin_find_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txt_find_friend.setTextColor(getResources().getColor(R.color.darkturquoise));
                txt_chats.setTextColor(def);
                txt_calls.setTextColor(def);
                txt_contact_list.setTextColor(def);

                iv_find_friend.setColorFilter(getResources().getColor(R.color.darkturquoise));
                iv_chats.setColorFilter(getResources().getColor(R.color.grey));
                iv_calls.setColorFilter(getResources().getColor(R.color.grey));
                iv_contact_list.setColorFilter(getResources().getColor(R.color.grey));

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout,
                                new MapsFragment()).commit();
            }
        });

        lin_calls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_calls.setTextColor(getResources().getColor(R.color.darkturquoise));
                txt_chats.setTextColor(def);
                txt_find_friend.setTextColor(def);
                txt_contact_list.setTextColor(def);

                iv_calls.setColorFilter(getResources().getColor(R.color.darkturquoise));
                iv_find_friend.setColorFilter(getResources().getColor(R.color.grey));
                iv_chats.setColorFilter(getResources().getColor(R.color.grey));
                iv_contact_list.setColorFilter(getResources().getColor(R.color.grey));

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout,
                                new CallsFragment()).commit();
            }
        });

        lin_contact_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_contact_list.setTextColor(getResources().getColor(R.color.darkturquoise));
                txt_chats.setTextColor(def);
                txt_find_friend.setTextColor(def);
                txt_calls.setTextColor(def);

                iv_contact_list.setColorFilter(getResources().getColor(R.color.darkturquoise));
                iv_find_friend.setColorFilter(getResources().getColor(R.color.grey));
                iv_calls.setColorFilter(getResources().getColor(R.color.grey));
                iv_chats.setColorFilter(getResources().getColor(R.color.grey));

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout,
                                new ContactFragment()).commit();

            }
        });

        popup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, popup_btn);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.main_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.requests:
                                startActivity(new Intent(MainActivity.this, RequestActivity.class));
                                finish();
                                return true;
                            case R.id.setting:
                                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                                finish();
                                return true;
                            case R.id.new_broadcast:
                                startActivity(new Intent(MainActivity.this, CreateNewGroupActivity.class));
                                finish();
                                return true;
                            case R.id.new_group:
                                startActivity(new Intent(MainActivity.this, CreateNewGroupActivity.class));
                                finish();
                                return true;
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });

        //Notification Code
        if (getIntent().getExtras() != null) {
            try {
                if (getIntent().getExtras() != null) {
                    for (String key : getIntent().getExtras().keySet()) {
                        if (key.equals("title"))
                            noti_title.setText(getIntent().getExtras().getString(key));
                        else if (key.equals("message"))
                            noti_message.setText(getIntent().getExtras().getString(key));
                    }

                    if (noti_title.equals("Chat")) {
                        startActivity(new Intent(MainActivity.this, ChatingActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Error Get Data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                }

            } catch (
                    Exception e) {
                e.getMessage();
            }
        }

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isComplete()) {
                    token[0] = task.getResult();
                    Log.e("AppConstants", "onComplete: new Token got: " + token[0]);
                }
            }
        });

        if (getIntent().getExtras() != null) {
            checkFCMBundle(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkFCMBundle(intent);
    }

    private void checkFCMBundle(Intent intent) {
        try {
            intent = getIntent();
            Bundle bundle = intent.getExtras();
            String str = bundle.getString("title");
            String str1 = bundle.getString("body");
            String push = bundle.getString("pushType");

            Log.d("str", str.toString());
            Log.d("str1", str1);

            switch (push) {

                case "TYPE_ONE":
                    startActivity(new Intent(getApplicationContext(), ChatingActivity.class));
                    break;

            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void getAllPersonalData() {

        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constans.fetch_personal_info, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        Log.e("personal", response.toString());
                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                        PeronalRegisterModel peronalInfoModel = new Gson().fromJson(response.toString(), PeronalRegisterModel.class);

                        MyApplication.setuserName(getApplicationContext(), peronalInfoModel.getPersonalInfoModel().getUserName());
                        MyApplication.setPersonalProfileRegistered(getApplicationContext(), peronalInfoModel.getPersonalInfoModel().getIsPersonalProfileRegistered());
                        MyApplication.setBusinessProfileRegistered(getApplicationContext(), peronalInfoModel.getPersonalInfoModel().getIsBusinessProfileRegistered());

                        Log.e("LLL_user_id-->", peronalInfoModel.getPersonalInfoModel().getId());

//                        if (!MyApplication.isPersonalProfileRegistered(getApplicationContext())) {
//                            CustomDialog();
//                        }

                        if (MyApplication.getuserName(getApplicationContext()).equals("")) {
                            user_name.setText(MyApplication.getCountryCode(getApplicationContext()) + " " + MyApplication.getcontactNo(getApplicationContext()));
                        } else {
                            user_name.setText(MyApplication.getuserName(getApplicationContext()));
                        }

                        if (MyApplication.isBusinessProfileRegistered(getApplicationContext())) {
                            iv_business_account.setVisibility(View.VISIBLE);
                        } else {
                            iv_business_account.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
                    map.put("auth-token", MyApplication.getAuthToken(getApplicationContext()));
                    return map;
                }
            };

            RequestQueue referenceQueue = Volley.newRequestQueue(getApplicationContext());
            referenceQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FileUtils.hideKeyboard(MainActivity.this);

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (MyApplication.getAccountType(getApplicationContext()).equals("")) {
                        CustomDialog();
                    }
                } else {
                    finishAffinity();
                }
                break;
        }
    }

    public void CustomDialog() {

        Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_dialog, null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView dialog_close = dialog.findViewById(R.id.dialog_close);
        AppCompatButton dialog_skip = dialog.findViewById(R.id.dialog_skip);
        AppCompatButton dialog_continue = dialog.findViewById(R.id.dialog_continue);

        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!MyApplication.isPersonalProfileRegistered(getApplicationContext())) {
//                    Toast.makeText(MainActivity.this, "Create Yout Profile", Toast.LENGTH_SHORT).show();
//                } else {
                if (dialog.isShowing())
                    dialog.dismiss();

//                }

            }
        });

        dialog_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!MyApplication.isPersonalProfileRegistered(getApplicationContext())) {
//                    Toast.makeText(MainActivity.this, "Create Yout Profile", Toast.LENGTH_SHORT).show();
//                } else {
                if (dialog.isShowing())
                    dialog.dismiss();

//                }
            }
        });

        dialog_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing())
                    dialog.dismiss();

                if (!MyApplication.isPersonalProfileRegistered(getApplicationContext())) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "User profile already registered", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getAllPersonalData();
//
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            if (!MyApplication.isPersonalProfileRegistered(getApplicationContext())) {
                CustomDialog();
            }
        }
//
//        if (!MyApplication.isBusinessProfileRegistered(getApplicationContext())) {
//            iv_business_account.setVisibility(View.GONE);
//        } else {
//            iv_business_account.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}