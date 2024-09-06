package com.thuydev.saydream.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.thuydev.saydream.R;
import com.thuydev.saydream.databinding.ActivityDangKyBinding;

public class MainActivity extends AppCompatActivity {
    ActivityDangKyBinding view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        view = ActivityDangKyBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
    }
}