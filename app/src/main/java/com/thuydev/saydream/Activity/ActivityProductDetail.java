package com.thuydev.saydream.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thuydev.saydream.DTO.Product;
import com.thuydev.saydream.Extentions.Tag;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.R;
import com.thuydev.saydream.databinding.ActivitySanphamShowBinding;

public class ActivityProductDetail extends AppCompatActivity {
    ActivitySanphamShowBinding view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivitySanphamShowBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        GetData(new ICallBackAction() {
            @Override
            public void CallBack(Object... obj) {
                // Do some thing
                String id = obj[0].toString();
                GetProduct(id, new ICallBackAction() {
                    @Override
                    public void CallBack(Object... obj) {
                        Product product = (Product) obj[0];
                        view.tvTenspShow.setText(product.getName());
                    }
                });
            }
        });
    }
    private void GetProduct(String idProduct,ICallBackAction action){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(Tag.DTO_PRODUCT).document(idProduct).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                action.CallBack(task.getResult().toObject(Product.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(Tag.TAG_LOG, "onFailure: ",e );
            }
        });
    }
    private void GetData(ICallBackAction action) {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                           String idProduct= deepLink.getQueryParameter("pass");
                            Log.e(Tag.TAG_LOG, "onSuccess: "+idProduct );
                            action.CallBack(idProduct);
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Tag.TAG_LOG, "getDynamicLink:onFailure", e);
                    }
                });
    }
}