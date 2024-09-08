package com.thuydev.saydream.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thuydev.saydream.Extentions.FirebaseExtention;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.databinding.ActivityManHinhChaoBinding;

public class ActivityWellcome extends AppCompatActivity {
    ActivityManHinhChaoBinding view;
    private int beginApp;
    private SharedPreferences sharedPreferences;
    private Intent intent;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        view = ActivityManHinhChaoBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("begin", MODE_PRIVATE);
        beginApp = sharedPreferences.getInt("only", 0);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //viet chuyen man o day
                if (beginApp == 0) {
                    intent = new Intent(ActivityWellcome.this, ActivityBegin.class);
                    startActivity(intent);
                    ChangeValue();
                    finish();
                } else {
                    StartActivityLogin();
                }
            }
        }, 3000);
    }
    private void ChangeValue() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("only", 1);
        editor.apply();
    }
    private void StartActivityLogin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            intent = new Intent(this, ActivityLogin.class);
            startActivity(intent);
        } else {
            FirebaseExtention.CheckBanAccount(user, ActivityWellcome.this, db, new ICallBackAction() {
                @Override
                public void Callback() {
                    finish();
                    finishAffinity();
                }
            });
        }
    }
}
