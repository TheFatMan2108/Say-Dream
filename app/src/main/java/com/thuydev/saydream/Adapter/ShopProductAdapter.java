package com.thuydev.saydream.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.thuydev.saydream.databinding.ItemCuahangSanphamBinding;

import java.util.ArrayList;
import java.util.List;

public class ShopProductAdapter extends RecyclerView.Adapter<ShopProductAdapter.ViewHoder> implements Filterable {
    Context context;
    List<Product> productList, listSearch;
    ItemCuahangSanphamBinding view;
    public ShopProductAdapter(Context context, List<Product> productList, ICallBackAction action) {
        this.context = context;
        this.productList = productList;
        listSearch = productList;
        action.CallBack(productList.size());
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = ItemCuahangSanphamBinding.inflate(((Activity)context).getLayoutInflater(),parent,false);
        return new ViewHoder(view.getRoot());
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        holder.nameProduct.setText(productList.get(position).getName());
        holder.price.setText(FomatExtention.MakeStyleMoney(productList.get(position).getPrice())+" đ");
        Glide.with(context).load(productList
                .get(position)
                .getImage())
                .error(R.drawable.baseline_crop_original_24)
                .into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseExtention.CreatedDeepLinkProduct(productList.get(position), (Activity) context, new ICallBackAction() {
                    @Override
                    public void CallBack(Object... obj) {
                        Intent intent = new Intent(context, ActivityProductDetail.class);
                        Uri link = (Uri)obj[0];
                        Log.e(Tag.TAG_LOG, "CallBack: "+link );
                        intent.setData(link);
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint.toString().isEmpty()) {
                    productList = listSearch;
                } else {
                    List<Product> listPro = new ArrayList<>();
                    for (Product pd : listSearch) {
                        if (pd.getName().toLowerCase().trim().contains(constraint.toString().toLowerCase().trim())) {
                            listPro.add(pd);
                            Log.e(Tag.TAG_LOG, "performFiltering: "+constraint+" - "+pd.getName() );
                        }
                    }
                    productList=listPro;
                }
                FilterResults results = new FilterResults();
                results.values = productList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                productList = (List<Product>) results.values;
                notifyDataSetChanged();
            }
        };
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
