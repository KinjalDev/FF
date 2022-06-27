package com.example.friendfield.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendfield.Fragment.SendRequestFragment;
import com.example.friendfield.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SendRequestAdapter extends RecyclerView.Adapter<SendRequestAdapter.MyViewHolder> {

    Fragment context;
    int[] img;
    String[] name;
    String[] dis;
    LayoutInflater inflater;

    public SendRequestAdapter(SendRequestFragment sendRequestFragment, int[] user_img, String[] user_name, String[] user_dis) {
        this.context = sendRequestFragment;
        this.img = user_img;
        this.name = user_name;
        this.dis = user_dis;
        inflater = LayoutInflater.from(context.getContext());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.send_request_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.text_username.setText(name[position]);
        holder.text_dis.setText(dis[position]);
        holder.send_img.setImageResource(img[position]);
    }

    @Override
    public int getItemCount() {
        return img.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView send_img;
        TextView text_username,text_time,text_dis,txt_request_check;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            send_img = itemView.findViewById(R.id.send_image);
            text_username = itemView.findViewById(R.id.text_username);
            text_time = itemView.findViewById(R.id.txt_time);
            text_dis = itemView.findViewById(R.id.text_dis);
            txt_request_check = itemView.findViewById(R.id.txt_request_check);
        }
    }
}
