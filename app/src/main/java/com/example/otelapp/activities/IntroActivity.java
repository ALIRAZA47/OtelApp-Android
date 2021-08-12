package com.example.otelapp.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otelapp.R;
import com.example.otelapp.activities.signin_signup.LoginActivity;
import com.example.otelapp.adminModule.AdminMain;
import com.example.otelapp.utils.SharedPrefs;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class IntroActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    SharedPrefs prefs;
    int counter = 0;
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new SharedPrefs(this);
        setContentView(R.layout.activity_intro);
        mAuth = FirebaseAuth.getInstance();
//        redirect();
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(() -> {
            /* Create an Intent that will start the Menu-Activity. */
            if(prefs.getString("ADMIN") == null){
                if (mAuth.getCurrentUser() != null) {
                    Intent mainIntent = new Intent(IntroActivity.this, HomeActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Intent mainIntent = new Intent(IntroActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
            else{
                Intent adminMain = new Intent(IntroActivity.this, AdminMain.class);
                startActivity(adminMain);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }



    private void setLocale(String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        prefs.setLang(lang);

    }
    private void loadDefLang() {
        String defLang = prefs.getLang();
        setLocale(defLang);
    }
}