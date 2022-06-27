package com.example.friendfield.Activity;

import static com.example.friendfield.Utils.Const.tag_str;
import static com.example.friendfield.Utils.Const.taglist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.friendfield.MainActivity;
import com.example.friendfield.Model.Profile.Register.ProfileRegisterModel;
import com.example.friendfield.MyApplication;
import com.example.friendfield.R;
import com.example.friendfield.TagView.ContactsCompletionView;
import com.example.friendfield.TagView.Person;
import com.example.friendfield.TagView.PersonAdapter;
import com.example.friendfield.TagView.TokenCompleteTextView;
import com.example.friendfield.Utils.Const;
import com.example.friendfield.Utils.Constans;
import com.example.friendfield.Utils.FileUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.skyhope.materialtagview.TagView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import me.bendik.simplerangeview.SimpleRangeView;

public class ProfileActivity<PersonModel> extends AppCompatActivity implements OnMapReadyCallback, LocationListener, TokenCompleteTextView.TokenListener<Person> {

    RadioGroup radioGender;
    RadioButton selectBtn;
    EditText edt_name, edt_nickname, edt_emailId, edt_fb, edt_insta, edt_twitter, edt_linkdin, edt_address, edt_aboutUs, edt_hobbies, edt_pintrest, edt_youtube;
    AppCompatButton btn_save, btn_business;
    ImageView ic_back;
//    CheckBox chk;
    LinearLayout ll_chk;
    LocationManager locationManager;
    private static final int REQUEST_CODE = 101;
    SeekBar seekbar_range;
    CircleImageView profile_image;
    private static final int PICK_IMAGE = 100;
    Marker mCurrLocationMarker;
    RelativeLayout edit_profile;
    TextView t_km, title, txt_min_age, txt_max_age, gender;
    int p = 0;
    int text_km = 0;
    int txt_min = 0;
    int txt_max = 0;
    SimpleRangeView rangeBar;
    String yourRd = "MALE";
    String hello = "";
    String profile_title = "";
    ImageView iv_location;
    LatLng latLng;
    MapView mapview;
    private GoogleMap map;
    public Criteria criteria;
    public String bestProvider;
    String longitude;
    String lattitude;
    RelativeLayout rl_map;
    RelativeLayout relative_map;
    Boolean isSaveAndCreateBusiness = false;
    ContactsCompletionView completionView;
    ArrayAdapter<Person> adapter;
    Person[] people;
//    ArrayList<String> taglist = new ArrayList<>();

    public ProfileActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        people = Person.samplePeople();
        adapter = new PersonAdapter(this, R.layout.person_layout, people);

        completionView = (ContactsCompletionView) findViewById(R.id.tagView);
        completionView.setAdapter(adapter);
        completionView.setThreshold(1);
        completionView.setTokenListener(ProfileActivity.this);
        completionView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);
        completionView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ((TextView) findViewById(R.id.textValue)).setText(editable.toString());

            }
        });

        edt_name = findViewById(R.id.edt_name);
        edt_nickname = findViewById(R.id.edt_nickname);
        edt_emailId = findViewById(R.id.edt_emailId);
        edt_fb = findViewById(R.id.edt_fb);
        edt_insta = findViewById(R.id.edt_insta);
        edt_twitter = findViewById(R.id.edt_twitter);
        edt_linkdin = findViewById(R.id.edt_linkdin);
        edt_pintrest = findViewById(R.id.edt_pintrest);
        edt_youtube = findViewById(R.id.edt_youtube);
        edt_aboutUs = findViewById(R.id.edt_aboutUs);
        btn_save = findViewById(R.id.btn_save);
        ic_back = findViewById(R.id.ic_back);
        btn_business = findViewById(R.id.btn_business);
//        chk = findViewById(R.id.chk);
        ll_chk = findViewById(R.id.ll_chk);
        radioGender = findViewById(R.id.radioGender);
        seekbar_range = findViewById(R.id.seekbar_range);
        profile_image = findViewById(R.id.profile_image);
        edit_profile = findViewById(R.id.edit_profile);
        t_km = findViewById(R.id.t_km);
        txt_min_age = findViewById(R.id.txt_min_age);
        rangeBar = findViewById(R.id.rangeSeekbar);
        title = findViewById(R.id.title);
        txt_max_age = findViewById(R.id.txt_max_age);
        edt_address = findViewById(R.id.edt_address);
        gender = findViewById(R.id.gender);
        iv_location = findViewById(R.id.iv_location);
        mapview = findViewById(R.id.mapview);
        rl_map = findViewById(R.id.rl_map);
        relative_map = findViewById(R.id.relative_map);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (Objects.requireNonNull(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }

        profile_title = getIntent().getStringExtra("edit_p_profile");

        rangeBar.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NonNull SimpleRangeView simpleRangeView, int i, int i1) {
                txt_min_age.setText(String.valueOf(i));
                txt_max_age.setText(String.valueOf(i1));
                txt_min = i;
                txt_max = i1;
            }
        });

        rangeBar.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @Override
            public void onStartRangeChanged(@NonNull SimpleRangeView simpleRangeView, int i) {
                txt_min_age.setText(String.valueOf(i));
            }

            @Override
            public void onEndRangeChanged(@NonNull SimpleRangeView simpleRangeView, int i) {
                txt_max_age.setText(String.valueOf(i));
            }
        });

        rangeBar.setOnRangeLabelsListener(new SimpleRangeView.OnRangeLabelsListener() {
            @Nullable
            @Override
            public String getLabelTextForPosition(@NonNull SimpleRangeView simpleRangeView, int i, @NonNull SimpleRangeView.State state) {
                return String.valueOf(i);
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapsLocationActivity.class).putExtra("isProfileLocation", true));

            }
        });

        seekbar_range.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                p = i;
                seekBar.setProgress(i);
                t_km.setText(String.valueOf(p));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                t_km.setText(String.valueOf(p));
                text_km = p;
            }
        });


//        chk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (((CheckBox) view).isChecked()) {
//                    ll_chk.setVisibility(View.GONE);
//                } else {
//                    ll_chk.setVisibility(View.VISIBLE);
//                }
//            }
//        });

        relative_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("LLL_rel_map", "isClick");
                startActivity(new Intent(getApplicationContext(), MapsLocationActivity.class).putExtra("isProfileLocation", true));

            }
        });

//        yourRd = String.valueOf(radioGender.getCheckedRadioButtonId());
//        Log.e("LLL_gender--->", yourRd.toString());
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edt_name.getText().toString().isEmpty()) {
//                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.enter_full_name), Toast.LENGTH_SHORT).show();
                    edt_name.setError(getResources().getString(R.string.enter_full_name));
                } else if (edt_nickname.getText().toString().isEmpty()) {
                    edt_nickname.setError(getResources().getString(R.string.enter_nickname));
//                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.enter_nickname), Toast.LENGTH_SHORT).show();
                } else if (edt_emailId.getText().toString().isEmpty()) {
                    edt_emailId.setError(getResources().getString(R.string.enter_emailid));
//                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.enter_emailid), Toast.LENGTH_SHORT).show();
                } else {
                    FileUtils.DisplayLoading(ProfileActivity.this);
                    if (profile_title != null) {
                        patchApivolley(edt_name.getText().toString().trim(), edt_nickname.getText().toString().trim(), edt_emailId.getText().toString().trim(), edt_aboutUs.getText().toString().trim(), String.valueOf(Const.longitude), String.valueOf(Const.lattitude), text_km, yourRd, txt_min, txt_max, edt_fb.getText().toString().trim(), edt_insta.getText().toString().trim(), edt_twitter.getText().toString().trim(), edt_linkdin.getText().toString().trim());
                    } else {
                        postApivolley(edt_name.getText().toString().trim(), edt_nickname.getText().toString().trim(), edt_emailId.getText().toString().trim(), edt_aboutUs.getText().toString().trim(), String.valueOf(Const.longitude), String.valueOf(Const.lattitude), text_km, yourRd, txt_min, txt_max, edt_fb.getText().toString().trim(), edt_insta.getText().toString().trim(), edt_twitter.getText().toString().trim(), edt_linkdin.getText().toString().trim());

                    }
                }
            }
        });

        btn_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSaveAndCreateBusiness = true;

                if (edt_name.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.enter_full_name), Toast.LENGTH_SHORT).show();
                    edt_name.setError(getResources().getString(R.string.enter_full_name));
                } else if (edt_nickname.getText().toString().isEmpty()) {
                    edt_nickname.setError(getResources().getString(R.string.enter_nickname));
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.enter_nickname), Toast.LENGTH_SHORT).show();
                } else if (edt_emailId.getText().toString().isEmpty()) {
                    edt_emailId.setError(getResources().getString(R.string.enter_emailid));
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.enter_emailid), Toast.LENGTH_SHORT).show();
                } else {
                    FileUtils.DisplayLoading(ProfileActivity.this);

                    postApivolley(edt_name.getText().toString().trim(), edt_nickname.getText().toString().trim(), edt_emailId.getText().toString().trim(), edt_aboutUs.getText().toString().trim(), String.valueOf(Const.longitude), String.valueOf(Const.lattitude), text_km, yourRd, txt_min, txt_max, edt_fb.getText().toString().trim(), edt_insta.getText().toString().trim(), edt_twitter.getText().toString().trim(), edt_linkdin.getText().toString().trim());
                }

//                startActivity(new Intent(ProfileActivity.this, BusinessProfileActivity.class));
//                finish();

            }
        });


        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectBtn = findViewById(radioGroup.getCheckedRadioButtonId());
                yourRd = selectBtn.getText().toString();
//                Toast.makeText(ProfileActivity.this, "" + yourRd, Toast.LENGTH_SHORT).show();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        mapview.getMapAsync(this);

        Log.e("LLL_lati--: ", Const.longitude + " : longi : " + Const.lattitude);

        if (profile_title != null) {
            title.setText(profile_title);
            fetchgetApi();
            btn_business.setVisibility(View.GONE);
        } else {
            title.setText(getResources().getString(R.string.create_personal_profile));
        }


        if (Const.longitude != null) {

            if (map != null) {
                map.clear();
//            }
                if (Const.mCurrLocationMarker != null) {
                    Const.mCurrLocationMarker.remove();
                }
                LatLng userLocation = new LatLng(Double.valueOf(Const.lattitude), Double.valueOf(Const.longitude));
                map.addMarker(new MarkerOptions().position(userLocation));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));
//            edt_address.setText(FileUtils.getAddressFromLatLng(getApplicationContext(), Const.latLngvalue));
            }
        } else {
            fetchLocation();
        }


    }


    private void patchApivolley(String p_name, String pu_name, String p_email, String about_us, String longitude, String lattitude, Integer text_km, String yourRd, Integer txt_min, Integer txt_max, String fb_link, String insta_link, String tw_link, String ld_link) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("fullName", p_name);
        params.put("userName", pu_name);
        params.put("nickName", pu_name);
        params.put("emailId", p_email);
        params.put("longitude", longitude);
        params.put("latitude", lattitude);
        params.put("areaRange", text_km.toString().trim());
        params.put("gender", yourRd.toUpperCase());
        params.put("targetAudienceAgeMin", txt_min.toString().trim());
        params.put("targetAudienceAgeMax", txt_max.toString().trim());

        if (!fb_link.equals("")) {
            params.put("facebookLink", fb_link);
        } else {
            params.put("facebookLink", "");

        }
        if (!insta_link.equals("")) {
            params.put("instagramLink", insta_link);
        } else {
            params.put("instagramLink", "");

        }
        if (!tw_link.equals("")) {
            params.put("twitterLink", tw_link);
        } else {
            params.put("twitterLink", "");

        }
        if (!ld_link.equals("")) {
            params.put("linkedinLink", ld_link);
        } else {
            params.put("linkedinLink", "");

        }

//        params.put("facebookLink", fb_link);
//        params.put("instagramLink", insta_link);
//        params.put("twitterLink", tw_link);
//        params.put("linkedinLink", ld_link);

        JsonObjectRequest jsonObjectRequest = null;
        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, Constans.fetch_personal_info, new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    FileUtils.DismissLoading(ProfileActivity.this);
                    Log.e("LLL_Profile_update", response.toString());
//                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.data_add_successfully), Toast.LENGTH_SHORT).show();
                    ProfileRegisterModel profileRegisterModel = new Gson().fromJson(response.toString(), ProfileRegisterModel.class);
//                    Toast.makeText(ProfileActivity.this, profileRegisterModel.getMessage(), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    FileUtils.DismissLoading(ProfileActivity.this);
                    System.out.println("LLL_p_err---> " + error.toString());
                    error.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Data Not Submit" + error, Toast.LENGTH_SHORT).show();
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
        } catch (Exception e) {
            FileUtils.DismissLoading(ProfileActivity.this);
            Toast.makeText(this, getResources().getString(R.string.something_want_to_wrong), Toast.LENGTH_SHORT).show();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        requestQueue.add(jsonObjectRequest);
    }

    private void postApivolley(String f_name, String u_name, String u_email, String about_us, String longitude, String lattitude, Integer text_km, String yourRd, Integer txt_min, Integer txt_max, String fb_link, String insta_link, String tw_link, String ld_link) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("fullName", f_name);
        params.put("userName", u_name);
        params.put("nickName", u_name);
        params.put("emailId", u_email);
        params.put("longitude", longitude);
        params.put("latitude", lattitude);
        params.put("areaRange", text_km.toString().trim());
        params.put("gender", yourRd.toUpperCase());
        params.put("targetAudienceAgeMin", txt_min.toString().trim());
        params.put("targetAudienceAgeMax", txt_max.toString().trim());

        if (!fb_link.equals("")) {
            params.put("facebookLink", fb_link);
        } else {
            params.put("facebookLink", "");

        }
        if (!insta_link.equals("")) {
            params.put("instagramLink", insta_link);
        } else {
            params.put("instagramLink", "");

        }
        if (!tw_link.equals("")) {
            params.put("twitterLink", tw_link);
        } else {
            params.put("twitterLink", "");

        }
        if (!ld_link.equals("")) {
            params.put("linkedinLink", ld_link);
        } else {
            params.put("linkedinLink", "");

        }

        JsonObjectRequest jsonObjectRequest = null;
        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constans.profile_register, new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    FileUtils.DismissLoading(ProfileActivity.this);
                    Log.e("LL_Profile", response.toString());
                    ProfileRegisterModel profileRegisterModel = new Gson().fromJson(response.toString(), ProfileRegisterModel.class);
//                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.data_add_successfully), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ProfileActivity.this, profileRegisterModel.getMessage(), Toast.LENGTH_SHORT).show();

                    if (isSaveAndCreateBusiness) {
                        startActivity(new Intent(ProfileActivity.this, BusinessProfileActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    FileUtils.DismissLoading(ProfileActivity.this);
                    System.out.println("LLL_p_err---> " + error.toString());
                    Toast.makeText(ProfileActivity.this, "Data Not Submit" + error, Toast.LENGTH_SHORT).show();
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
        } catch (Exception e) {
            FileUtils.DismissLoading(ProfileActivity.this);
            Toast.makeText(this, getResources().getString(R.string.something_want_to_wrong), Toast.LENGTH_SHORT).show();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        requestQueue.add(jsonObjectRequest);
    }


    private void fetchgetApi() {
//        FileUtils.DisplayLoading(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = null;
        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constans.fetch_personal_info, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
//                    FileUtils.DismissLoading(getApplicationContext());

                    ProfileRegisterModel profileRegisterModel = new Gson().fromJson(response.toString(), ProfileRegisterModel.class);

                    Double latitiude = profileRegisterModel.getPersonalInfoModel().getLatitude();
                    Double logitude = profileRegisterModel.getPersonalInfoModel().getLongitude();
                    LatLng latLng1 = new LatLng(latitiude, logitude);
                    latLng = latLng1;
                    String getadd = FileUtils.getAddressFromLatLng(getApplicationContext(), latitiude, logitude);
                    Log.e("LLL_b_add-->", getadd);

                    edt_address.setText(getadd);
                    edt_name.setText(profileRegisterModel.getPersonalInfoModel().getFullName());
                    edt_nickname.setText(profileRegisterModel.getPersonalInfoModel().getNickName());
                    edt_emailId.setText(profileRegisterModel.getPersonalInfoModel().getEmailId());
                    t_km.setText(String.valueOf(profileRegisterModel.getPersonalInfoModel().getAreaRange()));
                    txt_min_age.setText(String.valueOf(profileRegisterModel.getPersonalInfoModel().getTargetAudienceAgeMin()));
                    txt_max_age.setText(String.valueOf(profileRegisterModel.getPersonalInfoModel().getTargetAudienceAgeMax()));
                    edt_fb.setText(profileRegisterModel.getPersonalInfoModel().getFacebookLink());
                    edt_insta.setText(profileRegisterModel.getPersonalInfoModel().getInstagramLink());
                    edt_twitter.setText(profileRegisterModel.getPersonalInfoModel().getTwitterLink());
                    edt_linkdin.setText(profileRegisterModel.getPersonalInfoModel().getLinkedinLink());
                    seekbar_range.setProgress(Integer.parseInt(String.valueOf(profileRegisterModel.getPersonalInfoModel().getAreaRange())));
                    gender.setText(profileRegisterModel.getPersonalInfoModel().getGender());
                    rangeBar.setStart(profileRegisterModel.getPersonalInfoModel().getTargetAudienceAgeMin());
                    rangeBar.setEnd(profileRegisterModel.getPersonalInfoModel().getTargetAudienceAgeMax());

                    hello = gender.getText().toString();
                    if (hello.equals("Male")) {
                        ((RadioButton) radioGender.getChildAt(0)).setChecked(true);
                    } else if (hello.equals("FEMALE")) {
                        ((RadioButton) radioGender.getChildAt(1)).setChecked(true);
                    } else if (hello.equals("OTHER")) {
                        ((RadioButton) radioGender.getChildAt(2)).setChecked(true);
                    }

                    yourRd = hello;
                    text_km = Integer.parseInt(t_km.getText().toString());
                    txt_min = profileRegisterModel.getPersonalInfoModel().getTargetAudienceAgeMin();
                    txt_max = profileRegisterModel.getPersonalInfoModel().getTargetAudienceAgeMax();

                    map.addMarker(new MarkerOptions().position(latLng1));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 12));


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    FileUtils.DismissLoading(getApplicationContext());

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

            RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
//            FileUtils.DismissLoading(getApplicationContext());
            e.printStackTrace();
        }
    }

    private void openGallery() {
        ImagePicker.Companion.with(ProfileActivity.this)
                .crop()
                .maxResultSize(1080, 1080)
                .start(PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            Uri selectedImageUri;
            try {
                selectedImageUri = data.getData();
                Bitmap bitmap = null;
//                try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                Const.bitmap_profile_image = bitmap;
                profile_image.setImageBitmap(bitmap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
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
    public void onLocationChanged(Location location) {
//        txtLat = (TextView) findViewById(R.id.textview1);
        Log.e("Latitude: ", +location.getLatitude() + ", Longitude:" + location.getLongitude());
        longitude = String.valueOf(location.getLongitude());
        lattitude = String.valueOf(location.getLatitude());

        LatLng userLocation;
        if (Const.longitude != null) {
            userLocation = new LatLng(Const.lattitude, Const.longitude);

        } else {
            userLocation = new LatLng(Double.valueOf(lattitude), Double.valueOf(longitude));

        }
        map.addMarker(new MarkerOptions().position(userLocation));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    public void fetchLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                return;
            } else {

            }
//            map.setMyLocationEnabled(true);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);

        } catch (Exception e) {
            Log.e("LLL_bproerr--->", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (Const.longitude != null) {
//
//            edt_address.setText(FileUtils.getAddressFromLatLng(getApplicationContext(), Const.latLngvalue));
//        } else {
//            fetchLocation();
//        }

        if (Const.longitude != null) {

            if (map != null) {
                map.clear();
                if (Const.mCurrLocationMarker != null) {
                    Const.mCurrLocationMarker.remove();
                }
                LatLng userLocation = new LatLng(Const.lattitude, Const.longitude);
//            Const.mCurrLocationMarker = map.addMarker(new MarkerOptions().position(userLocation));
                map.addMarker(new MarkerOptions().position(userLocation));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));

                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        Log.e("LLL_onclickmap==>", "false");
                    }
                });

            }
//            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                @Override
//                public void onMapClick(@NonNull LatLng latLng) {
//                    Log.e("LLL_onclickmap==>", "true");
//                }
//            });
//            mapview.invalidate();

        } else {
            fetchLocation();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        try {
            map = googleMap;
//        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);

//                if (map != null) {
//                    map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                        @Override
//                        public void onMapClick(LatLng arg0) {
//                            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
//
//                            android.util.Log.i("onMapClick", "Horray!");
//                        }
//                    });
//                }

                criteria = new Criteria();
                bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

                //You can still do this if you like, you might get lucky:
                Location location = locationManager.getLastKnownLocation(bestProvider);
                if (location != null) {
                    Log.e("TAG", "GPS is on");
                    Const.lattitude = location.getLatitude();
                    Const.longitude = location.getLongitude();
                    Log.e("LLL_Latitude: ", +location.getLatitude() + ", Longitude:" + location.getLongitude());

                    //
                    centreMapOnLocation(location, bestProvider);


                } else {
                    //This is what you need:
                    locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
                }

                mapview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), MapsLocationActivity.class).putExtra("isProfileLocation", true));
                    }
                });

                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng arg0) {
                        startActivity(new Intent(getApplicationContext(), MapsLocationActivity.class).putExtra("isProfileLocation", true));

                        android.util.Log.i("onMapClick", "Horray!");
                    }
                });

            } else {

            }
        } catch (Exception e) {
            Log.e("LLL_bproerr1--->", e.getMessage());
            e.printStackTrace();
        }
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false).setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void centreMapOnLocation(Location location, String title) {
//        if (mCurrLocationMarker != null) {
//            mCurrLocationMarker.remove();
//        }

        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
//        map.clear();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(userLocation);
        markerOptions.draggable(true);
        markerOptions.title(title);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//        mCurrLocationMarker = map.addMarker(markerOptions);
        map.addMarker(markerOptions);

//        map.addMarker(new MarkerOptions().position(userLocation).title(title));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));

    }

    private void updateTokenConfirmation() {
//        StringBuilder sb = new StringBuilder("Current tokens:\n");
        StringBuilder sb = new StringBuilder();
        for (Object token : completionView.getObjects()) {
            sb.append(token.toString());
//            sb.append("\n");
            sb.append(",");

        }
        tag_str = sb.toString();
        Log.e("LLL_tag_str--->", tag_str);

    }

    @Override
    public void onTokenAdded(Person token) {
        ((TextView) findViewById(R.id.lastEvent)).setText("Added: " + token);
        updateTokenConfirmation();
    }

    @Override
    public void onTokenRemoved(Person token) {
        ((TextView) findViewById(R.id.lastEvent)).setText("Removed: " + token);
        updateTokenConfirmation();
    }

    @Override
    public void onTokenIgnored(Person token) {
        ((TextView) findViewById(R.id.lastEvent)).setText("Ignored: " + token);
        updateTokenConfirmation();
    }

    @Override
    public void onBackPressed() {
//        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

}