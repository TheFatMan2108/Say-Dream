package com.thuydev.saydream.Extentions;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.bumptech.glide.Glide;
import com.thuydev.saydream.Activity.ActivityMain;
import com.thuydev.saydream.DTO.User;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.R;
import com.thuydev.saydream.databinding.DialogAnhBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivityExtentions {
    public static void NextActivity(Activity oldActivity, Class aClass, ICallBackAction action) {
       Intent intent = new Intent(oldActivity, aClass);
        oldActivity.startActivity(intent);
        action.CallBack();
    }
    public static void Login(Activity oldActivity, User user,ICallBackAction action) {
        Intent intent = new Intent(oldActivity, ActivityMain.class);
        action.CallBack();
        if (!oldActivity.isFinishing()) {
            return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        oldActivity.startActivity(intent);
    }
    public static void ShowQR(String anh,Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        DialogAnhBinding view = DialogAnhBinding.inflate(activity.getLayoutInflater());
        builder.setView(view.getRoot());
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Glide.with(activity).load(anh)
                .error(R.drawable.baseline_crop_original_24).into(view.imvAnhGg);
    }
    @SuppressLint("DefaultLocale")
    public  static String  GetTime(){
        return String.format("%02d/%02d/%02d", Calendar.getInstance().get(Calendar.DAY_OF_MONTH), (Calendar.getInstance().get(Calendar.MONTH)+1), Calendar.getInstance().get(Calendar.YEAR)) +"/ - "+
                String.format("%02d:%02d:%02d",new Date().getHours(),new Date().getMinutes(),new Date().getSeconds());

    }
    public static String getDate () {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(now);
    }
}
