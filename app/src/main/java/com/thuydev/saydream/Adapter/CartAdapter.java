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
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thuydev.saydream.Activity.ActivityCart;
import com.thuydev.saydream.DTO.Cart;
import com.thuydev.saydream.DTO.Categoty;
import com.thuydev.saydream.DTO.Product;
import com.thuydev.saydream.Extentions.ActivityExtentions;
import com.thuydev.saydream.Extentions.FirebaseExtention;
import com.thuydev.saydream.Extentions.FomatExtention;
import com.thuydev.saydream.Extentions.Tag;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.R;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Cart> listCart;
    private Context context;
    private ICallBackAction callBackAction, callBackTotal, callBackDelete;
    private LayoutInflater inflater;
    private ProgressDialog progressDialog;


    public CartAdapter(List<Cart> listCart, Context context, ICallBackAction callBackAction, ICallBackAction callBackTotal, LayoutInflater inflater) {
        this.listCart = listCart;
        this.context = context;
        this.callBackAction = callBackAction;
        this.callBackTotal = callBackTotal;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_giohang, parent, false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        Cart cart = listCart.get(position);
        progressDialog = new ProgressDialog(context);


        FirebaseExtention.GetProduct(cart.getIdProducts(), new ICallBackAction() {
            @Override
            public void CallBack(Object... obj) {
                Product product = (Product) obj[0];

                FirebaseExtention.GetCategory(product.getIdCategory(), new ICallBackAction() {
                    @Override
                    public void CallBack(Object... obj) {
                        Categoty categoty;
                        categoty = (Categoty) obj[0];
                        holder.tv_TenSP_GioHang.setText(product.getName());
                        Glide.with(context)
                                .load(product.getImage())
                                .into(holder.imv_AnhSP_GioHang);
                        String sizeText = context.getString(R.string.size) + ": " + cart.getSize();
                        holder.tv_KichCoSP_GioHang.setText(sizeText);

                        String quantityText = context.getString(R.string.quantity) + ": " + cart.getQuantity();
                        holder.tv_SoLuongSP_GioHang.setText(quantityText);
                        Log.e(TAG, "product: "+product.getPrice() );
                        Log.e(TAG, "cart: "+cart.getQuantity() );
                        Long price = product.getPrice() * cart.getQuantity();
                        holder.price = price;
                        holder.tv_GiaSP_GioHang.setText(FomatExtention.MakeStyleMoney(price));
                        String categoryText = context.getString(R.string.category) + ": " + categoty.getName();
                        holder.tv_ThuongHieu_GioHang.setText(categoryText);

                        callBackTotal.CallBack(price);


                    }
                });
            }
        });
        holder.tv_xoa_giohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle(R.string.deleteCart)
                        .setMessage(R.string.deleteCartConfirm)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Hành động khi bấm OK
                                deleteItemCart(cart.getId(), callBackAction);

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Hành động khi bấm Cancel
                                dialog.dismiss();
                            }
                        })
                        .show();


            }
        });
        holder.tv_mua_giohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle(R.string.deleteCart)
                        .setMessage(R.string.paymentCart)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                payment(cart, holder.price, new ICallBackAction() {
                                    @Override
                                    public void CallBack(Object... obj) {
                                        String url = obj[0].toString();
                                        ActivityExtentions.ShowQR(url,(Activity) context);
                                        deleteItemWhenPayment(cart.getId(), callBackAction);
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
    }

    private void deleteItemCart(String idCart, ICallBackAction iCallBackAction) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Tag.DTO_CART).document(idCart).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        new AlertDialog.Builder(context)
                                .setTitle(R.string.deleteCart)
                                .setMessage(R.string.deleteCartSuccess)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Hành động khi bấm OK
                                        dialog.dismiss(); // Đóng dialog

                                        iCallBackAction.CallBack();
                                    }
                                })
                                .show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        new AlertDialog.Builder(context)
                                .setTitle(R.string.deleteCart)
                                .setMessage(R.string.deleteCartFail)
                                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
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

    private void deleteItemWhenPayment(String idCart, ICallBackAction iCallBackAction) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Tag.DTO_CART).document(idCart).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        iCallBackAction.CallBack();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void payment(Cart cart, Long price, ICallBackAction callBackAction) {
        ShowProgressDialog();
        Map<String, Object> data = new HashMap<>();
        List<Cart> nList = new ArrayList<>();
        nList.add(cart);
        String id = UUID.randomUUID().toString();
        String idUser = FirebaseAuth.getInstance().getUid();
        data.put("id", id);
        data.put("totalPrice", price);
        data.put("listSP", nList);
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
                        @SuppressLint("DefaultLocale") String formattedUrl = String.format(urlTemplate, price, id);
                        callBackAction.CallBack(formattedUrl);
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        new AlertDialog.Builder(context)
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

    private void showQR(String anh) {
        View view = View.inflate(context, R.layout.dialog_anh, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imageView = view.findViewById(R.id.imv_anh_gg);

//        imageView.setImageDrawable(anh.getDrawable());

        Glide.with(context).load(anh)
                .error(R.drawable.baseline_crop_original_24).into(imageView);
    }

    private void ShowProgressDialog() {
        progressDialog.setTitle("Loading");
        progressDialog.setMessage(context.getString(R.string.MessageLoading));
        progressDialog.show();
    }

    @Override
    public int getItemCount() {
        return listCart.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tv_TenSP_GioHang;
        TextView tv_ThuongHieu_GioHang;
        TextView tv_KichCoSP_GioHang;
        TextView tv_SoLuongSP_GioHang;
        TextView tv_GiaSP_GioHang;
        ImageView imv_AnhSP_GioHang;
        TextView tv_xoa_giohang;
        TextView tv_mua_giohang;
        Long price;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_TenSP_GioHang = itemView.findViewById(R.id.tv_tensp_gioHang);
            tv_ThuongHieu_GioHang = itemView.findViewById(R.id.tv_thuonghieu_gioHang);
            tv_KichCoSP_GioHang = itemView.findViewById(R.id.tv_kichcosp_giohang);
            tv_SoLuongSP_GioHang = itemView.findViewById(R.id.tv_soluongsp_giohang);
            tv_GiaSP_GioHang = itemView.findViewById(R.id.tv_giasp_giohang);
            imv_AnhSP_GioHang = itemView.findViewById(R.id.imv_anh_sp_gioHang);
            tv_xoa_giohang = itemView.findViewById(R.id.tv_xoa_giohang);
            tv_mua_giohang = itemView.findViewById(R.id.tv_mua_giohang);

        }
    }
}
