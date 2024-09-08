package com.thuydev.saydream;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
<<<<<<<< HEAD:app/src/main/java/com/thuydev/saydream/MainActivity.java

public class MainActivity extends AppCompatActivity {
========

import com.thuydev.saydream.R;

public class ActivityMain extends AppCompatActivity {
>>>>>>>> cdf0d3d (add activity and function):app/src/main/java/com/thuydev/saydream/ActivityMain.java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<<< HEAD:app/src/main/java/com/thuydev/saydream/MainActivity.java
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
========
>>>>>>>> cdf0d3d (add activity and function):app/src/main/java/com/thuydev/saydream/ActivityMain.java
    }
}