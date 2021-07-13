package com.example.otelapp.activities.signin_signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.otelapp.R;

public class PhoneLoginActivity extends AppCompatActivity {

    TextView btnEmailLogin;
    TextView btnSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        // Function call two setup views of activity
        setupViews();
    }

    private void setupViews() {
       setupClickListeners();
    }

    //setup all clicks of activity
    private void setupClickListeners() {
        btnEmailLogin = findViewById(R.id.btnEmailLogin);
        btnSignup = findViewById(R.id.btnSignup);

        // If clicked on "Login by Email"
        btnEmailLogin.setOnClickListener(v -> {
            Intent gotoPhoneActivity = new Intent(this, LoginActivity.class);
            startActivity(gotoPhoneActivity);
            finish();
        });

        // if clicked on "sign up"
        btnSignup.setOnClickListener(v -> {
            Intent gotoSignupActivity = new Intent(this, SignupActivity.class);
            startActivity(gotoSignupActivity);
            finish();
        });

    }
}