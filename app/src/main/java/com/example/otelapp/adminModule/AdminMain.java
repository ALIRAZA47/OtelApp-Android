package com.example.otelapp.adminModule;

import android.app.AlertDialog;
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
import com.example.otelapp.adminModule.adminAdapters.HotelAdapter;
import com.example.otelapp.models.Hotel;
import com.example.otelapp.models.User;
import com.example.otelapp.utils.SharedPrefs;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
public class AdminMain extends AppCompatActivity {

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
    FloatingActionButton signOut;

    Button logout;
    // ordinary variables here
    private SharedPrefs sharedPref;
    private User user;
    private User currentUser = new User();
    private ArrayList<Hotel> hotels = new ArrayList<>();
    private HotelAdapter hotelsAdapter;
    private String userPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        //initialize variables
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        sharedPref = new SharedPrefs(this);

        //function calls here
        setupViews();


    }

    private void setupViews() {
        retrieveDataFromDatabase();

//        setupRecyclerView();
        setupClickListeners();

    }

    private void setupClickListeners() {
        signOut = findViewById(R.id.logoutFab);

        //if lougout FAB clicked
        signOut.setOnClickListener(v -> {
           new AlertDialog.Builder(this, R.style.MyAlertDialogTheme)
                   .setTitle("Logout Confirmation")
                   .setMessage(getString(R.string.sure_you_want_to_logout))
                   .setPositiveButton("Logout", (dialog, which) -> {
                       mAuth.signOut();
                       sharedPref.removeKeyPair("ADMIN");
                       Intent intent = new Intent(this, LoginActivity.class);
                       startActivity(intent);
                       finish();
                   })
                   .setNegativeButton("No", (dialog, which) -> {
                       //no clicked
                       dialog.dismiss();
                   }).show();
        });
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

    //setup recycler view
    private void setupRecyclerView() {
        hotelRecyclerView = findViewById(R.id.hotelsRecyclerViewAdmin);
        Log.i("TAG-ALI", "setupRecyclerView: " + hotels.toString());
        hotelsAdapter = new HotelAdapter(this, hotels);
        hotelRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        hotelRecyclerView.setAdapter(hotelsAdapter);

    }

}
