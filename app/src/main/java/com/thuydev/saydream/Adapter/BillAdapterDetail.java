package com.thuydev.saydream.Adapter;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.thuydev.saydream.DTO.Cart;
import com.thuydev.saydream.DTO.Product;
import com.thuydev.saydream.Extentions.FomatExtention;
import com.thuydev.saydream.Extentions.Tag;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.R;
import com.thuydev.saydream.databinding.ItemListSanphamMuaBinding;

import java.util.ArrayList;
import java.util.List;

public class BillAdapterDetail extends BaseAdapter {
    Context context;
    List<Cart> listCart;
    List<Product> listProduct;

    public BillAdapterDetail(List<Cart> listCart, Context context) {
        this.context = context;
        this.listCart = listCart;
        listProduct = new ArrayList<>();
        loadListProduct();
    }
    @Override
    public int getCount() {
        return listCart.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ItemListSanphamMuaBinding viewdata = ItemListSanphamMuaBinding.inflate(((Activity) context).getLayoutInflater(), viewGroup, false);
        Cart cart = listCart.get(i);
        loadListProduct();
        for (Product p : listProduct) {
            if (cart.getIdProducts().equals(p.getId())) {

                Glide.with(context).load(p.getImage()).into(viewdata.imvAnhSpCt);
                String tensp = context.getString(R.string.nameProduct);
                String giasp = context.getString(R.string.priceProduct);
                String soluong = context.getString(R.string.quantity);
                String size = context.getString(R.string.size);


                viewdata.tvMaspCt.setVisibility(View.GONE);
                viewdata.tvTenspCt.setText(String.format("%s: %s", tensp, p.getName()));
                viewdata.tvGiaCt.setText(String.format("%s: %s", giasp, FomatExtention.MakeStyleMoney(p.getPrice() * cart.getQuantity())));
                viewdata.tvSoluongCt.setText(String.format("%s: %s", soluong, cart.getQuantity()));
                viewdata.tvSoluongTrongKhoCt.setText(String.format("%s: %s", size, cart.getSize()));

            }
        }
        return viewdata.getRoot();
    }
    private void loadListProduct() {
        getListproduct(new ICallBackAction() {
            @Override
            public void CallBack(Object... obj) {
                listProduct.clear();
                listProduct.addAll((List<Product>) obj[0]);
notifyDataSetChanged();
            }
        });
    }

    private void getListproduct(ICallBackAction action) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Tag.DTO_PRODUCT).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    action.CallBack(task.getResult().toObjects(Product.class));
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
