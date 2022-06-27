package com.example.friendfield.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendfield.Activity.BlockedContactActivity;
import com.example.friendfield.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlockeContactAdapter extends RecyclerView.Adapter<BlockeContactAdapter.MyViewHolder> {

    Activity activity;
    int[] u_img;
    String[] u_contact;
    LayoutInflater inflater;

    public BlockeContactAdapter(BlockedContactActivity blockedContactActivity, int[] user_img, String[] contact) {
        this.activity = blockedContactActivity;
        this.u_img = user_img;
        this.u_contact = contact;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.blocked_contact_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.circleImageView.setImageResource(u_img[position]);
        holder.txt_contact.setText(u_contact[position]);
        holder.btn_clear_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Unblock", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return u_img.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView txt_contact;
        AppCompatButton btn_clear_data;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.contact_user);
            txt_contact = itemView.findViewById(R.id.txt_contact);
            btn_clear_data = itemView.findViewById(R.id.btn_clear_data);
        }
    }
}
