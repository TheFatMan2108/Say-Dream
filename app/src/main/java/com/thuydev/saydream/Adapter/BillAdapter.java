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

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {
ItemChodonBinding view;
Context context;
List<Bill> list;

    public BillAdapter(Context context, List<Bill> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = ItemChodonBinding.inflate(((Activity)context).getLayoutInflater(),parent,false);
        return new ViewHolder(view.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String Xanh = "#44cc00";
        String Do = "#FF0000";
        String Cam = "#FFC107";

        Bill bill = list.get(position);
        String priceTotal = context.getString(R.string.totalPrice);
        String id = context.getString(R.string.idBill);
        String quantity = context.getString(R.string.quantity);
        String staff = context.getString(R.string.staff);
        String date = context.getString(R.string.date);

        holder.price.setText(String.format("%s: %s", priceTotal, FomatExtention.MakeStyleMoney(bill.getTotalPrice())));
        holder.name.setText(String.format("%s: %s", id, bill.getId()));
        holder.quantity.setText(String.format("%s: %s", quantity, bill.getQuantity()));
        holder.date.setText(String.format("%s: %s", date, bill.getTime()));
        holder.staff.setText(String.format("%s: %s", staff, bill.getIdStaff()));

        if (bill.getStatus() == 0) {
            holder.status.setText(R.string.waitDone);
            holder.status.setTextColor(Color.parseColor(Cam));
            holder.reBuy.setVisibility(View.GONE);
        } else if (bill.getStatus() == 1) {
            holder.status.setText(R.string.doneBill);
            holder.reBuy.setVisibility(View.GONE);
            holder.status.setTextColor(Color.parseColor(Xanh));
        } else if (bill.getStatus() == 3) {
            holder.status.setText(R.string.cancelBill);
            holder.status.setTextColor(Color.parseColor(Do));
            holder.reBuy.setVisibility(View.VISIBLE);
        } else {
            holder.status.setText(R.string.error);
        }

        holder.reBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // viet ham mua lai o day
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // viet ham hien thi chi tiet bill o day : cac san pham va so luong tung san pham
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, quantity, price, status, reBuy, date,staff;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = view.tvTenspGioHang;
            quantity = view.tvSoluongspGiohang;
            price = view.tvGiaspGiohang;
            status = view.tvMuaGiohang;
            reBuy = view.tvXoaGiohang;
            date = view.tvNgayMua;
            staff = view.tvNguoiduyet;
        }
    }
}
