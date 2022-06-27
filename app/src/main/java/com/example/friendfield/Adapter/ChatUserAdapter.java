package com.example.friendfield.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendfield.Activity.ChatingActivity;
import com.example.friendfield.Fragment.ChatFragment;
import com.example.friendfield.R;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.MyDataViewHolder> {

    Fragment activity;
    int[] user_img;
    //    String[] user_name;
    ArrayList<String> user_name;
    LayoutInflater inflater;
    ArrayList<String> arraylist;

    //    public ChatUserAdapter(ChatFragment chatFragment, int[] user_img, String[] user_name) {
    public ChatUserAdapter(ChatFragment chatFragment, int[] user_img, ArrayList<String> user_name) {
        this.activity = chatFragment;
        this.user_img = user_img;
        this.user_name = user_name;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(this.user_name);
        inflater = LayoutInflater.from(activity.getContext());
    }

    @NonNull
    @Override
    public MyDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user_chat_list, parent, false);
        return new MyDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDataViewHolder holder, int position) {
        holder.img_user.setImageResource(user_img[position]);
        holder.txt_user_name.setText(user_name.get(position));
        holder.txt_message.setText(user_name.get(position));



        holder.ll_user_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity.getContext(), ChatingActivity.class);
//                intent.putExtra("Name",user_name[position]);
                intent.putExtra("Name", user_name.get(position));
                activity.startActivity(intent);
//                activity.getActivity().finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return user_name.size();
    }

    class MyDataViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_user;
        TextView txt_user_name, txt_time, txt_message, txt_chat_count;
        LinearLayout ll_user_view;
        RelativeLayout iv_circle_image;
        ImageView iv_type_img;

        public MyDataViewHolder(@NonNull View itemView) {
            super(itemView);

            img_user = itemView.findViewById(R.id.img_user);
            txt_user_name = itemView.findViewById(R.id.txt_user_name);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_message = itemView.findViewById(R.id.txt_message);
            txt_chat_count = itemView.findViewById(R.id.txt_chat_count);
            ll_user_view = itemView.findViewById(R.id.ll_user_view);

            iv_circle_image = itemView.findViewById(R.id.iv_circle_image);
            iv_type_img = itemView.findViewById(R.id.iv_type_img);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        user_name.clear();
        if (charText.length() == 0) {
            user_name.addAll(arraylist);
        } else {
            for (String wp : arraylist) {
//                if (wp.getAnimalName().toLowerCase(Locale.getDefault()).contains(charText)) {
                if (wp.toLowerCase(Locale.getDefault()).contains(charText)) {
                    user_name.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
