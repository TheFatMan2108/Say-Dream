package com.thuydev.saydream.Activity;

import static android.content.ContentValues.TAG;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import com.thuydev.saydream.Adapter.CartAdapter;

import com.thuydev.saydream.DTO.Cart;
import com.thuydev.saydream.Extentions.ActivityExtentions;
import com.thuydev.saydream.Extentions.FomatExtention;
import com.thuydev.saydream.Extentions.Tag;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.R;
import com.thuydev.saydream.databinding.FragmentGioHangBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class ActivityCart extends AppCompatActivity {
    FragmentGioHangBinding view;
    CartAdapter cartAdapter;
    List<Cart> listCart;
    Long totalPrice = 0L;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = FragmentGioHangBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        progressDialog = new ProgressDialog(this);
        listCart = new ArrayList<>();
        view.tvGioGia.setText(FomatExtention.MakeStyleMoney(0l));

        view.rcvListgio.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        loadCartData();
        cartAdapter = new CartAdapter(listCart, this, new ICallBackAction() {

            @Override
            public void CallBack(Object... obj) {
                loadCartData();
            }
        },
                new ICallBackAction() {
                    @Override
                    public void CallBack(Object... obj) {
                        totalPrice += (Long) obj[0];
                        view.tvGioGia.setText(FomatExtention.MakeStyleMoney(totalPrice));
                    }
                }, getLayoutInflater());
        view.rcvListgio.setAdapter(cartAdapter);

        view.llThemgio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadCartData();
                if (listCart.isEmpty()) {
                    new AlertDialog.Builder(ActivityCart.this)
                            .setMessage(R.string.emptyCart)
                            .setTitle(R.string.deleteCart)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                }else {


                new AlertDialog.Builder(ActivityCart.this)
                        .setTitle(R.string.deleteCart)
                        .setMessage(R.string.paymentCart)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {


                                payment(new ICallBackAction() {
                                    @Override
                                    public void CallBack(Object... obj) {
                                        String url = obj[0].toString();
                                        ActivityExtentions.ShowQR(url,ActivityCart.this);
                                        deleteAllCart();
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
                Log.e(TAG, "onClick: " + totalPrice);
            }
            }
        });

    }

    private void deleteAllCart() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (Cart c : listCart) {
            db.collection(Tag.DTO_CART).document(c.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            view.tvGioGia.setText(FomatExtention.MakeStyleMoney(0l));
                            getListCart(new ICallBackAction() {
                                @Override
                                public void CallBack(Object... obj) {
                                    loadCartData();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }


    }

    private void payment(ICallBackAction callBackAction) {
        ShowProgressDialog();
        Map<String, Object> data = new HashMap<>();
        String id = UUID.randomUUID().toString();
        String idUser = FirebaseAuth.getInstance().getUid();
        data.put("id", id);
        data.put("totalPrice", totalPrice);
        data.put("listSP", listCart);
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
                        String formattedUrl = String.format(urlTemplate, totalPrice, id);
                        callBackAction.CallBack(formattedUrl);
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        new AlertDialog.Builder(ActivityCart.this)
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



    private void loadCartData() {
        getListCart(new ICallBackAction() {
            @Override
            public void CallBack(Object... obj) {
                totalPrice = 0L;
                view.tvGioGia.setText(FomatExtention.MakeStyleMoney(totalPrice));
                listCart.clear();
                listCart.addAll((List<Cart>) obj[0]);
                cartAdapter.notifyDataSetChanged();

            }
        });
    }

    private void getListCart(ICallBackAction action) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String idUser = FirebaseAuth.getInstance().getUid();
        db.collection(Tag.DTO_CART).whereEqualTo("idUser", idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    action.CallBack(task.getResult().toObjects(Cart.class));
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void ShowProgressDialog() {
        progressDialog.setTitle("Loading");
        progressDialog.setMessage(getString(R.string.MessageLoading));
        progressDialog.show();
    }

}
