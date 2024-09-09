package com.thuydev.saydream.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thuydev.saydream.DTO.User;
import com.thuydev.saydream.Extentions.ActivityExtentions;
import com.thuydev.saydream.Extentions.Regex;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.R;
import com.thuydev.saydream.databinding.ActivityDangKyBinding;

public class ActivitySignUp extends AppCompatActivity {
    ActivityDangKyBinding view;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivityDangKyBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        progressDialog = new ProgressDialog(this);
        view.btnDangkyOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });
    }
    private void SignUp() {
        String email = view.edtEmailDangnky.getText().toString();
        String password = view.edtMatkhauDangky.getText().toString();
        String confirmPassword = view.edtRematkhauDangky.getText().toString();
        ShowProgressDialog();
        if(email.isEmpty()||password.isEmpty()||confirmPassword.isEmpty()){
            Toast.makeText(this, R.string.IsEmpty, Toast.LENGTH_SHORT).show();
            HideProgressDialog();
            return;
        }
        if (Regex.CheckRegex(Regex.CheckEmail,email)){
            Toast.makeText(this, R.string.WrongEmailFomat, Toast.LENGTH_SHORT).show();
            HideProgressDialog();
            return;
        }
        if (!password.equals(confirmPassword)){
            Toast.makeText(this, R.string.WrongPasswordConfirm, Toast.LENGTH_SHORT).show();
            HideProgressDialog();
            return;
        }
        if (password.length()<6){
            Toast.makeText(this, R.string.WrongPasswordSize, Toast.LENGTH_SHORT).show();
            HideProgressDialog();
            return;
        }
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()){
                            CreateAccount();
                        }else {
                            Toast.makeText(ActivitySignUp.this, R.string.FailSignUp, Toast.LENGTH_SHORT).show();
                            HideProgressDialog();
                        }
                    }
                });
    }
    private void CreateAccount(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (user==null)return;
        User n_user = new User(user.getUid(),user.getEmail(),"",1,0L);
        db.collection("User").document(user.getUid()).set(n_user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        ActivityExtentions.NextActivity(ActivitySignUp.this, ActivityMain.class, new ICallBackAction() {
                            @Override
                            public void CallBack(Object... obj) {
                                finish();
                            }
                        });
                        Toast.makeText(ActivitySignUp.this, R.string.SuccessSignUp, Toast.LENGTH_SHORT).show();
                        HideProgressDialog();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "onFailure: ",e );
                        Toast.makeText(ActivitySignUp.this, e.toString(), Toast.LENGTH_SHORT).show();
                        HideProgressDialog();
                    }
                });
    }
    public void ShowProgressDialog() {
        progressDialog.setTitle("Loading");
        progressDialog.setMessage(getString(R.string.MessageLoading));
        progressDialog.show();
    }
    public void HideProgressDialog(){progressDialog.cancel();}
}