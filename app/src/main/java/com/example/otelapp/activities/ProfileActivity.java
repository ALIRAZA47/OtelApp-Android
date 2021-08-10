package com.example.otelapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.otelapp.R;
import com.example.otelapp.models.User;
import com.example.otelapp.utils.SharedPrefs;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference dbRef;
    EditText userName, userEmail, phone, cnic, address, gender;
    Button btnEdit, btnSave;
    User user = new User();
    private SharedPrefs prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initialize variables
        dbRef = FirebaseDatabase.getInstance().getReference();
        prefs = new SharedPrefs(this);
        String temp = prefs.getString("currentUser");
        user = new Gson().fromJson(temp, User.class);

        setupViews();
    }

    private void setupViews() {
        userName = findViewById(R.id.profileName);
        userEmail = findViewById(R.id.profileEmail);
        cnic = findViewById(R.id.profileCNIC);
        address = findViewById(R.id.profileAddress);
        gender = findViewById(R.id.profileGender);
        phone = findViewById(R.id.profilePhone);
        showUserInfo();

        //edit profile
        btnEdit = findViewById(R.id.btnEditProfile);
        btnSave = findViewById(R.id.btnSaveProfile);
        btnSave.setVisibility(View.GONE);

        btnEdit.setOnClickListener(v -> {
            //making them editable
            userName.setEnabled(true);
            userEmail.setEnabled(false);
            phone.setEnabled(false);
            address.setEnabled(true);
            cnic.setEnabled(true);
            gender.setEnabled(false);

            btnEdit.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);

            btnSave.setOnClickListener(v1 -> {
                //save user
                //get updated fields
                String newName = String.valueOf(userName.getText());
                String newAddr = String.valueOf(address.getText());
                String newCnic = String.valueOf(cnic.getText());
                user.name = newName;
                user.address = newAddr;
                user.cnic = newCnic;

                //.. update shared preferences' user
                prefs.removeKeyPair("currentUser");
                prefs.setString("currentUser", new Gson().toJson(user));

                //change data on firebase
                dbRef.child("main").child("users").child(user.phone).setValue(user);

                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                finish();


            });

        });


    }

    //shows user info of current user
    private void showUserInfo() {


        //making them non editable
        userName.setEnabled(false);
        userEmail.setEnabled(false);
        phone.setEnabled(false);
        address.setEnabled(false);
        cnic.setEnabled(false);
        gender.setEnabled(false);

        //show details of user
        userName.setText(String.valueOf(user.name));
        userEmail.setText(String.valueOf(user.email));
        address.setText(String.valueOf(user.address));
        phone.setText(String.valueOf(user.phone));
        gender.setText(String.valueOf(user.gender));
        cnic.setText(String.valueOf(user.cnic));
    }
}