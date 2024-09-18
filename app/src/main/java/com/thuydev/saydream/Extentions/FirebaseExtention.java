package com.thuydev.saydream.Extentions;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.thuydev.saydream.Activity.ActivityMain;
import com.thuydev.saydream.DTO.Product;
import com.thuydev.saydream.DTO.User;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.R;

public class FirebaseExtention {
    public static void CheckBanAccount(FirebaseUser user, Activity oldActivity, FirebaseFirestore db, ICallBackAction action) {
        if (user==null){
            Toast.makeText(oldActivity, R.string.AccountNull, Toast.LENGTH_SHORT).show();
            return;
        }
        db.collection("User").whereEqualTo("id", user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isComplete()) {
                    Toast.makeText(oldActivity, "Lỗi", Toast.LENGTH_SHORT).show();
                    return;
                }
                User n_user = task.getResult().toObjects(User.class).get(0);
                Log.e("TAG", "onComplete: "+n_user );
                if (n_user.getStatus() == 1) {
                    ActivityExtentions.Login(oldActivity, n_user, new ICallBackAction() {
                        @Override
                        public void CallBack(Object... obj) {
                            ActivityExtentions.NextActivity(oldActivity, ActivityMain.class, new ICallBackAction() {
                                @Override
                                public void CallBack(Object... obj) {

                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(oldActivity, "Tài khoản bạn đã bị đình chỉ vui lòng liên", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    action.CallBack();
                }
            }
        });
    }
    public static void CreatedDeepLinkProduct(Product data,Activity oldActivity, ICallBackAction action){
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://saydream.page.link/product/?id="+data.getId()))
                .setDomainUriPrefix("https://saydream.page.link/")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();
        Uri dynamicLinkUri = dynamicLink.getUri();
        Log.e(Tag.TAG_LOG, "CreatedDeepLinkProduct: "+dynamicLinkUri );
        MakeShortLink(dynamicLinkUri.toString(), oldActivity, new ICallBackAction() {
            @Override
            public void CallBack(Object... obj) {
                action.CallBack(obj);
            }
        });
    }
    public static void MakeShortLink(String link, Activity oldActivity,ICallBackAction action){
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(link))
                .buildShortDynamicLink()
                .addOnCompleteListener(oldActivity, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            action.CallBack(shortLink);
                        } else {
                            Log.e("TAG", "onComplete: Loi" );
                            Toast.makeText(oldActivity, R.string.error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public static void GetProduct(String idProduct,ICallBackAction action){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Tag.DTO_PRODUCT).document(idProduct).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.e(Tag.TAG_LOG, "onComplete: "+(task.getResult()==null) );
                if (task.getResult()==null){{
                    GetProduct(idProduct,action);
                    return;
                }}
                action.CallBack(task.getResult().toObject(Product.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(Tag.TAG_LOG, "onFailure: ",e );
            }
        });
    }
}
