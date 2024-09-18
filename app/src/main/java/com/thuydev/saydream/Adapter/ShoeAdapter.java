package com.thuydev.saydream.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thuydev.saydream.Model.Shoe;
import com.thuydev.saydream.R;

import java.util.List;

public class ShoeAdapter extends RecyclerView.Adapter<ShoeAdapter.ShoeViewHolder> {

    private List<Shoe> mShoes;

    public void setData(List<Shoe> list) {
        this.mShoes = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShoeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shoe, parent, false);

        return new ShoeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoeViewHolder holder, int position) {
        Shoe shoe = mShoes.get(position);
        if (shoe == null){
            return;
        }

        holder.imgShoe.setImageResource(shoe.getResouceId());
        holder.tvNew.setText(shoe.getNew());
        holder.tvPrice.setText(shoe.getPrice());
        holder.tvName.setText(shoe.getName());


    }

    @Override
    public int getItemCount() {
        if (mShoes != null) {
            return mShoes.size();
        }
        return 0;
    }

    public class ShoeViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgShoe;
        private TextView tvNew;
        private TextView tvPrice;
        private TextView tvName;


        public ShoeViewHolder(@NonNull View itemView) {
            super(itemView);

            imgShoe = itemView.findViewById(R.id.img_shoe);
            tvNew = itemView.findViewById(R.id.tv_new);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
