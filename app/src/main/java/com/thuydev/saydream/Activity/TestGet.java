package com.thuydev.saydream.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.thuydev.saydream.R;
import com.thuydev.saydream.databinding.ActivityTestGetBinding;

public class TestGet extends AppCompatActivity {
ActivityTestGetBinding view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        view = ActivityTestGetBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        GetData();
    }

    private void GetData() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            Log.e("TAG", "onSuccess: "+deepLink.getQueryParameter("pass") );
                            view.tvLinktest.setText(deepLink.getQueryParameter("email"));
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "getDynamicLink:onFailure", e);
                    }
                });
        Log.w("TAG", "getDynamicLink:onFailure"+"da chay");
    }
}