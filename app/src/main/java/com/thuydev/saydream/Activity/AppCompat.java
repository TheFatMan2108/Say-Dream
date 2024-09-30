package com.thuydev.saydream.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.thuydev.saydream.Extentions.ActivityExtentions;
import com.thuydev.saydream.R;

public class AppCompat extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String langCode = sharedPreferences.getString("lang", "vi");
        ActivityExtentions.SetLocal(this,langCode);
    }
}