package com.example.friendfield.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.friendfield.Activity.AddProductActivity;
import com.example.friendfield.Activity.ProductDetailsActivity;
import com.example.friendfield.Model.CreateGroupModel;
import com.example.friendfield.Model.Product.ProductDetailsModel;
import com.example.friendfield.R;
import com.example.friendfield.Utils.Constans;
import com.example.friendfield.Utils.FileUtils;

import java.util.ArrayList;

public class AllProductDisplayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<ProductDetailsModel> productDetailsModelArrayList;
    private static final int CONTENT_TYPE = 0;
    private static final int AD_TYPE = 1;

    public AllProductDisplayAdapter(Context context, ArrayList<ProductDetailsModel> productDetailsModelArrayList) {
        this.context = context;
        this.productDetailsModelArrayList = productDetailsModelArrayList;
    }

    @Override
    public int getItemViewType(int position) {
        if (productDetailsModelArrayList.get(position) == null)
            return AD_TYPE;
        return CONTENT_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == AD_TYPE) {
            View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_ads_view, parent, false);
            return new AdRecyclerHolder(view1);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_product_display, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        if (viewType == CONTENT_TYPE) {
            ViewHolder viewHolder = (ViewHolder) holder;

            Glide.with(context).load(R.drawable.headpone).into(viewHolder.iv_pro_image);

            viewHolder.txt_pro_name.setText(productDetailsModelArrayList.get(position).getName());
            viewHolder.txt_pro_des.setText(productDetailsModelArrayList.get(position).getDescription());
//        viewHolder.txt_pro_price.setText("$" + String.valueOf(productDetailsModelArrayList.get(position).getPrice()) + ".00");
            viewHolder.txt_pro_price.setText("$" + String.format("%,.0f", Float.valueOf(productDetailsModelArrayList.get(position).getPrice())) + ".00");

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    intent.putExtra("isFromUser", true);
//                intent.putExtra("ProductDetails", productDetailsModelArrayList.get(position));
                    intent.putExtra("ProductDetailsId", productDetailsModelArrayList.get(position).getId());
                    context.startActivity(intent);
                }
            });
        } else {
            AdRecyclerHolder adRecyclerHolder = (AdRecyclerHolder) holder;

        }

    }

    public void filterList(ArrayList<ProductDetailsModel> filterllist) {
        productDetailsModelArrayList = filterllist;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return productDetailsModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_pro_image;
        TextView txt_pro_name;
        TextView txt_pro_des;
        TextView txt_pro_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_pro_image = itemView.findViewById(R.id.iv_pro_image);
            txt_pro_name = itemView.findViewById(R.id.txt_pro_name);
            txt_pro_des = itemView.findViewById(R.id.txt_pro_des);
            txt_pro_price = itemView.findViewById(R.id.txt_pro_price);

        }
    }

    class AdRecyclerHolder extends RecyclerView.ViewHolder {

        public AdRecyclerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
