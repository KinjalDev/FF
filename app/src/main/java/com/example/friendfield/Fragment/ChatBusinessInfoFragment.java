package com.example.friendfield.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.friendfield.Activity.BusinessProfileActivity;
import com.example.friendfield.Model.BusinessInfo.BusinessInfoRegisterModel;
import com.example.friendfield.MyApplication;
import com.example.friendfield.R;
import com.example.friendfield.Utils.Const;
import com.example.friendfield.Utils.Constans;
import com.example.friendfield.Utils.FileUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.kyleduo.switchbutton.SwitchButton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatBusinessInfoFragment extends Fragment {

    CircleImageView business_img;
    TextView txt_business_name, txt_category, txt_subcategory, txt_discription, txt_business_location, txt_business_category, txt_business_subcategory, txt_business_brochure;
    RelativeLayout rel_brochure;
    RelativeLayout rl_business_authorized;
    RelativeLayout clear_chats;
    RelativeLayout block_hunter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_chat_business_info, container, false);
        business_img = inflate.findViewById(R.id.business_img);
        txt_business_name = inflate.findViewById(R.id.text_business_name);
        txt_category = inflate.findViewById(R.id.txt_category);
        txt_subcategory = inflate.findViewById(R.id.txt_subcategory);
        txt_discription = inflate.findViewById(R.id.txt_discription);
        txt_business_location = inflate.findViewById(R.id.txt_business_location);
        txt_business_category = inflate.findViewById(R.id.txt_business_category);
        txt_business_subcategory = inflate.findViewById(R.id.txt_business_subcategory);
        txt_business_brochure = inflate.findViewById(R.id.txt_business_brochure);

        rel_brochure = inflate.findViewById(R.id.rel_brochure);
        rl_business_authorized = inflate.findViewById(R.id.rl_business_authorized);
        clear_chats = inflate.findViewById(R.id.clear_chats);
        block_hunter = inflate.findViewById(R.id.block_hunter);

        if (Const.bitmap_business_profile_image != null) {
            business_img.setImageBitmap(Const.bitmap_business_profile_image);
        }

        rl_business_authorized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authorizedDialog();

            }
        });

//        getApiCalling();
        return inflate;
    }

    public void authorizedDialog() {
        Dialog dialog = new Dialog(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.authorized_dialog, null);
        dialog.setContentView(view);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView btn_close = dialog.findViewById(R.id.btn_close);

        TextView txt_user = dialog.findViewById(R.id.txt_user);
        TextView txt_ph_mo = dialog.findViewById(R.id.txt_ph_mo);
        TextView txt_email = dialog.findViewById(R.id.txt_email);

        SwitchButton name_switch = dialog.findViewById(R.id.name_switch);
        SwitchButton number_switch = dialog.findViewById(R.id.number_switch);
        SwitchButton email_switch = dialog.findViewById(R.id.email_switch);
        SwitchButton media_switch = dialog.findViewById(R.id.media_switch);


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }


//    private void getApiCalling() {
//
//        try {
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constans.fetch_business_info, null, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//
//                    Log.e("BusinessInfo", response.toString());
//                    BusinessInfoRegisterModel businessInfoRegisterModel = new Gson().fromJson(response.toString(), BusinessInfoRegisterModel.class);
//
//                    Double latitiude = businessInfoRegisterModel.getBusinessInfoModel().getLatitude();
//                    Double logitude = businessInfoRegisterModel.getBusinessInfoModel().getLongitude();
//
//                    LatLng latLng = new LatLng(latitiude, logitude);
//
//                    txt_business_name.setText(businessInfoRegisterModel.getBusinessInfoModel().getName());
//                    txt_category.setText(businessInfoRegisterModel.getBusinessInfoModel().getCategory());
//                    txt_subcategory.setText(businessInfoRegisterModel.getBusinessInfoModel().getSubCategory());
//                    txt_discription.setText(businessInfoRegisterModel.getBusinessInfoModel().getDescription());
//                    txt_business_location.setText(FileUtils.getAddressFromLatLng(getContext(), latLng));
//                    txt_business_category.setText(businessInfoRegisterModel.getBusinessInfoModel().getInterestedCategory());
//                    txt_business_subcategory.setText(businessInfoRegisterModel.getBusinessInfoModel().getInterestedSubCategory());
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e("Error", error.toString());
//                }
//            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> map = new HashMap<>();
//                    map.put("Content-Type", "application/json");
//                    map.put("auth-token", MyApplication.getAuthToken(getContext()));
//                    return map;
//                }
//            };
//
//            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//            requestQueue.add(jsonObjectRequest);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}