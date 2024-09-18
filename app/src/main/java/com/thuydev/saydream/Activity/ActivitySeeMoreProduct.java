package com.thuydev.saydream.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.thuydev.saydream.Adapter.ShopProductAdapter;
import com.thuydev.saydream.DTO.Product;
import com.thuydev.saydream.Extentions.Tag;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.R;
import com.thuydev.saydream.databinding.ActivityShowMoreBinding;

import java.util.ArrayList;
import java.util.List;

public class ActivitySeeMoreProduct extends AppCompatActivity {
    ActivityShowMoreBinding view;
    List<Product> list;
    ShopProductAdapter shopProductAdapter;
    String idCate = "";
    String nameCater = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivityShowMoreBinding.inflate(getLayoutInflater(),null,false);
        setContentView(view.getRoot());
        list = new ArrayList<>();
        shopProductAdapter = new ShopProductAdapter(this, list, new ICallBackAction() {
            @Override
            public void CallBack(Object... obj) {

            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(this,2, LinearLayoutManager.VERTICAL,false);
        view.rcvListSanPhamMore.setLayoutManager(layoutManager);
        view.rcvListSanPhamMore.setAdapter(shopProductAdapter);
        GetProducts();
        view.tvTenHangShow.setText(nameCater);
        view.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        view.seachSpKH.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                shopProductAdapter.getFilter().filter(newText);
                shopProductAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void GetProducts() {
        Intent intent = getIntent();
        idCate = intent.getStringExtra(Tag.ID_CATEGORY);
        nameCater = intent.getStringExtra(Tag.NAME_CATEGORY);
        view.tvTenHangShow.setText(nameCater);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(Tag.DTO_PRODUCT).whereEqualTo("idCategory",idCate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult().isEmpty()){
                    Toast.makeText(ActivitySeeMoreProduct.this, R.string.error, Toast.LENGTH_SHORT).show();
                    GetProducts();
                    return;
                }
                list.clear();
                list.addAll(task.getResult().toObjects(Product.class));
                shopProductAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(Tag.TAG_LOG, "onFailure: ",e );
            }
        });
    }
}