package com.example.friendfield.Adapter;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendfield.Activity.ProfileActivity;
import com.example.friendfield.Fragment.FriendRequestFragment;
import com.example.friendfield.MainActivity;
import com.example.friendfield.MyApplication;
import com.example.friendfield.R;
import com.kyleduo.switchbutton.SwitchButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyDataHolder> {

    Fragment fragment;
    int[] u_img;
    String[] u_name,u_dis;
    LayoutInflater inflater;

    public RequestAdapter(FriendRequestFragment friendRequestFragment, int[] user_img, String[] user_name, String[] user_dis) {
        this.fragment = friendRequestFragment;
        this.u_img = user_img;
        this.u_name = user_name;
        this.u_dis = user_dis;
        inflater = LayoutInflater.from(fragment.getContext());
    }

    @NonNull
    @Override
    public MyDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.request_item,parent,false);
        return new MyDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDataHolder holder, int position) {

        holder.text_username.setText(u_name[position]);
        holder.text_dis.setText(u_dis[position]);
        holder.request_image.setImageResource(u_img[position]);

        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptDialog();
            }
        });

        holder.btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectDialog();
            }
        });
    }

    @Override
    public int getItemCount() {
        return u_dis.length;
    }

    class MyDataHolder extends RecyclerView.ViewHolder {

        AppCompatButton btn_accept,btn_reject;
        TextView text_username,text_time,text_dis;
        CircleImageView request_image;

        public MyDataHolder(@NonNull View itemView) {
            super(itemView);

            btn_accept = itemView.findViewById(R.id.btn_accept);
            btn_reject = itemView.findViewById(R.id.btn_reject);
            text_username = itemView.findViewById(R.id.text_username);
            text_time = itemView.findViewById(R.id.text_time);
            text_dis = itemView.findViewById(R.id.text_dis);
            request_image = itemView.findViewById(R.id.request_image);
        }
    }

    public void acceptDialog(){
        Dialog dialog = new Dialog(fragment.getActivity());
        View view = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.request_accept_dialog,null);
        dialog.setContentView(view);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView btn_close = dialog.findViewById(R.id.btn_close);

        TextView txt_user = dialog.findViewById(R.id.txt_user);
        TextView txt_ph_mo = dialog.findViewById(R.id.txt_ph_mo);
        TextView txt_email = dialog.findViewById(R.id.txt_email);
        TextView txt_media = dialog.findViewById(R.id.txt_media);

        SwitchButton name_switch = dialog.findViewById(R.id.name_switch);
        SwitchButton number_switch = dialog.findViewById(R.id.number_switch);
        SwitchButton email_switch = dialog.findViewById(R.id.email_switch);
        SwitchButton media_switch = dialog.findViewById(R.id.media_switch);

        AppCompatButton button_cancle = dialog.findViewById(R.id.button_cancle);
        AppCompatButton button_accept = dialog.findViewById(R.id.button_accpet);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        button_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(fragment.getContext(), "Accept", Toast.LENGTH_SHORT).show();
            }
        });

        button_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void rejectDialog(){
        Dialog dialog =new Dialog(fragment.getActivity());
        View view = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.reject_dialog,null);
        dialog.setContentView(view);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView dialog_close = dialog.findViewById(R.id.dialog_close);
        AppCompatButton button_reject = dialog.findViewById(R.id.button_reject);
        AppCompatButton button_permanent_reject = dialog.findViewById(R.id.button_permanent_reject);

        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        button_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(fragment.getContext(), "Reject", Toast.LENGTH_SHORT).show();
            }
        });

        button_permanent_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(fragment.getContext(), "Block", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }


}
