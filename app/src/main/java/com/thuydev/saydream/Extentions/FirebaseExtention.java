package com.thuydev.saydream.Extentions;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
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
                        public void Callback() {
                            // Do some things
                        }
                    });
                } else {
                    Toast.makeText(oldActivity, "Tài khoản bạn đã bị đình chỉ vui lòng liên", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    action.Callback();
                }
            }
        });
    }
}
