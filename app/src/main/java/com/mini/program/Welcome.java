package com.mini.program;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.mini.program.helper.GlobalHelper;

public class Welcome extends AppCompatActivity {
    public static float     v_density;
    GlobalHelper globalHelper = new GlobalHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fmx_welcome);
        v_density               = getResources().getDisplayMetrics().density;

        globalHelper.GenerateDefaultData();
        globalHelper.Profile("PORTRAIT");
        new Handler().postDelayed(() ->
                globalHelper.moveActivity(MainActivity.class, ""),
                2000);
    }
}