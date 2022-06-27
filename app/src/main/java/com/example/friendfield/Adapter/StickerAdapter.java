package com.example.friendfield.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.friendfield.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {
    Context context;
    ArrayList<String> listSticker;
    OnItemClickListener mOnItemClickListener;

    public StickerAdapter(Context context, ArrayList<String> listSticker, OnItemClickListener mOnItemClickListener) {
        this.context = context;
        this.listSticker = listSticker;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(String imgsticker);
    }

//    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//        mOnItemClickListener = onItemClickListener;
//    }

    @NonNull
    @Override
    public StickerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerAdapter.ViewHolder holder, int position) {

        InputStream inputstream = null;
        try {
            inputstream = context.getAssets().open("stickers/"
                    + listSticker.get(position));
            Drawable drawable = Drawable.createFromStream(inputstream, null);
            holder.iv_sticker.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        Glide.with(context).load(listSticker.get(position)).into(holder.iv_sticker);
//
//        holder.iv_sticker.setImageResource(Integer.parseInt(listSticker.get(position)));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(listSticker.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return listSticker.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_sticker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_sticker = itemView.findViewById(R.id.iv_sticker);
        }
    }
}
