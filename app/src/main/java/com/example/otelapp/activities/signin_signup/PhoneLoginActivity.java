package com.example.otelapp.activities.signin_signup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.otelapp.R;
import com.example.otelapp.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PhoneLoginActivity extends AppCompatActivity {

    //Firebase variables here
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    // UI variables here
    private TextView btnEmailLogin;
    private TextView btnSignup;
    private EditText phoneNumber;
    private Button btnLogin;

    // Common / Ordinary Variables here
    private String phoneNumberString;
    //ArrayList<User> users = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();

    //start of onCreate function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        // variable definition here
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        // Function call two setup views of activity
        setupViews();
    }

    private void setupViews() {
        retrieveDataFromDB();
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
        btnSignup.setOnClickListener(v -> {
            Intent gotoSignupActivity = new Intent(this, SignupActivity.class);
            startActivity(gotoSignupActivity);
            finish();
        });

        // if clicked on "sign up"
        btnSignup.setOnClickListener(v -> {
            Intent gotoSignupActivity = new Intent(this, SignupActivity.class);
            startActivity(gotoSignupActivity);
            finish();
        });

    }


    //retrieve data from real time Database
    private void retrieveDataFromDB() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Successfully retrieved data
                for (DataSnapshot userSnap : snapshot.child("main").child("users").getChildren()) {
                    User user = new User();
                    user = userSnap.getValue(User.class);
                    users.add(user);
                    Log.i("TAG--", "onDataChange: --> " + userSnap.toString());

                }
                Log.i("DB-Data", "onDataChange: Data from DB is  --> " + users.toString() + "size --> " + users.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //could'nt get Data log an error message
                Log.i("TAG", "loadPost:onCancelled  Error is-->  " + (error.toException().getMessage()));
            }
        };
        dbRef.addValueEventListener(postListener);

        Log.i("DB-Data", "checkIfNumberIsRegistered: " + users.toString());


    }


    //to check whether an account is register with the given number or not
    private boolean checkIfNumberIsRegistered(String phoneNumberString) {
        Log.i("Data-users", "checkIfNumberIsRegistered: users are--> " + users.toString());
        //check if numbers MActh
        for (User user : users) {
            if (user.phone.equals(phoneNumberString)) {
                Toast.makeText(PhoneLoginActivity.this, "Phone Match....", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    // Start Verify activity to verify phone
    private void setupVerifyActivity() {
        btnLogin = findViewById(R.id.btnPhoneLogin);
        phoneNumber = findViewById(R.id.loginPhoneNum);
        Intent gotoVerifyActivity = new Intent(this, VerifyPhone.class);
        Intent gotoLoginActivity = new Intent(this, PhoneLoginActivity.class);

        btnLogin.setOnClickListener(v -> {
            //Start verify activity to verify phone number
            phoneNumberString = String.valueOf(phoneNumber.getText());
            if (checkIfNumberIsRegistered(phoneNumberString)) {
                Toast.makeText(PhoneLoginActivity.this, "User Found", Toast.LENGTH_SHORT).show();
                // start verification (activity)
                gotoVerifyActivity.putExtra("PhoneNumber", phoneNumberString);
                startActivity(gotoVerifyActivity);
            } else {
                Toast.makeText(PhoneLoginActivity.this, "Phone Number Not Registered", Toast.LENGTH_SHORT).show();
                startActivity(gotoLoginActivity);
            }
            finish();

        });


    }


}