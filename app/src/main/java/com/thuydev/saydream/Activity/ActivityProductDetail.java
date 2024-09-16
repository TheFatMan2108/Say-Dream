package com.thuydev.saydream.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.thuydev.saydream.Adapter.SizeProductAdapter;
import com.thuydev.saydream.DTO.Product;
import com.thuydev.saydream.Extentions.FirebaseExtention;
import com.thuydev.saydream.Extentions.FomatExtention;
import com.thuydev.saydream.Extentions.Tag;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.R;
import com.thuydev.saydream.databinding.ActivitySanphamShowBinding;

import java.util.ArrayList;
import java.util.List;

public class ActivityProductDetail extends AppCompatActivity {
    ActivitySanphamShowBinding view;
    int quantity = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivitySanphamShowBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        GetData(new ICallBackAction() {
            @Override
            public void CallBack(Object... obj) {
                // Do some thing
                String id = obj[0].toString();
                FirebaseExtention.GetProduct(id, new ICallBackAction() {
                    @Override
                    public void CallBack(Object... obj) {
                        if(obj==null||obj.length<1)return;
                        Product product = (Product) obj[0];
                        view.tvTenspShow.setText(product.getName());
                        view.tvGiaspShow.setText(FomatExtention.MakeStyleMoney(product.getPrice())+" đ");
                        view.tvMamspShow.setText(product.getYearOfManufacture());
                        Glide.with(ActivityProductDetail.this).
                                load(product.getImage())
                                .error(R.drawable.baseline_crop_original_24)
                                .into(view.imvAnhSpLon);
                        List<String> listSize = new ArrayList<>();
                        listSize.addAll(product.getSize());
                        SizeProductAdapter sizeProductAdapter = new SizeProductAdapter(ActivityProductDetail.this,listSize);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityProductDetail.this, RecyclerView.HORIZONTAL,false);
                        view.rcvListco.setAdapter(sizeProductAdapter);
                        view.rcvListco.setLayoutManager(layoutManager);
                        sizeProductAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        view.edtSoluongShow.setText(quantity +"");
        view.bntTruSoluong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetQuantity("-");
            }
        });
        view.bntCongSoluong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetQuantity("+");
            }
        });
        view.edtSoluongShow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()||Integer.valueOf(s.toString())<1){
                    s="1";
                }
                if (Integer.parseInt(s.toString())>30){
                    Toast.makeText(ActivityProductDetail.this, R.string.max30, Toast.LENGTH_SHORT).show();
                    quantity=30;
                    view.edtSoluongShow.setText(quantity+"");
                    return;
                }
                quantity=Integer.parseInt(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void SetQuantity(String dau) {
        if ("-".equals(dau)) {
            quantity -= 1;
            if (quantity <1) {
                quantity = 1;
            }
        } else {
            quantity += 1;
        }
        if (quantity >30){
            Toast.makeText(this, R.string.max30, Toast.LENGTH_SHORT).show();
            quantity =30;
            view.edtSoluongShow.setText(quantity +"");
            return;
        }
        view.edtSoluongShow.setText(quantity + "");
    }
    private void GetData(ICallBackAction action) {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                           String idProduct= deepLink.getQueryParameter("id");
                            Log.e(Tag.TAG_LOG, "onSuccess: "+idProduct );
                            action.CallBack(idProduct);
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Tag.TAG_LOG, "getDynamicLink:onFailure", e);
                    }
                });
    }
}