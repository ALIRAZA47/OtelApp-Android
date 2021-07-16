package com.example.otelapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.otelapp.R;
import com.example.otelapp.activities.signin_signup.LoginActivity;
import com.example.otelapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference dbRef;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize variables
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        // function calls
        Button logout = findViewById(R.id.logOut);
        logout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }



}