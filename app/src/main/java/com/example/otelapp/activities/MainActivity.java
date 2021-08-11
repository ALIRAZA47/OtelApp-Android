package com.example.otelapp.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.otelapp.utils.SharedPrefs;
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
import java.util.GregorianCalendar;

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
    private SharedPrefs sharedPref;
    private User user;
    private User currentUser = new User();
    private ArrayList<Hotel> hotels = new ArrayList<>();
    private HotelMainAdapter hotelsAdapter;
    private String userPhoneNumber;
    private Time timeNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize variables
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        sharedPref = new SharedPrefs(this);

        //time now
        timeNow = new Time();
        Log.i("TAG-Time", "onCreate: --> " +timeNow.toString());

        //function calls here
        setupViews();


    }

    private void setupViews() {
        retrieveDataFromDatabase();
        //setupDrawerLayout();

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
                hotels.clear();
                Log.i("TAG-Hotel", "onDataChange:  Data--> " + snapshot.child("main").child("hotelsList").toString());
                for (DataSnapshot userSnap : snapshot.child("main").child("hotelsList").getChildren()) {
                    Hotel hotel = new Hotel();
                    Log.i("TAG-Snap", "onDataChange: --> " + userSnap.toString());
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

                TextView title = new TextView(this);
                title.setText(getString(R.string.sure_you_want_to_logout));
                title.setPadding(10, 10, 10, 10);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(20);

                AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme).create();
                alertDialog.setCustomTitle(title);
                this.dbRef = FirebaseDatabase.getInstance().getReference("main");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                drawerLayout.closeDrawers();
                                startActivity(intent);
                                finish();
                                dialog.dismiss();

                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);

            }
            if(item.getItemId() == R.id.navProfile){
                Intent intent = new Intent(this, ProfileActivity.class);
                drawerLayout.closeDrawers();
                startActivity(intent);
                //drawerLayout.closeDrawers();
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



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}