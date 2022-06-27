package com.example.friendfield.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.friendfield.Adapter.TagAdapter;
import com.example.friendfield.R;
import com.example.friendfield.Utils.Const;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.kyleduo.switchbutton.SwitchButton;

public class ChatPersnoalInfoFragment extends Fragment {

    TextView txt_about, txt_email, txt_number;
    RelativeLayout rl_authorized, clear_chats, block_hunter, report_hunter;
    Fragment fragment;
    RecyclerView recy_tag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_chat_persnoal_info, container, false);

        txt_about = inflate.findViewById(R.id.txt_about);
        txt_email = inflate.findViewById(R.id.txt_email);
        txt_number = inflate.findViewById(R.id.txt_number);
        rl_authorized = inflate.findViewById(R.id.rl_authorized);
        clear_chats = inflate.findViewById(R.id.clear_chats);
        block_hunter = inflate.findViewById(R.id.block_hunter);
        report_hunter = inflate.findViewById(R.id.report_hunter);
        recy_tag = inflate.findViewById(R.id.recy_tag);

        recy_tag.setLayoutManager(new FlexboxLayoutManager(getContext()));

        if (Const.tag_str != null && !Const.tag_str.isEmpty()) {
            String[] items = Const.tag_str.split(",");

            for (int i = 0; i < items.length; i++) {
                Const.taglist.add(items[i]);
            }

            TagAdapter tagAdapter=new TagAdapter(getActivity(), Const.taglist);
            recy_tag.setAdapter(tagAdapter);
        }


        rl_authorized.setOnClickListener(new View.OnClickListener() {
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
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constans.fetch_personal_info, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    Log.e("ChatUserProfile" ,response.toString());
//                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
//                    ChatUserProfileTokenModel chatUserProfileTokenModel = new Gson().fromJson(response.toString(),ChatUserProfileTokenModel.class);
//
//                    Double latitiude = chatUserProfileTokenModel.getChatUserProfileModel().getLatitude();
//                    Double logitude = chatUserProfileTokenModel.getChatUserProfileModel().getLongitude();
//
//                    LatLng latLng = new LatLng(latitiude, logitude);
//
//                    txt_number.setText(chatUserProfileTokenModel.getChatUserProfileModel().getContactNo());
//                    txt_email.setText(chatUserProfileTokenModel.getChatUserProfileModel().getEmailId());
//                    text_location.setText(FileUtils.getAddressFromLatLng(getContext(),latLng));
//                    text_rage.setText(chatUserProfileTokenModel.getChatUserProfileModel().getAreaRange().toString());
//                    text_gender.setText(chatUserProfileTokenModel.getChatUserProfileModel().getGender());
//                    text_age.setText(chatUserProfileTokenModel.getChatUserProfileModel().getTargetAudienceAgeMin()  + "-" +  chatUserProfileTokenModel.getChatUserProfileModel().getTargetAudienceAgeMax());
//
//
//                }catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> map = new HashMap<>();
//                map.put("Content-Type", "application/json");
//                map.put("auth-token", MyApplication.getAuthToken(getContext()));
//                return map;
//            }
//        };
//
//        RequestQueue referenceQueue = Volley.newRequestQueue(getContext());
//        referenceQueue.add(jsonObjectRequest);
//    }

    @Override
    public void onResume() {
        super.onResume();
//        getApiCalling();
    }
}