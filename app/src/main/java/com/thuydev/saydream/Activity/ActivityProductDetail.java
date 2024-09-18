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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.thuydev.saydream.Adapter.SizeProductAdapter;
import com.thuydev.saydream.DTO.Cart;
import com.thuydev.saydream.DTO.Product;
import com.thuydev.saydream.Extentions.FirebaseExtention;
import com.thuydev.saydream.Extentions.FomatExtention;
import com.thuydev.saydream.Extentions.Tag;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.R;
import com.thuydev.saydream.databinding.ActivitySanphamShowBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActivityProductDetail extends AppCompatActivity {
    ActivitySanphamShowBinding view;
    Product product;
    int quantity = 1;
    String size = "";
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
                        product = (Product) obj[0];
                        view.tvTenspShow.setText(product.getName());
                        view.tvGiaspShow.setText(FomatExtention.MakeStyleMoney(product.getPrice())+" đ");
                        view.tvMamspShow.setText(product.getYearOfManufacture());
                        Glide.with(ActivityProductDetail.this).
                                load(product.getImage())
                                .error(R.drawable.baseline_crop_original_24)
                                .into(view.imvAnhSpLon);
                        List<String> listSize = new ArrayList<>();
                        listSize.addAll(product.getSize());
                        SizeProductAdapter sizeProductAdapter = new SizeProductAdapter(ActivityProductDetail.this, listSize, new ICallBackAction() {
                            @Override
                            public void CallBack(Object... obj) {
                                size = (String) obj[0];
                            }
                        });
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
        view.btnThemgio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCart();
            }
        });
    }

    private void AddCart() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(size.isEmpty()){
            Toast.makeText(this, R.string.nullSize, Toast.LENGTH_SHORT).show();
            return;
        }
        CheckProductInCart(new ICallBackAction() {
            @Override
            public void CallBack(Object... obj) {
                AddProductToCart(db);
            }
        }, new ICallBackAction() {
            @Override
            public void CallBack(Object... obj) {
                if (obj==null||obj.length<0)return;
              UpDateToCart(db,(Cart)obj[0]);
            }
        });

    }
    private void UpDateToCart(FirebaseFirestore db, Cart oldCart) {
        oldCart.setQuantity(oldCart.getQuantity()+quantity); ;
        db.collection(Tag.DTO_CART)
                .document(oldCart.getId())
                .set(oldCart)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            Toast.makeText(ActivityProductDetail.this, R.string.update_Cart, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ActivityProductDetail.this, R.string.error, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(Tag.TAG_LOG, "onFailure: ",e );
                    }
                });
    }
    private void AddProductToCart(FirebaseFirestore db) {
        String id = UUID.randomUUID().toString();
        String idUser = FirebaseAuth.getInstance().getUid();
        db.collection(Tag.DTO_CART).document(id).set(new Cart(
                id,idUser,product.getId(),size,quantity
        )).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Toast.makeText(ActivityProductDetail.this, R.string.CAddCart, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ActivityProductDetail.this, R.string.error, Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(Tag.TAG_LOG, "onFailure: ",e );
            }
        });
    }
    private void CheckProductInCart(ICallBackAction noThing, ICallBackAction haveProduct) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String idUser = FirebaseAuth.getInstance().getUid();
        db.collection(Tag.DTO_CART)
                .whereEqualTo("idUser",idUser)
                .whereEqualTo("idProducts",product.getId())
                .whereEqualTo("size",size)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().isEmpty())noThing.CallBack();
                        else {
                            List<Cart> oldCart = new ArrayList<>();
                            oldCart.addAll(task.getResult().toObjects(Cart.class));
                            haveProduct.CallBack(oldCart.get(0));
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(Tag.TAG_LOG, "onFailure: ",e );
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