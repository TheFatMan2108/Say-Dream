package com.thuydev.saydream.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.thuydev.saydream.Adapter.ShopCateAdapter;
import com.thuydev.saydream.DTO.Categoty;
import com.thuydev.saydream.Extentions.Tag;
import com.thuydev.saydream.databinding.FragmentCuahangBinding;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Shop extends Fragment {
    FragmentCuahangBinding viewLayout;
    List<Categoty> listCate;
    ShopCateAdapter shopCateAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewLayout = FragmentCuahangBinding.inflate(getLayoutInflater());
        Log.e(Tag.TAG_LOG, "onCreateView: "+(viewLayout ==null) );
        return viewLayout.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(Tag.TAG_LOG, "onCreateView: "+1 );
        listCate = new ArrayList<>();
        shopCateAdapter = new ShopCateAdapter(getContext(),listCate);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        viewLayout.rcvCuaHang.setAdapter(shopCateAdapter);
        viewLayout.rcvCuaHang.setLayoutManager(layoutManager);
        GetListCate();
    }

    private void GetListCate() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(Tag.DTO_CATEGORY).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                listCate.clear();
                listCate.addAll(task.getResult().toObjects(Categoty.class));
                shopCateAdapter.notifyDataSetChanged();
                Log.e(Tag.TAG_LOG, "onComplete: "+listCate.size());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(Tag.TAG_LOG, "onComplete: "+"Loi");
            }
        });
    }


}
