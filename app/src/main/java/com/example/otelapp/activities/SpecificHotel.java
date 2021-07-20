package com.example.otelapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.otelapp.R;
import com.example.otelapp.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class SpecificHotel extends AppCompatActivity {

    //Firebase variables
    private DatabaseReference dbRef;

    //UI elements or variables

    // ordinary variables
    private SharedPreferences sharePref;
    private User currentUser = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_hotel);

        //initialize global variables here
        dbRef = FirebaseDatabase.getInstance().getReference();
        sharePref = PreferenceManager.getDefaultSharedPreferences(this);

        //function calls
        setupViews();


    }

    private void setupViews() {
        getUserFromSharedPref();
    }

    //get saved user from shared preferences
    private void getUserFromSharedPref() {
        String user = new String();
        user =sharePref.getString("currentUser", null);
        Log.i("TAG-HeHe", "getUserFromSharedPref: "+user);
        if(user.isEmpty()){
            Toast.makeText(this, "Couldn't retrieve user", Toast.LENGTH_SHORT).show();
        }
        else{
            currentUser = new Gson().fromJson(user, User.class);
            Log.i("TAG-HeHe", "getUserFromSharedPref: --> "+currentUser.toString());
        }
    }


}