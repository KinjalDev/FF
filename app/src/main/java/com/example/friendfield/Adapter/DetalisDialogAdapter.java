package com.example.friendfield.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendfield.Activity.PromotionDetailsActivity;
import com.example.friendfield.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetalisDialogAdapter extends RecyclerView.Adapter<DetalisDialogAdapter.MyDataHolder> {

    Activity activity;
    int[] d_img;
    String[] d_name;
    LayoutInflater inflater;

    public DetalisDialogAdapter(PromotionDetailsActivity messageDetailsActivity, int[] details_img, String[] details_name) {
        this.activity = messageDetailsActivity;
        this.d_img = details_img;
        this.d_name = details_name;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public MyDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_people_noti, parent, false);
        return new MyDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDataHolder holder, int position) {
        holder.people_img.setImageResource(d_img[position]);
        holder.txt_peoplename.setText(d_name[position]);
        holder.txt_peoplegmail.setText(d_name[position]);
    }

    @Override
    public int getItemCount() {
        return d_img.length;
    }

    class MyDataHolder extends RecyclerView.ViewHolder {

        CircleImageView people_img;
        TextView txt_peoplename, txt_peoplegmail;

        public MyDataHolder(@NonNull View itemView) {
            super(itemView);
            people_img = itemView.findViewById(R.id.people_img);
            txt_peoplename = itemView.findViewById(R.id.txt_peoplename);
            txt_peoplegmail = itemView.findViewById(R.id.txt_peoplegmail);
        }
    }
}
