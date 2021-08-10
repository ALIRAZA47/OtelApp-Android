package com.example.otelapp.adminModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otelapp.R;
import com.example.otelapp.activities.SpecificHotel;
import com.example.otelapp.adapters.RoomsAdapter;
import com.example.otelapp.adminModule.adminAdapters.RoomAdapter;
import com.example.otelapp.models.Hotel;
import com.example.otelapp.models.Room;
import com.example.otelapp.models.User;
import com.example.otelapp.utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AdminRooms extends AppCompatActivity {

    //UI elements or variables
    TextView hotelNameOnTopBar;
    RecyclerView roomRecyclerView;
    //Firebase variables
    private DatabaseReference dbRef;

    // ordinary variables
    RoomAdapter roomsAdapter;
    private SharedPrefs sharePref;
    private ArrayList<Room> rooms = new ArrayList<>();
    private Hotel selectedHotelAdmin = new Hotel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_rooms);

        //initialize global variables here
        dbRef = FirebaseDatabase.getInstance().getReference();
        sharePref = new SharedPrefs(this);
        hotelNameOnTopBar = findViewById(R.id.hotelName_roomActivityAdmin);

        //get value from shared prefs
        String temp = sharePref.getString("selectedHotelAdmin");
        selectedHotelAdmin = new Gson().fromJson(temp, Hotel.class);
        //function calls and other code
        // set hotel name on top bar
        hotelNameOnTopBar.setText(selectedHotelAdmin.hotelName + "");
        setupViews();
    }

    private void setupViews() {
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
                        .child(selectedHotelAdmin.hotelName).child("rooms").getChildren()) {
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
                Toast.makeText(AdminRooms.this, "Failed to Retrieve Data", Toast.LENGTH_SHORT).show();
                return;
            }
        };
        dbRef.addValueEventListener(postListener);
    }

    private void setupRecyclerView() {
        roomRecyclerView = findViewById(R.id.roomsRecyclerViewAdmin);
        roomsAdapter = new RoomAdapter(rooms, this, selectedHotelAdmin.hotelName);
        roomRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        roomRecyclerView.setAdapter(roomsAdapter);

    }

}