package com.example.friendfield.Reels.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.friendfield.R;
import com.example.friendfield.Reels.model.StickerRes;

import java.util.List;


public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

    private List<StickerRes> mData;
    private OnItemClickListener onItemClickListener;
    public StickerAdapter(List<StickerRes> mData) {
        this.mData = mData;
    }

    public interface OnItemClickListener {
        void onItemClick(StickerRes item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stickerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.iv_sticker.setImageResource(mData.get(position).getImgRes());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(mData.get(pos), pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_sticker;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_sticker = itemView.findViewById(R.id.iv_sticker);
        }
    }
}
