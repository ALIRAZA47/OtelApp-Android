package com.example.otelapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otelapp.R;
import com.example.otelapp.activities.signin_signup.LoginActivity;
import com.example.otelapp.adapters.HotelMainAdapter;
import com.example.otelapp.models.Hotel;
import com.example.otelapp.models.User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;

//start of main activity
public class MainActivity extends AppCompatActivity {

    //Firebase variables here
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference dbRef;

    // UI variables / elements here
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    RecyclerView hotelRecyclerView;
    MaterialToolbar appBar;

    Button logout;
    // ordinary variables here
    private SharedPreferences sharedPref;
    private User user;
    private User currentUser = new User();
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Hotel> hotels = new ArrayList<>();
    private HotelMainAdapter hotelsAdapter;
    private String userPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize variables
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        //function calls here
        setupViews();


    }

    private void setupViews() {
        retrieveDataFromDatabase();
        setupDrawerLayout();
        retrieveDataFromDatabaseForUser();
//        setupRecyclerView();
        //setupClickListeners();

        Log.i("TAG-CurrentUser", "setCurrentUser: --> "+currentUser.toString());
    }

    //retrieve data from database
    private void retrieveDataFromDatabase() {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Successfully retrieved data
                Log.i("TAG-Hotel", "onDataChange:  Data--> " + snapshot.child("main").child("hotelsList").toString());
                for (DataSnapshot userSnap : snapshot.child("main").child("hotelsList").getChildren()) {
                    Hotel hotel = new Hotel();
                    hotel = userSnap.getValue(Hotel.class);
                    hotels.add(hotel);
                    Log.i("TAG--", "onDataChange: --> " + userSnap.toString());

                }
                setupRecyclerView();
                Log.i("DB-Data", "onDataChange: Data from DB is  --> " + hotels.toString() + "size --> " + hotels.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //could'nt get Data log an error message
                Log.i("TAG", "loadPost:onCancelled  Error is-->  " + (error.toException().getMessage()));
            }
        };
        dbRef.addValueEventListener(postListener);
    }

    private void retrieveDataFromDatabaseForUser() {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Successfully retrieved data
                for (DataSnapshot userSnap : snapshot.child("main").child("users").getChildren()) {
                    User user = new User();
                    user = userSnap.getValue(User.class);
                    users.add(user);
                    setCurrentUser();
                    Log.i("TAG-SingleUserSnap", "onDataChange: --> " + userSnap.toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //could'nt get Data log an error message
                Log.i("TAG", "loadPost:onCancelled  Error is-->  " + (error.toException().getMessage()));
            }
        };
        dbRef.addValueEventListener(postListener);
    }

    //set current user function
    private void setCurrentUser() {
        //if user has logged in using phone number
        if (mAuth.getCurrentUser().getEmail().isEmpty()) {
            String tempPhone = mAuth.getCurrentUser().getPhoneNumber();
            userPhoneNumber = tempPhone.substring(3);
            Log.i("TAG-PhoneMain", "setCurrentUser:   numbers are ---> "+tempPhone+" and "+userPhoneNumber);
            tempPhone.substring(0,2);
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Successfully retrieved data
                    currentUser = snapshot.child("main").child("users").child(userPhoneNumber).getValue(User.class);
                    saveUserToSharedPref(currentUser);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //could'nt get Data log an error message
                    Log.i("TAG", "loadPost:onCancelled  Error is-->  " + (error.toException().getMessage()));
                }
            };
            dbRef.addValueEventListener(postListener);

        }
        //if user has logged in using email
        else {
            for (User user : users) {
                if (user.email.equals(mAuth.getCurrentUser().getEmail())) {
                    currentUser = user;
                    saveUserToSharedPref(currentUser);
                }
            }

        }


//        Log.i("TAG-CurrentUser", "setCurrentUser: --> "+currentUser.toString());
    }
    private void setUser(User user) {
        currentUser = user;
//        Log.i("TAG-CurrentUser", "setCurrentUser: --> "+currentUser.toString());
    }


    // function to setup drawer menu
    private void setupDrawerLayout() {
        navigationView = findViewById(R.id.navigationViewMain);
        appBar = findViewById(R.id.topAppBar);
        setSupportActionBar(appBar);
        drawerLayout = findViewById(R.id.mainDrawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.primaryColor));
        toggle.syncState();

        //set listeners for navigation items
        navigationView.setNavigationItemSelectedListener(item -> {
            //if logout is clicked
            if (item.getItemId() == R.id.navLogout) {
                mAuth.signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                drawerLayout.closeDrawers();
            }
            return true;
        });


    }

    //setup recycler view
    private void setupRecyclerView() {
        hotelRecyclerView = findViewById(R.id.hotelsRecyclerView);
        Log.i("TAG-ALI", "setupRecyclerView: " + hotels.toString());
        hotelsAdapter = new HotelMainAdapter(this, hotels);
        hotelRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        hotelRecyclerView.setAdapter(hotelsAdapter);

    }


    //Save current user to shared preferences
    private void saveUserToSharedPref(User currentUser) {
        String user = new Gson().toJson(currentUser);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("currentUser", user);
        editor.commit();
        Log.i("TAG-UserSaved", "saveUserToSharedPref: ---> User has been Saved"+ user);


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}