package com.thuydev.saydream.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thuydev.saydream.Extentions.ActivityExtentions;
import com.thuydev.saydream.Extentions.FirebaseExtention;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.R;
import com.thuydev.saydream.databinding.ActivityDangNhapBinding;
import com.thuydev.saydream.databinding.DialogQuenpassBinding;

public class ActivityLogin extends AppCompat {
    private ActivityDangNhapBinding view;
    private ProgressDialog progressDialog;
    public FirebaseAuth mAuth ;
    public FirebaseUser mUser;
    public FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivityDangNhapBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        view.btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        view.btnDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityExtentions.NextActivity(ActivityLogin.this, ActivitySignUp.class, new ICallBackAction() {
                    @Override
                    public void CallBack(Object... obj) {

                    }
                });
            }
        });
        view.tvQuenpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPassword();
            }
        });
    }

    private void ForgotPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogQuenpassBinding view = DialogQuenpassBinding.inflate(getLayoutInflater());
        builder.setView(view.getRoot());
        Dialog dialog = builder.create();
        builder.show();
        view.btnQuen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = view.edtEmailQuen.getText().toString();
                ShowProgressDialog();
                if (email.isEmpty()){
                    Toast.makeText(ActivityLogin.this, R.string.IsEmpty, Toast.LENGTH_SHORT).show();
                    HideProgressDialog();
                    return;
                }
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()){
                                    dialog.cancel();
                                    HideProgressDialog();
                                    Toast.makeText(ActivityLogin.this, R.string.SentToEmail, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
    private void Login() {
        String email = view.edtEmailDangnhap.getText().toString();
        String password = view.edtMatkhauDangnhap.getText().toString();

        if(email.isEmpty()||password.isEmpty()){
            Toast.makeText(this, R.string.IsEmpty, Toast.LENGTH_SHORT).show();
            return;
        }
        ShowProgressDialog();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()){
                            mUser = FirebaseAuth.getInstance().getCurrentUser();
                            if(mUser==null){
                                Toast.makeText(ActivityLogin.this, R.string.AccountNull, Toast.LENGTH_SHORT).show();
                                HideProgressDialog();
                                return;
                            }
                            FirebaseExtention.CheckBanAccount(mUser, ActivityLogin.this, db, new ICallBackAction() {
                                @Override
                                public void CallBack(Object... obj) {

                                }
                            });
                            return;
                        }
                        Toast.makeText(ActivityLogin.this, R.string.LoginFail, Toast.LENGTH_SHORT).show();
                        HideProgressDialog();
                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        HideProgressDialog();
                    }
                });

    }
    private void ShowProgressDialog() {
        progressDialog.setTitle("Loading");
        progressDialog.setMessage(getString(R.string.MessageLoading));
        progressDialog.show();
    }
    private void HideProgressDialog(){progressDialog.cancel();}
    public void ReloadUser(){
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }
}