package com.thuydev.saydream.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.thuydev.saydream.Adapter.CategoryAdapter;
import com.thuydev.saydream.Adapter.PhotoAdapter;
import com.thuydev.saydream.Model.Category;
import com.thuydev.saydream.Model.Photo;
import com.thuydev.saydream.Model.Shoe;
import com.thuydev.saydream.R;
import com.thuydev.saydream.databinding.FragmentFrgHomeBinding;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class Fragment_frg_Home extends Fragment {

    private FragmentFrgHomeBinding binding;
    private List<Photo> mListPhoto;
    private CategoryAdapter categoryAdapter;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = binding.viewPager2.getCurrentItem();
            if (currentPosition == mListPhoto.size() - 1) {
                binding.viewPager2.setCurrentItem(0);
            } else {
                binding.viewPager2.setCurrentItem(currentPosition + 1);
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFrgHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup Toolbar (nếu có cần thiết để xử lý sự kiện)
        setupToolbar();

        // Setup ViewPager2 cho ảnh
        mListPhoto = getListPhoto();
        PhotoAdapter photoAdapter = new PhotoAdapter(requireActivity(), mListPhoto);
        binding.viewPager2.setAdapter(photoAdapter);
        binding.circleIndicator3.setViewPager(binding.viewPager2);

        // Tự động chuyển ảnh
        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, 3000);
            }
        });

        // Setup RecyclerView cho danh mục
        categoryAdapter = new CategoryAdapter(requireContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        binding.rcvCategory.setLayoutManager(linearLayoutManager);

        categoryAdapter.setData(getListCategory());
        binding.rcvCategory.setAdapter(categoryAdapter);
    }

    private void setupToolbar() {
        // Ở đây bạn có thể thêm xử lý sự kiện cho Toolbar nếu cần
    }

    private List<Photo> getListPhoto() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.logo1));
        list.add(new Photo(R.drawable.logo2));
        list.add(new Photo(R.drawable.logo3));
        list.add(new Photo(R.drawable.logo4));
        list.add(new Photo(R.drawable.logo3));
        list.add(new Photo(R.drawable.logo2));



        return list;
    }

    private List<Category> getListCategory() {
        List<Category> listCategory = new ArrayList<>();
        List<Shoe> listShoe = new ArrayList<>();
        listShoe.add(new Shoe(R.drawable.logo1, "Mới", "1.000.000 VNĐ", "Giày AF1"));
        listShoe.add(new Shoe(R.drawable.logo2, "Mới", "1.000.000 VNĐ", "Giày AF1"));
        listShoe.add(new Shoe(R.drawable.logo3, "Cũ", "500.000 VNĐ", "Giày AF1"));
        listShoe.add(new Shoe(R.drawable.logo4, "Cũ", "1.000.000 VNĐ", "Giày AF1"));
        listCategory.add(new Category("Sản phẩm mới", listShoe));
        return listCategory;
    }

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
