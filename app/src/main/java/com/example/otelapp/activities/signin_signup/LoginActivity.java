package com.example.otelapp.activities.signin_signup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.otelapp.R;
import com.example.otelapp.activities.MainActivity;
import com.example.otelapp.models.Hotel;
import com.example.otelapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    //Firebase Variables
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    //ordinary variables
    private User currentUser = new User();
    String passwordStr;
    String emailStr;
    String phoneNumber;

    // UI variables
    Button btnLogin;
    TextView btnPhoneLogin;
    TextView btnSignup;
    TextView email, password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialization of variables here
        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
        btnLogin = findViewById(R.id.btnEmailLogin);

        // Function call two setup views of activity
        setupViews();

    }

    private void setupViews() {
        setupClickListeners();

        btnLogin.setOnClickListener(v -> {
            email = findViewById(R.id.loginEmail);
            password = findViewById(R.id.loginPassword);
            emailStr = email.getText().toString();
            passwordStr = password.getText().toString();
            login();

        });

    }

    private void login() {
        setupLoginViaEmail();
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

    //setup email login functionality


    private void setupLoginViaEmail() {
        if (emailStr.isEmpty() || passwordStr.isEmpty()) {
            Toast.makeText(this, "Please Provide Email & Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (passwordStr.length() < 6) {
            Toast.makeText(this, "Password must contain at least 6 Character", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, Make a Toast to Confirm
                    Toast.makeText(LoginActivity.this, "Signed in", Toast.LENGTH_SHORT).show();
                    Intent gotoMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                    gotoMainActivity.putExtra("currentUser", new Gson().toJson(currentUser));
                    startActivity(gotoMainActivity);
                    finish();

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Auth", "onComplete: ", task.getException());
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}