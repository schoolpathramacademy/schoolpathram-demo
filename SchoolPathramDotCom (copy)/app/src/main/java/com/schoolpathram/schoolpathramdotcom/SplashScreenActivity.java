package com.schoolpathram.schoolpathramdotcom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.schoolpathram.schoolpathramdotcom.ui.user.LoginActivity;

public class SplashScreenActivity extends AppCompatActivity {
    Intent loginIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        loginIntent = new Intent(this, LoginActivity.class);
        getSupportActionBar().hide();


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                startActivity(loginIntent);
                finish();
            }
        }, 3000);
    }
}