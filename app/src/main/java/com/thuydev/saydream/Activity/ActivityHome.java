package com.thuydev.saydream.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.thuydev.saydream.Adapter.CategoryAdapter;
import com.thuydev.saydream.Adapter.PhotoAdapter;
import com.thuydev.saydream.Category.Category;
import com.thuydev.saydream.Model.Photo;
import com.thuydev.saydream.Model.Shoe;
import com.thuydev.saydream.R;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class ActivityHome extends AppCompatActivity {
    private ViewPager2 mViewPager2;
    private CircleIndicator3 mCircleIndicator3;
    private List<Photo> mListPhoto;
    private RecyclerView rcvCategory;
    private CategoryAdapter categoryAdapter;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = mViewPager2.getCurrentItem();
            if (currentPosition == mListPhoto.size() - 1){
                mViewPager2.setCurrentItem(0);
            }else {
                mViewPager2.setCurrentItem(currentPosition + 1);
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mViewPager2 = findViewById(R.id.view_pager_2);
        mCircleIndicator3 = findViewById(R.id.circle_indicator_3);
        rcvCategory = findViewById(R.id.rcv_category);

        mListPhoto = getListPhoto();
        PhotoAdapter photoAdapter = new PhotoAdapter(this,mListPhoto);
        mViewPager2.setAdapter(photoAdapter);
        mCircleIndicator3.setViewPager(mViewPager2);

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable,3000);
            }
        });

        categoryAdapter = new CategoryAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rcvCategory.setLayoutManager(linearLayoutManager);

        categoryAdapter.setData(getListCategory());
        rcvCategory.setAdapter(categoryAdapter);
    }

    private List<Photo> getListPhoto(){
        List<Photo> list = new ArrayList<>();

        list.add(new Photo(R.drawable.logo1));
        list.add(new Photo(R.drawable.logo2));
        list.add(new Photo(R.drawable.logo3));
        list.add(new Photo(R.drawable.logo4));

        return list;
    }

    private List<Category> getListCategory(){
        List<Category> listCategory = new ArrayList<>();

        List<Shoe> listShoe = new ArrayList<>();
        listShoe.add(new Shoe(R.drawable.logo1, "Mới","1.000.000 VNĐ", "Giày AF1"));
        listShoe.add(new Shoe(R.drawable.logo2, "Mới","1.000.000 VNĐ", "Giày AF1"));
        listShoe.add(new Shoe(R.drawable.logo3, "Cũ","500.000 VNĐ", "Giày AF1"));
        listShoe.add(new Shoe(R.drawable.logo4, "Cũ","1.000.000 VNĐ", "Giày AF1"));

        listShoe.add(new Shoe(R.drawable.logo1, "Mới","1.000.000 VNĐ", "Giày AF1"));
        listShoe.add(new Shoe(R.drawable.logo2, "Mới","1.000.000 VNĐ", "Giày AF1"));
        listShoe.add(new Shoe(R.drawable.logo3, "Cũ","500.000 VNĐ", "Giày AF1"));
        listShoe.add(new Shoe(R.drawable.logo4, "Cũ","1.000.000 VNĐ", "Giày AF1"));

        listCategory.add(new Category("Adidas", listShoe));

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
        mHandler.postDelayed(mRunnable,3000);
    }
}