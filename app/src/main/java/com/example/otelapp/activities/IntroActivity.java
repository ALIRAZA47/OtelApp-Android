package com.example.otelapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otelapp.R;
import com.example.otelapp.activities.signin_signup.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class IntroActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    int counter = 0;
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        mAuth = FirebaseAuth.getInstance();
//        redirect();
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if (mAuth.getCurrentUser() != null) {
                    Intent mainIntent = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Intent mainIntent = new Intent(IntroActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}