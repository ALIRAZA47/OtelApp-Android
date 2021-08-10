package com.example.otelapp.activities.signin_signup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.otelapp.R;
import com.example.otelapp.activities.HomeActivity;
import com.example.otelapp.activities.MainActivity;
import com.example.otelapp.adminModule.AdminMain;
import com.example.otelapp.models.Hotel;
import com.example.otelapp.models.User;
import com.example.otelapp.utils.SharedPrefs;
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

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {

    //Firebase Variables
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    //ordinary variables
    private User currentUser = new User();
    String passwordStr;
    String emailStr;
    String phoneNumber;
    SharedPrefs prefs;

    // UI variables
    Switch userSwitch;
    Button btnLogin;
    TextView btnPhoneLogin;
    TextView btnSignup;
    TextView email, password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        //initialization of variables here
        prefs = new SharedPrefs(this);
        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("main");
        btnLogin = findViewById(R.id.btnEmailLogin);

        // Function call two setup views of activity
        setupViews();

    }

    private void setupViews() {
        setupClickListeners();

        userSwitch = findViewById(R.id.userSwitch);


        btnLogin.setOnClickListener(v -> {
            email = findViewById(R.id.loginEmail);
            password = findViewById(R.id.loginPassword);
            emailStr = email.getText().toString();
            passwordStr = password.getText().toString();

            if(emailStr.isEmpty()){
                email.setError(getString(R.string.email_required));
            }
            else if(passwordStr.isEmpty()){
                password.setError(getString(R.string.password_required));
            }
            else{
                //check for login state
                boolean isChecked = userSwitch.isChecked();
                    //        Log.i("TAG-Switch", "setupClickListeners: --> " + userSwitch.isChecked());
                    if(isChecked){
                        //login as admin
                        loginAsAdmin(emailStr, passwordStr);
                    }
                    else{
                        //login as user
                        login();
                    }

            }

        });

    }

    private void loginAsAdmin(String emailStr, String passwordStr) {
        Log.i(TAG, "loginAsAdmin: Entered Here");
//        ValueEventListener valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.i("TAG-Admin", "onDataChange: --> " + snapshot.child("admins"));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//                Log.e(TAG, "onCancelled: --> ", error.toException());
//            }
//        };
//        dbRef.addValueEventListener(valueEventListener);

        if(emailStr.equals("admin@otel.org.pk") && passwordStr.equals("admin123")){
            Toast.makeText(this, getString(R.string.admin_success), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AdminMain.class);
            mAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(task -> {
                //if success
                if (task.isSuccessful()){
                    prefs.setString("ADMIN", "true");
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this, getString(R.string.invalid_admin_id), Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void login() {
        prefs.removeKeyPair("ADMIN");
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
            Toast.makeText(this, getString(R.string.provide_email_pass), Toast.LENGTH_SHORT).show();
            return;
        }
        else if (passwordStr.length() < 6) {
            Toast.makeText(this, getString(R.string.password_must_contain_six_char), Toast.LENGTH_SHORT).show();
            return;
        }
        else if(emailStr.equals("admin@otel.org.pk")){
            Toast.makeText(this, getString(R.string.not_valid_user), Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            mAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, Make a Toast to Confirm
                        Toast.makeText(LoginActivity.this, getString(R.string.singed_in), Toast.LENGTH_SHORT).show();
                        Intent gotoMainActivity = new Intent(LoginActivity.this, HomeActivity.class);
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
}