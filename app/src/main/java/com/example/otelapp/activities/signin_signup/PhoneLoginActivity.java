package com.example.otelapp.activities.signin_signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.otelapp.R;

public class PhoneLoginActivity extends AppCompatActivity {

    TextView btnEmailLogin;
    TextView btnSignup;
    EditText phoneNumber;
//    LinearLayout btnLogin;
    Button btnLogin;
    String phoneNumberString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        // Function call two setup views of activity

        setupViews();
    }

    private void setupViews() {
       setupClickListeners();
       setupVerifyActivity();
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

    // Start Verify activity to verify phone
    private void setupVerifyActivity() {
        btnLogin = findViewById(R.id.btnPhoneLogin);
        phoneNumber = findViewById(R.id.loginPhoneNum);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("TAG", "onClick: ---> Clicked the Button");
                //Start verify activity to verify phone number
                phoneNumberString = String.valueOf(phoneNumber.getText());
//                Log.e("TAG-Verify", "setupVerifyActivity: and Phone no is-->  "+phoneNumberString );

                Intent gotoVerifyActivity = new Intent(PhoneLoginActivity.this, VerifyPhone.class);
                gotoVerifyActivity.putExtra("PhoneNumber", phoneNumberString);
//                Log.i("TAG-Verify", "About to start another activity");

                startActivity(gotoVerifyActivity);
            }
        });


    }

}