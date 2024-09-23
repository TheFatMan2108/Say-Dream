package com.thuydev.saydream.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thuydev.saydream.DTO.Bill;
import com.thuydev.saydream.Extentions.FomatExtention;
import com.thuydev.saydream.R;
import com.thuydev.saydream.databinding.ItemChodonBinding;
import com.thuydev.saydream.databinding.ItemLichsuggBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DepositMoneyAdapter extends RecyclerView.Adapter<DepositMoneyAdapter.ViewHolder> {
ItemLichsuggBinding view;
Context context;
List<HashMap<String,Object>> list;

    public DepositMoneyAdapter(Context context, List<HashMap<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = ItemLichsuggBinding.inflate(((Activity)context).getLayoutInflater(),parent,false);
        return new ViewHolder(view.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String Xanh = "#44cc00";
        String Cam = "#FFC107";

        HashMap<String,Object> data = list.get(position);
        holder.id.setText(String.format("%s", data.get("id")));
        holder.money.setText(String.format("%s %s", data.get("money"),"VND"));
        holder.date.setText(String.format("%s", data.get("time")));
        int status = Integer.parseInt(Objects.requireNonNull(data.get("status")).toString());
        if (status == 0) {
            holder.status.setText(R.string.wait);
            holder.status.setTextColor(Color.parseColor(Cam));
        } else if (status == 1) {
            holder.status.setText(R.string.done);
            holder.status.setTextColor(Color.parseColor(Xanh));
        } else {
            holder.status.setText(R.string.error);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id, money, date, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = view.tvMaGG;
            money = view.tvSotienGg;
            date = view.tvThoigiangg;
            status = view.tvTrangThaiGg;
        }
    }
}
