package com.thuydev.saydream.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.thuydev.saydream.Extentions.ActivityExtentions;
import com.thuydev.saydream.Fragment.Fragment_Bill;
import com.thuydev.saydream.Fragment.Fragment_Shop;
import com.thuydev.saydream.Fragment.Fragment_frg_Home;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.R;
import com.thuydev.saydream.databinding.ActivityManHinhKhachHangBinding;

public class ActivityMain extends AppCompat {
    ActivityManHinhKhachHangBinding view;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivityManHinhKhachHangBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        CreatedNavi();
        fragmentTransaction.add(R.id.fcv_KhachHang, new Fragment_frg_Home());
        fragmentTransaction.commit();
    }

    private void ChangeFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(view.fcvKhachHang.getId(), fragment).commit();
    }

    private void CreatedNavi() {
        view.bnvKhachhang.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_khachhang_danhsachsp) {

                    ChangeFragment(new Fragment_Shop());
                } else if (item.getItemId() == R.id.menu_khachhang_Home) {
                    ChangeFragment(new Fragment_frg_Home());
                } else if (item.getItemId() == R.id.menu_khachhang_hoadon) {
                    ChangeFragment(new Fragment_Bill());
                } else if (item.getItemId() == R.id.menu_khachhang_thongtincanhan) {
                    ActivityExtentions.NextActivity(ActivityMain.this, ActivityProfile.class, new ICallBackAction() {
                        @Override
                        public void CallBack(Object... obj) {

                        }
                    });
                    return false;
                } else {
                    Toast.makeText(ActivityMain.this, "Lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }
}