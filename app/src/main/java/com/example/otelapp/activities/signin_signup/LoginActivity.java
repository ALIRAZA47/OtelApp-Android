package com.example.otelapp.activities.signin_signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otelapp.R;

public class LoginActivity extends AppCompatActivity {

    // Declarations here
    TextView btnPhoneLogin;
    TextView btnSignup;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Function call two setup views of activity
        setupViews();

    }

    private void setupViews() {
        setupClickListeners();

    }

    //setup all clicks of activity
    private void setupClickListeners() {
        btnPhoneLogin = findViewById(R.id.btnPhoneLogin);
        btnSignup = findViewById(R.id.btnSignup);

        // If clicked on "Login by Phone Number"
        btnPhoneLogin.setOnClickListener(v -> {
            Intent gotoPhoneActivity = new Intent(this, PhoneLoginActivity.class);
            startActivity(gotoPhoneActivity);
        });

        // if clicked on "sign up"
        btnSignup.setOnClickListener(v -> {
            Intent gotoSignupActivity = new Intent(this, SignupActivity.class);
            startActivity(gotoSignupActivity);
        });
    }
}