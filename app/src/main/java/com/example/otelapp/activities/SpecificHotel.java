package com.example.otelapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otelapp.R;
import com.example.otelapp.adapters.RoomsAdapter;
import com.example.otelapp.models.Hotel;
import com.example.otelapp.models.HotelDetails;
import com.example.otelapp.models.Room;
import com.example.otelapp.models.User;
import com.example.otelapp.utils.SharedPrefs;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SpecificHotel extends AppCompatActivity {

    //UI elements or variables
    TextView hotelNameOnTopBar;
    RecyclerView roomRecyclerView;
    //Firebase variables
    private DatabaseReference dbRef;

    // ordinary variables
    RoomsAdapter roomsAdapter;
    private SharedPrefs sharePref;
    private User currentUser = new User();
    private Hotel selectedHotel = new Hotel();
    private ArrayList<Room> rooms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_hotel);

        //initialize global variables here
        dbRef = FirebaseDatabase.getInstance().getReference();
        sharePref = new SharedPrefs(this);
        hotelNameOnTopBar = findViewById(R.id.hotelName_roomActivity);

        //get value from shared prefs
        String temp = sharePref.getString("selectedHotel");
        selectedHotel = new Gson().fromJson(temp, Hotel.class);
        //function calls and other code
        // set hotel name on top bar
        hotelNameOnTopBar.setText(selectedHotel.hotelName + "");
        setupViews();


    }

    private void setupViews() {
        getUserFromSharedPref();
        retrieveDataForHotelRoomsFromDB();
//        addDummyDataToDB();

    }

    private void retrieveDataForHotelRoomsFromDB() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Successfully retrieved data
                rooms.clear();
//                Log.i("TAG-Hotel", "onDataChange:  Data--> " + snapshot.child("main").child("hotelsList").child("rooms").toString());
                for (DataSnapshot userSnap : snapshot.child("main").child("hotelsList")
                        .child(selectedHotel.hotelName).child("rooms").getChildren()) {
                    Room room = new Room();
                    room = userSnap.getValue(Room.class);
                    rooms.add(room);
//                    Log.i("TAG--", "onDataChange: --> " + userSnap.toString());

                }
                setupRecyclerView();
//                Log.i("DB-Data", "onDataChange: Data from DB is  --> " + rooms.toString() + "size --> " + rooms.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //could'nt get Data log an error message
//                Log.i("TAG", "loadPost:onCancelled  Error is-->  " + (error.toException().getMessage()));
                Toast.makeText(SpecificHotel.this, getString(R.string.failed_to_retrieve_data), Toast.LENGTH_SHORT).show();
                return;
            }
        };
        dbRef.addValueEventListener(postListener);
    }

    private void setupRecyclerView() {
        roomRecyclerView = findViewById(R.id.roomsRecyclerView);
        roomsAdapter = new RoomsAdapter(rooms, this, selectedHotel.hotelName);
        roomRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        roomRecyclerView.setAdapter(roomsAdapter);

    }

    private void addDummyDataToDB() {


        ArrayList<Room> rooms = new ArrayList<>();
        Room r1 = new Room("01", "Available", "1500", "1-single bed without AC. Wifi is available");
        Room r2 = new Room("02", "Booked", "1500", "1-single bed without AC. Wifi is available");
        Room r3 = new Room("03", "Available","1800", "1-single bed with AC. Wifi is available");
        Room r4 = new Room("04", "Available","2500", "1-single and 1-double bed, without AC. Wifi is available");
        Room r5 = new Room("05", "Available", "3500", "2-double bed with AC. Wifi is available");
        rooms.add(null);
        rooms.add(r1);
        rooms.add(r2);
        rooms.add(r3);
        rooms.add(r4);
        rooms.add(r5);


        Log.i("TAG-Errorrr", "addDummyDataToDB: --> " + rooms.toString());
        //HotelDetails details = new HotelDetails(selectedHotel, "2 beds and a kitchen room", rooms);
        selectedHotel.rooms = rooms;
        Task task = dbRef.child("main").child("hotelsList").child(selectedHotel.hotelName).setValue(selectedHotel);
        //Log.i("TAG-Errorrr", "addDummyDataToDB: -->" + task.getException().getMessage());

    }

    //get saved user from shared preferences
    private void getUserFromSharedPref() {
        String user = "";
        user = sharePref.getString("currentUser");
        Log.i("TAG-HeHe", "getUserFromSharedPref: " + user);
        if (user.isEmpty()) {
            Toast.makeText(this, getString(R.string.user_not_retrieved), Toast.LENGTH_SHORT).show();
        } else {
            currentUser = new Gson().fromJson(user, User.class);
            Log.i("TAG-HeHe", "getUserFromSharedPref: --> " + currentUser.toString());
        }
    }


}