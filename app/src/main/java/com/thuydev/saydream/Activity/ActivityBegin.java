package com.thuydev.saydream.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.thuydev.saydream.Extentions.ActivityExtentions;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.databinding.ActivityManHinhKhoiDauBinding;

public class ActivityBegin extends AppCompatActivity {
    ActivityManHinhKhoiDauBinding view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivityManHinhKhoiDauBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        view.btnDangNhapBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityExtentions.NextActivity(ActivityBegin.this, ActivityLogin.class, new ICallBackAction() {
                    @Override
                    public void CallBack(Object... obj) {
                        finish();
                    }

                    
                });
            }
        });
        view.tvDangkyBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityExtentions.NextActivity(ActivityBegin.this, ActivitySignUp.class, new ICallBackAction() {
                    @Override
                    public void CallBack(Object... obj) {
                        finish();
                    }
                });
            }
        });
    }

}