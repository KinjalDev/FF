package com.example.friendfield.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amazonaws.mobileconnectors.s3.transfermanager.Upload;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.friendfield.Model.Business.Register.BusinessRegisterModel;
import com.example.friendfield.Model.BusinessInfo.BusinessInfoRegisterModel;
import com.example.friendfield.MyApplication;
import com.example.friendfield.R;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BusinessProfileActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap map;
    ImageView ic_back, img_add_brochure, iv_location, img;
    CircleImageView business_profile_image;
    RelativeLayout edt_img, rl_map, relative_busi_map, rl_upload;
    LinearLayout lin_category, lin_business_category,lin_add_images;
    Spinner name_spinner, inter_business_category;
    EditText edt_subcategory, edt_interested_subcategory, edt_interested_category, edt_category, edt_description, edt_bussiness_name, edt_business_address;
    AppCompatButton btn_next, btn_save;
    File myFile;
    String cat_spinner_value = "";
    String inter_cat_spinner_value = "";
    String inter_subcat_spinner_value = "";
    LocationManager locationManager;
    LocationListener locationListener;
    RequestQueue queue;
    Context context;
    public Criteria criteria;
    public String bestProvider;
    LatLng currentLoaction, latLng;
    String edit_profile = "";
    TextView tv_title, txt_open;
    MapView mapview;
    String longitude;
    String lattitude;
    String path = null;
    private int requestCode;
    private String[] permissions;
    private int[] grantResults;
    public static final int PICK_FILE = 99;
    PdfRenderer renderer;
    int total_pages = 0;
    int display_page = 0;

    private String getpath(String path) {
        return path;
    }

    public static final int PICK_IMAGE = 1;
    public static final int PICK_PDF = 2;

    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile);

        context = this;
        queue = Volley.newRequestQueue(BusinessProfileActivity.this, new HurlStack());

        ic_back = findViewById(R.id.ic_back);
        business_profile_image = findViewById(R.id.business_profile_image);
        edt_bussiness_name = findViewById(R.id.edt_bussiness_name);
        edt_img = findViewById(R.id.edt_img);
        edt_category = findViewById(R.id.edt_category);
        edt_subcategory = findViewById(R.id.edt_subcategory);
        edt_description = findViewById(R.id.edt_description);
        edt_interested_subcategory = findViewById(R.id.edt_interested_subcategory);
        edt_interested_category = findViewById(R.id.edt_interested_category);
        btn_next = findViewById(R.id.btn_next);
        rl_map = findViewById(R.id.rl_map);
        edt_business_address = findViewById(R.id.edt_business_address);
        iv_location = findViewById(R.id.iv_location);
        tv_title = findViewById(R.id.tv_title);
        btn_save = findViewById(R.id.btn_save);
        mapview = findViewById(R.id.mapview);
        relative_busi_map = findViewById(R.id.relative_busi_map);
        img_add_brochure = findViewById(R.id.img_add_brochure);
        rl_upload = findViewById(R.id.rl_upload);
        lin_add_images = findViewById(R.id.lin_add_images);

        edit_profile = getIntent().getStringExtra("edit_profile");


        edt_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.Companion.with(BusinessProfileActivity.this)
                        .crop()
//                        .galleryOnly()
                        .maxResultSize(1080, 1080)
                        .start(PICK_IMAGE);

            }
        });

        rl_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPdf();
            }
        });

//        img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myFile = new File(data1.getPath());
//                myFile.delete();
//                if(myFile.exists()){
////                    myFile.getCanonicalFile().delete();
////                    if(myFile.exists()){
////                        getApplicationContext().deleteFile(myFile.getName());
////                    }
//                }
//            }
//        });


//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.business_category, R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
//        name_spinner.setAdapter(adapter);
//
//        ArrayAdapter<CharSequence> adapter_cat = ArrayAdapter.createFromResource(this, R.array.inter_business_category, R.layout.simple_spinner_item);
//        adapter_cat.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
//        inter_business_category.setAdapter(adapter_cat);

//        ArrayAdapter<CharSequence> adapter_subcat = ArrayAdapter.createFromResource(this, R.array.inter_business_subcategory, R.layout.simple_spinner_item);
//        adapter_subcat.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
//        inter_business_subcategory.setAdapter(adapter_subcat);


//        name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                cat_spinner_value = parent.getItemAtPosition(position).toString();
//                ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        inter_business_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                inter_cat_spinner_value = parent.getItemAtPosition(position).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

//        inter_business_subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                inter_subcat_spinner_value = parent.getItemAtPosition(position).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

        ic_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapsLocationActivity.class).putExtra("isBusinessLocation", true));

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_bussiness_name.getText().toString().equals("")) {
                    edt_bussiness_name.setError(getResources().getString(R.string.please_enter_bname));

                } else if (edt_category.getText().toString().equals("")) {
//                    Toast.makeText(context, getResources().getString(R.string.please_enter_b_cat), Toast.LENGTH_SHORT).show();
                    edt_category.setError(getResources().getString(R.string.please_enter_bname));

                } else if (edt_subcategory.getText().toString().equals("")) {
                    edt_subcategory.setError(getResources().getString(R.string.please_enter_b_subcat));

                } else if (edt_description.getText().toString().equals("")) {
                    edt_description.setError(getResources().getString(R.string.please_enter_b_des));

                } else if (Const.b_longitude == null) {
                    Toast.makeText(context, getResources().getString(R.string.please_enter_b_location), Toast.LENGTH_SHORT).show();

                } else if (Const.b_lattitude == null) {
                    Toast.makeText(context, getResources().getString(R.string.please_enter_b_location), Toast.LENGTH_SHORT).show();

                } else {
                    FileUtils.DisplayLoading(context);
                    CreateBusinessAccount(Request.Method.POST, Constans.business_register, edt_bussiness_name.getText().toString().trim(), edt_category.getText().toString().trim(), edt_subcategory.getText().toString().trim(), edt_description.getText().toString().trim(), String.valueOf(Const.b_longitude), String.valueOf(Const.b_lattitude), inter_cat_spinner_value, inter_subcat_spinner_value);
                }
            }
        });

        relative_busi_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapsLocationActivity.class).putExtra("isBusinessLocation", true));
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_bussiness_name.getText().toString().equals("")) {
                    edt_bussiness_name.setError(getResources().getString(R.string.please_enter_bname));

                } else if (edt_category.getText().toString().equals("")) {
                    edt_category.setError(getResources().getString(R.string.please_enter_bname));

//                } else if ((cat_spinner_value.equals(""))) {
//                    Toast.makeText(context, getResources().getString(R.string.please_enter_b_cat), Toast.LENGTH_SHORT).show();

                } else if (edt_subcategory.getText().toString().equals("")) {
                    edt_subcategory.setError(getResources().getString(R.string.please_enter_b_subcat));

                } else if (edt_description.getText().toString().equals("")) {
                    edt_description.setError(getResources().getString(R.string.please_enter_b_des));

                } else if (String.valueOf(Const.b_longitude) == null) {
                    Toast.makeText(context, getResources().getString(R.string.please_enter_b_location), Toast.LENGTH_SHORT).show();

                } else if (String.valueOf(Const.b_lattitude) == null) {
                    Toast.makeText(context, getResources().getString(R.string.please_enter_b_location), Toast.LENGTH_SHORT).show();

                } else {
                    FileUtils.DisplayLoading(context);
                    CreateBusinessAccount(Request.Method.PATCH, Constans.fetch_business_info, edt_bussiness_name.getText().toString().trim(), edt_category.getText().toString().trim(), edt_subcategory.getText().toString().trim(), edt_description.getText().toString().trim(), String.valueOf(Const.b_longitude), String.valueOf(Const.b_lattitude), inter_cat_spinner_value, inter_subcat_spinner_value);
                }
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        mapview.getMapAsync(this);

        if (edit_profile != null) {

            tv_title.setText(edit_profile);
            if (Const.bitmap_business_profile_image != null) {
                business_profile_image.setImageBitmap(Const.bitmap_business_profile_image);
            }
            getBusinessProfileInfo();
            btn_save.setVisibility(View.VISIBLE);
            btn_next.setVisibility(View.GONE);

        } else {
            tv_title.setText(getResources().getString(R.string.create_business_profile));
            btn_save.setVisibility(View.GONE);
            btn_next.setVisibility(View.VISIBLE);
        }


        if (Const.b_longitude != null) {
            if (map != null) {
                map.clear();

                if (Const.mCurrLocationMarker != null) {
                    Const.mCurrLocationMarker.remove();
                }
                LatLng userLocation = new LatLng(Const.b_lattitude, Const.b_longitude);
                map.addMarker(new MarkerOptions().position(userLocation));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));
                edt_business_address.setText(FileUtils.getAddressFromLatLng(getApplicationContext(), Const.latLngvalue));

            }
        } else {
            fetchLocation();

        }

    }

    private void selectPdf() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_FILE);
    }

    public void getBusinessProfileInfo() {
        FileUtils.DisplayLoading(BusinessProfileActivity.this);
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constans.fetch_business_info, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    FileUtils.DismissLoading(BusinessProfileActivity.this);

                    Log.e("LLL_BusinessInfo", response.toString());

//                    if (Const.bitmap_business_profile_image != null) {
//                        business_profile_image.setImageBitmap(Const.bitmap_business_profile_image);
//                    }

                    BusinessInfoRegisterModel businessInfoRegisterModel = new Gson().fromJson(response.toString(), BusinessInfoRegisterModel.class);

                    Double latitiude = businessInfoRegisterModel.getBusinessInfoModel().getLatitude();
                    Double logitude = businessInfoRegisterModel.getBusinessInfoModel().getLongitude();

                    LatLng latLng1 = new LatLng(latitiude, logitude);
                    latLng = latLng1;

                    edt_bussiness_name.setText(businessInfoRegisterModel.getBusinessInfoModel().getName());
                    edt_category.setText(businessInfoRegisterModel.getBusinessInfoModel().getCategory());
                    edt_subcategory.setText(businessInfoRegisterModel.getBusinessInfoModel().getSubCategory());
                    edt_description.setText(businessInfoRegisterModel.getBusinessInfoModel().getDescription());

                    String getadd = FileUtils.getAddressFromLatLng(getApplicationContext(), latLng);

                    Log.e("LLL_b_add-->", getadd);

                    edt_business_address.setText(getadd);

//                    cat_spinner_value = businessInfoRegisterModel.getBusinessInfoModel().getCategory();
//                    ArrayAdapter<String> spinnerAdap = (ArrayAdapter<String>) name_spinner.getAdapter();
//                    int spinnerPosition = spinnerAdap.getPosition(cat_spinner_value);
//                    name_spinner.setSelection(spinnerPosition);
//
//
//                    inter_cat_spinner_value = businessInfoRegisterModel.getBusinessInfoModel().getInterestedCategory();
//                    ArrayAdapter<String> intercatbusiness = (ArrayAdapter<String>) inter_business_category.getAdapter();
//                    int spinnerintercat = intercatbusiness.getPosition(inter_cat_spinner_value);
//                    inter_business_category.setSelection(spinnerintercat);


//                    inter_subcat_spinner_value = businessInfoRegisterModel.getBusinessInfoModel().getInterestedSubCategory();
//                    ArrayAdapter<String> intersubcatbusiness = (ArrayAdapter<String>) inter_business_subcategory.getAdapter();
//                    int spinnerintersubcat = intersubcatbusiness.getPosition(inter_subcat_spinner_value);
//                    inter_business_subcategory.setSelection(spinnerintersubcat);

                    map.addMarker(new MarkerOptions().position(latLng1));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 12));

                    edt_interested_category.setText(businessInfoRegisterModel.getBusinessInfoModel().getInterestedCategory());
                    edt_interested_subcategory.setText(businessInfoRegisterModel.getBusinessInfoModel().getInterestedSubCategory());

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    FileUtils.DismissLoading(BusinessProfileActivity.this);
//                    Log.e("Error", error.printStackTrace());
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
            FileUtils.DismissLoading(BusinessProfileActivity.this);

        }
    }

    @Override
    public void onLocationChanged(Location location) {
//        txtLat = (TextView) findViewById(R.id.textview1);
        Log.e("Latitude: ", +location.getLatitude() + ", Longitude:" + location.getLongitude());
        longitude = String.valueOf(location.getLongitude());
        lattitude = String.valueOf(location.getLatitude());

        LatLng userLocation;
        if (Const.b_longitude != null) {
            userLocation = new LatLng(Const.b_lattitude, Const.b_longitude);

        } else if (latLng != null) {
            userLocation = new LatLng(Double.valueOf(latLng.latitude), Double.valueOf(latLng.longitude));

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
                //            map.setMyLocationEnabled(true);
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        } catch (Exception e) {
            Log.e("LLL_bproerr--->", e.getMessage());
            e.printStackTrace();
        }


    }

    @SuppressLint("Range")
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
                    business_profile_image.setImageBitmap(bitmap);
                    img_add_brochure.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//            business_profile_image.setImageURI(selectedImageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (requestCode == PICK_FILE && resultCode == RESULT_OK){
            if (data != null){
                Uri uri = data.getData();
                lin_add_images.setVisibility(View.GONE);
                try {
                    ParcelFileDescriptor parcelFileDescriptor = getContentResolver()
                            .openFileDescriptor(uri, "r");
                    renderer = new PdfRenderer(parcelFileDescriptor);
                    total_pages = renderer.getPageCount();
                    display_page = 0;
                    _display(display_page);
                } catch (FileNotFoundException fnfe){

                } catch (IOException e){

                }
            }
        }
    }

    private void _display(int _n) {
        if (renderer != null) {
            PdfRenderer.Page page = renderer.openPage(_n);
            Bitmap mBitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
            page.render(mBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            img_add_brochure.setImageBitmap(mBitmap);
            page.close();
//            textview1.setText((_n + 1) + "/" + total_pages);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                }
                break;
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
//                        startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("isBusinessLocation", true));
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
                    Const.b_lattitude = location.getLatitude();
                    Const.b_longitude = location.getLongitude();
                    Log.e("LLL_Latitude: ", +location.getLatitude() + ", Longitude:" + location.getLongitude());

                    centreMapOnLocation(location, bestProvider);

                } else {
                    //This is what you need:
                    locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
                }

                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng arg0) {
                        startActivity(new Intent(getApplicationContext(), MapsLocationActivity.class).putExtra("isBusinessLocation", true));

//                        android.util.Log.i("onMapClick", "Horray!");
                    }
                });

                mapview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), MapsLocationActivity.class).putExtra("isBusinessLocation", true));

                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "Accetta i permessi", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("LLL_bproerr1--->", e.getMessage());
            e.printStackTrace();
        }

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

    public void CreateBusinessAccount(int method, String url, String name, String category, String subcategory, String des, String longi, String latti, String interestedCategory, String interestedSubCategory) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("category", category);
        params.put("subCategory", subcategory);
        params.put("description", des);
        params.put("longitude", longi);
        params.put("latitude", latti);
        params.put("interestedCategory", interestedCategory);
        params.put("interestedSubCategory", interestedSubCategory);

        JsonObjectRequest request = null;
        try {
            request = new JsonObjectRequest(method, url, new JSONObject(params), new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    FileUtils.DismissLoading(context);
                    Log.e("LLL_busi_res-->", response.toString());

                    BusinessRegisterModel businessRegisterModel = new Gson().fromJson(response.toString(), BusinessRegisterModel.class);

                    finish();

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    FileUtils.DismissLoading(getApplicationContext());
                    System.out.println("LLL_b_err--> " + error.toString());
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

            queue.add(request);

        } catch (Exception e) {
            FileUtils.DismissLoading(BusinessProfileActivity.this);
            Toast.makeText(this, getResources().getString(R.string.something_want_to_wrong), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Log.e("LLL_bserror-->", e.getMessage());
        }


    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (Const.longitude != null) {
//
//            edt_business_address.setText(FileUtils.getAddressFromLatLng(getApplicationContext(), Const.latLngvalue));
//        } else {
//            fetchLocation();
//        }

        if (Const.b_longitude != null) {
            if (map != null) {
                map.clear();
                if (Const.mCurrLocationMarker != null) {
                    Const.mCurrLocationMarker.remove();
                }
                LatLng userLocation = new LatLng(Const.b_lattitude, Const.b_longitude);
//            Const.mCurrLocationMarker = map.addMarker(new MarkerOptions().position(userLocation));
                map.addMarker(new MarkerOptions().position(userLocation));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));

                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        Log.e("LLL_onclick_map==>", "false");
                    }
                });

            }
//            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                @Override
//                public void onMapClick(@NonNull LatLng latLng) {
//                    Log.e("LLL_onclick_map==>", "true");
//                }
//            });
        } else {
            fetchLocation();
        }

    }

}