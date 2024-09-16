package com.thuydev.saydream.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.thuydev.saydream.Activity.ActivityProductDetail;
import com.thuydev.saydream.DTO.Product;
import com.thuydev.saydream.Extentions.FirebaseExtention;
import com.thuydev.saydream.Extentions.FomatExtention;
import com.thuydev.saydream.Extentions.Tag;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.R;
import com.thuydev.saydream.databinding.ActivitySanphamShowBinding;
import com.thuydev.saydream.databinding.ItemCuahangBinding;
import com.thuydev.saydream.databinding.ItemCuahangSanphamBinding;

import java.util.List;

public class ShopProductAdapter extends RecyclerView.Adapter<ShopProductAdapter.ViewHoder>{
    Context context;
    List<Product> productList;
    ItemCuahangSanphamBinding view;
    public ShopProductAdapter(Context context, List<Product> productList, ICallBackAction action) {
        this.context = context;
        this.productList = productList;
        action.CallBack(productList.size());
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = ItemCuahangSanphamBinding.inflate(((Activity)context).getLayoutInflater(),parent,false);
        return new ViewHoder(view.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        view.tvTenspCuahang.setText(productList.get(position).getName());
        view.tvGiaspCuahang.setText(FomatExtention.MakeStyleMoney(productList.get(position).getPrice())+" đ");
        Glide.with(context).load(productList
                .get(position)
                .getImage())
                .error(R.drawable.baseline_crop_original_24)
                .into(view.imvAnhSpCuahang);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseExtention.CreatedDeepLinkProduct(productList.get(position), (Activity) context, new ICallBackAction() {
                    @Override
                    public void CallBack(Object... obj) {
                        Intent intent = new Intent(context, ActivityProductDetail.class);
                        Uri link = (Uri)obj[0];
                        Log.e(Tag.TAG_LOG, "CallBack: "+link );
                        intent.putExtra(Tag.ID_PRODUCT,link);
                        ((Activity)context).startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHoder extends ViewHolder{
        TextView nameProduct,price;
        ImageView image;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            image = view.imvAnhSpCuahang;
            nameProduct = view.tvTenspCuahang;
            price = view.tvGiaspCuahang;
        }
    }
}
