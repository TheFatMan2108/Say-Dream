package com.thuydev.saydream.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.thuydev.saydream.Activity.ActivityCart;
import com.thuydev.saydream.Adapter.ShopCateAdapter;
import com.thuydev.saydream.Adapter.PhotoAdapter;  // Thêm PhotoAdapter
import com.thuydev.saydream.DTO.Categoty;
import com.thuydev.saydream.DTO.Photo;  // Thêm model Photo
import com.thuydev.saydream.Extentions.Tag;
import com.thuydev.saydream.databinding.FragmentFrgHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class Fragment_frg_Home extends Fragment {

    FragmentFrgHomeBinding binding;
    List<Categoty> listCate;
    ShopCateAdapter shopCateAdapter;

    // Danh sách ảnh cho slider
    List<Photo> mListPhoto;
    PhotoAdapter photoAdapter;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentFrgHomeBinding.inflate(getLayoutInflater());
        Log.e(Tag.TAG_LOG, "onCreateView: "+(binding == null));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Thiết lập slider ảnh
        mListPhoto = new ArrayList<>();
        photoAdapter = new PhotoAdapter(getContext(), mListPhoto);
        binding.viewPager2.setAdapter(photoAdapter);
        binding.circleIndicator3.setViewPager(binding.viewPager2);

        // Lấy ảnh từ Firebase
        getListPhotoFromFirebase();

        // Thiết lập tự động chuyển ảnh
        mHandler.postDelayed(mRunnable, 3000);

        // Thiết lập danh sách sản phẩm
        listCate = new ArrayList<>();
        shopCateAdapter = new ShopCateAdapter(getContext(), listCate);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rcvCuaHang.setAdapter(shopCateAdapter);
        binding.rcvCuaHang.setLayoutManager(layoutManager);
        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityCart.class);
                startActivity(intent);
            }
        });

        GetListCate();

        binding.svTimsp.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                shopCateAdapter.getFilter().filter(newText);
                shopCateAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    // Phương thức lấy ảnh từ Firebase Firestore (collection Product)
    private void getListPhotoFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Product")  // Collection "Product"
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mListPhoto.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String imageUrl = document.getString("image");  // Lấy trường "image" từ document
                                if (imageUrl != null) {
                                    mListPhoto.add(new Photo(imageUrl));  // Thêm vào danh sách ảnh
                                }
                            }
                            photoAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("Firebase", "Error getting products: ", task.getException());
                        }
                    }
                });
    }

    // Phương thức lấy danh sách danh mục sản phẩm
    private void GetListCate() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Tag.DTO_CATEGORY).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                listCate.clear();
                listCate.addAll(task.getResult().toObjects(Categoty.class));
                shopCateAdapter.notifyDataSetChanged();
                Log.e(Tag.TAG_LOG, "onComplete: " + listCate.size());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(Tag.TAG_LOG, "onComplete: " + "Lỗi");
            }
        });
    }

    // Runnable cho slider ảnh
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = binding.viewPager2.getCurrentItem();
            if (currentPosition == mListPhoto.size() - 1) {
                binding.viewPager2.setCurrentItem(0);
            } else {
                binding.viewPager2.setCurrentItem(currentPosition + 1);
            }
            mHandler.postDelayed(this, 3000);  // Chuyển ảnh mỗi 3 giây
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, 3000);
    }
}
