package com.example.friendfield.Fragment;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.friendfield.Adapter.TagAdapter;
import com.example.friendfield.Model.PersonalInfo.PeronalRegisterModel;
import com.example.friendfield.MyApplication;
import com.example.friendfield.R;
import com.example.friendfield.Utils.Const;
import com.example.friendfield.Utils.Constans;
import com.example.friendfield.Utils.FileUtils;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.kyleduo.switchbutton.SwitchButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PersnoalInfoFragment extends Fragment {

    TextView txt_number, txt_mail, txt_location, txt_range, txt_gender, txt_age;
    TextView txt_about;
    SwitchButton switch_face_id;
    RecyclerView recy_tag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_persnoal_info, container, false);

        txt_number = view.findViewById(R.id.txt_number);
        txt_mail = view.findViewById(R.id.txt_email);
        txt_location = view.findViewById(R.id.text_location);
        txt_range = view.findViewById(R.id.text_rage);
        txt_gender = view.findViewById(R.id.text_gender);
        txt_age = view.findViewById(R.id.text_age);
        recy_tag = view.findViewById(R.id.recy_tag);

        txt_about = view.findViewById(R.id.txt_about);
        switch_face_id = view.findViewById(R.id.switch_face_id);

        recy_tag.setLayoutManager(new FlexboxLayoutManager(getContext()));

        if (Const.tag_str != null && !Const.tag_str.isEmpty()) {
            String[] items = Const.tag_str.split(",");

            for (int i = 0; i < items.length; i++) {
                Const.taglist.add(items[i]);
            }

            TagAdapter tagAdapter=new TagAdapter(getActivity(), Const.taglist);
            recy_tag.setAdapter(tagAdapter);
        }

        getAllData();

        return view;
    }

    private void getAllData() {

        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constans.fetch_personal_info, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        Log.e("personal", response.toString());
                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                        PeronalRegisterModel peronalInfoModel = new Gson().fromJson(response.toString(), PeronalRegisterModel.class);


                        Double latitiude = peronalInfoModel.getPersonalInfoModel().getLatitude();
                        Double logitude = peronalInfoModel.getPersonalInfoModel().getLongitude();

                        LatLng latLng = new LatLng(latitiude, logitude);

                        txt_number.setText(MyApplication.getCountryCode(getContext()) + " " + peronalInfoModel.getPersonalInfoModel().getContactNo());
                        txt_mail.setText(peronalInfoModel.getPersonalInfoModel().getEmailId());
                        txt_range.setText(peronalInfoModel.getPersonalInfoModel().getAreaRange().toString());
                        txt_gender.setText(peronalInfoModel.getPersonalInfoModel().getGender());
                        txt_age.setText(peronalInfoModel.getPersonalInfoModel().getTargetAudienceAgeMin() + "-" + peronalInfoModel.getPersonalInfoModel().getTargetAudienceAgeMax());
                        txt_location.setText(FileUtils.getAddressFromLatLng(getContext(), latLng));

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
                    map.put("auth-token", MyApplication.getAuthToken(getContext()));
                    return map;
                }
            };

            RequestQueue referenceQueue = Volley.newRequestQueue(getContext());
            referenceQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
