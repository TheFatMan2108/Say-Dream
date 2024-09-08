package com.thuydev.saydream.Extentions;


import android.app.Activity;
import android.content.Intent;
import com.thuydev.saydream.Activity.ActivityMain;
import com.thuydev.saydream.DTO.User;
import com.thuydev.saydream.Interface.ICallBackAction;

public class ActivityExtentions {
    public static void NextActivity(Activity oldActivity, Class aClass, ICallBackAction action) {
       Intent intent = new Intent(oldActivity, aClass);
        oldActivity.startActivity(intent);
        action.Callback();
    }
    public static void Login(Activity oldActivity, User user,ICallBackAction action) {
        Intent intent = new Intent(oldActivity, ActivityMain.class);
        action.Callback();
        if (!oldActivity.isFinishing()) {
            return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        oldActivity.startActivity(intent);
    }
}
