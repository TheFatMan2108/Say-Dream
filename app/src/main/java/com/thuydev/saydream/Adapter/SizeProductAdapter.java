package com.thuydev.saydream.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thuydev.saydream.databinding.ItemKichcoBinding;

import java.util.List;

public class SizeProductAdapter extends RecyclerView.Adapter<SizeProductAdapter.ViewHolder> {
    Context context;
    List<String> list;
    ItemKichcoBinding view;
    int index = -1;
    public SizeProductAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = ItemKichcoBinding.inflate(((Activity)context).getLayoutInflater(),parent,false);
        return new ViewHolder(view.getRoot());
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.size.setText(list.get(position));
        String cam = "#FF4800";
        String vang = "#FFC107";
        if (position==index){

            holder.bgColor.setBackgroundColor(Color.parseColor(cam));
        }else {
            holder.bgColor.setBackgroundColor(Color.parseColor(vang));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                notifyDataSetChanged();
                // lam gi do o day
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView size;
        LinearLayout bgColor;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            size = view.tvKichcoShow1;
            bgColor = view.cvKichco;
        }
    }
}
