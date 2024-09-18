package com.thuydev.saydream.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.thuydev.saydream.Activity.ActivitySeeMoreProduct;
import com.thuydev.saydream.DTO.Categoty;
import com.thuydev.saydream.DTO.Product;
import com.thuydev.saydream.Extentions.Tag;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.databinding.ItemCuahangBinding;

import java.util.ArrayList;
import java.util.List;

public class ShopCateAdapter extends RecyclerView.Adapter<ShopCateAdapter.ViewHoder> implements Filterable {
    Context context;
    List<Categoty> listCate,listSearch;
    ItemCuahangBinding view;

    public ShopCateAdapter(Context context, List<Categoty> listCate) {
        this.context = context;
        this.listCate = listCate;
        listSearch = listCate;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = ItemCuahangBinding.inflate(((Activity) context).getLayoutInflater(),parent,false);
        return new ViewHoder(view.getRoot());
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        holder.nameCate.setText(listCate.get(position).getName());
        GetProduct(listCate.get(position).getId(),holder);
        holder.seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivitySeeMoreProduct.class);
                intent.putExtra(Tag.ID_CATEGORY,listCate.get(position).getId());
                intent.putExtra(Tag.NAME_CATEGORY,listCate.get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCate.size();
    }

    public void GetProduct(String idCate, ViewHoder holder) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Product> productList = new ArrayList<>();
        final ShopProductAdapter[] shopProductAdapter = new ShopProductAdapter[1];
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        holder.rcListSp.setLayoutManager(layoutManager);
        db.collection(Tag.DTO_PRODUCT).whereEqualTo("idCategory", idCate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                productList.addAll(task.getResult().toObjects(Product.class));
                shopProductAdapter[0] = new ShopProductAdapter(context, productList, new ICallBackAction() {
                    @Override
                    public void CallBack(Object... obj) {
                        int listSize = (Integer)obj[0];
                        if(listSize<1)holder.itemView.setVisibility(View.GONE);
                    }
                });
                holder.rcListSp.setAdapter(shopProductAdapter[0]);
                shopProductAdapter[0].notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(Tag.TAG_LOG, "onFailure: ", e);
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint.toString().isEmpty()) {
                    listCate = listSearch;
                } else {
                    List<Categoty> listCat = new ArrayList<>();
                    for (Categoty categoty : listSearch) {
                        if (categoty.getName().toLowerCase().trim().contains(constraint.toString().toLowerCase().trim())) {
                            listCat.add(categoty);
                        }
                    }
                    listCate=listCat;

                }
                FilterResults results = new FilterResults();
                results.values = listCate;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listCate = (List<Categoty>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHoder extends ViewHolder {
        RecyclerView rcListSp;
        TextView seeMore,nameCate;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            rcListSp = view.rcvListSpKhach;
            nameCate = view.tvTenhang;
            seeMore = view.llXemthemMoi;
        }
    }
}
