package com.thuydev.saydream.Adapter;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thuydev.saydream.Activity.ActivityCart;
import com.thuydev.saydream.DTO.Bill;
import com.thuydev.saydream.DTO.Cart;
import com.thuydev.saydream.DTO.Product;
import com.thuydev.saydream.Extentions.ActivityExtentions;
import com.thuydev.saydream.Extentions.FomatExtention;
import com.thuydev.saydream.Extentions.Tag;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.R;
import com.thuydev.saydream.databinding.DialogThemHangBinding;
import com.thuydev.saydream.databinding.ItemChodonBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {
ItemChodonBinding view;
Context context;
List<Bill> list;
BillAdapterDetail adapter;
ICallBackAction reloadList;
    private ProgressDialog progressDialog;
    public BillAdapter(Context context, List<Bill> list,ICallBackAction action) {
        this.context = context;
        this.list = list;
        reloadList = action;
        progressDialog = new ProgressDialog(context);

        Log.d(TAG, "List size: " + list.size());
        for (Bill bill : list) {
            Log.d(TAG, "Bill ID: " + bill.getId());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = ItemChodonBinding.inflate(((Activity)context).getLayoutInflater(),parent,false);
        Log.d(TAG, "onBindViewHolder called for position: " + viewType);
        return new ViewHolder(view.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String Xanh = "#44cc00";
        String Do = "#FF0000";
        String Cam = "#FFC107";

        Log.d(TAG, "Binding data for position: " + position);

        Bill bill = list.get(position);
        Log.d(TAG, "Bill ID: " + bill.getId());
        String priceTotal = context.getString(R.string.totalPrice);
        String id = context.getString(R.string.idBill);
        String quantity = context.getString(R.string.quantity);
        String staff = context.getString(R.string.staff);
        String date = context.getString(R.string.date);


        holder.price.setText(String.format("%s: %s", priceTotal, FomatExtention.MakeStyleMoney(bill.getTotalPrice())));
        holder.name.setText(String.format("%s: %s", id, bill.getId()));
        holder.quantity.setText(String.format("%s: %s", quantity, getTotalQuantity(bill)));
        holder.date.setText(String.format("%s: %s", date, bill.getDate()));

        holder.staff.setText(String.format("%s: %s", staff, bill.getIdStaff()));

        if (bill.getStatus() == 0) {
            holder.status.setText(R.string.waitDone);
            holder.status.setTextColor(Color.parseColor(Cam));
            holder.reBuy.setVisibility(View.GONE);
        } else if (bill.getStatus() == 1) {
            holder.status.setText(R.string.doneBill);
            holder.reBuy.setVisibility(View.VISIBLE);
            holder.reBuy.setText(R.string.reBuy);
            holder.status.setTextColor(Color.parseColor(Xanh));
        } else if (bill.getStatus() == 3) {
            holder.status.setText(R.string.cancelBill);
            holder.status.setTextColor(Color.parseColor(Do));
            holder.reBuy.setVisibility(View.GONE);
        } else {
            holder.status.setText(R.string.error);
        }

        holder.reBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // viet ham mua lai o day
                new AlertDialog.Builder((Activity)context)
                        .setTitle(R.string.deleteCart)
                        .setMessage(R.string.paymentCart)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {


                                payment(position,new ICallBackAction() {
                                    @Override
                                    public void CallBack(Object... obj) {
                                        String url = obj[0].toString();
                                        ActivityExtentions.ShowQR(url,(Activity)context);
                                        reloadList.CallBack();

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        }).show();

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // viet ham hien thi chi tiet bill o day : cac san pham va so luong tung san pham
                adapter= new BillAdapterDetail(list.get(position).getListSP(),context);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                DialogThemHangBinding view1 = DialogThemHangBinding.inflate(((Activity)context).getLayoutInflater());
                builder.setView(view1.getRoot());
                Dialog dialog = builder.create();
                dialog.show();

                view1.ibtnAddhang.setVisibility(View.GONE);;
                view1.edtThemhang.setVisibility(View.GONE);;
                String title = context.getString(R.string.titleBilldetail);
                view1.tvTittle2.setText(title);
                view1.listHang.setAdapter(adapter);


            }
        });

    }
    private void payment(Integer i ,ICallBackAction callBackAction) {
        ShowProgressDialog();
        Map<String, Object> data = new HashMap<>();
        String id = UUID.randomUUID().toString();
        String idUser = FirebaseAuth.getInstance().getUid();
        data.put("id", id);
        data.put("totalPrice", list.get(i).getTotalPrice());
        data.put("listSP", list.get(i).getListSP());
        data.put("idUser", idUser);
        data.put("idStaff", "?");
        data.put("date", ActivityExtentions.getDate());
        data.put("status", 0);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Tag.DTO_BILL)
                .document(id)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String urlTemplate = "https://img.vietqr.io/image/vietinbank-101870446659-compact2.jpg?amount=%d&addInfo=%s";
                        String formattedUrl = String.format(urlTemplate, list.get(i).getTotalPrice(), id);
                        callBackAction.CallBack(formattedUrl);
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        new AlertDialog.Builder((Activity)context)
                                .setTitle(R.string.deleteCart)
                                .setMessage(R.string.failPayment)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Hành động khi bấm OK
                                        dialog.dismiss(); // Đóng dialog
                                    }
                                })
                                .show();
                    }
                });
    }
    private long getTotalQuantity(Bill bill){
        long totalQuantity = 0;
            if (bill.getListSP()!=null){
                for (Cart cart : bill.getListSP()) {
                    totalQuantity += cart.getQuantity();

                }
            }
        return totalQuantity;
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + list.size());
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

    private void ShowProgressDialog() {
        progressDialog.setTitle("Loading");
        progressDialog.setMessage(context.getString(R.string.MessageLoading));
        progressDialog.show();
    }
}
