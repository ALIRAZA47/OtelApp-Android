package com.example.otelapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.otelapp.R;
import com.example.otelapp.models.Hotel;
import com.example.otelapp.models.Room;
import com.example.otelapp.models.User;
import com.example.otelapp.utils.SharedPrefs;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookRoomPopup extends Activity {
    //fireBase variables here
    DatabaseReference dbRef;
    StorageReference storageRef;

    // UI variables here
    TextView roomNo, roomFare, roomDetail, userBooking, phoneNum;
    Button btnBookRoom;
    ImageView imgRoom;

    //ordinary variables here
    Room selectedRoom = new Room();
    Hotel selectedHotel;
    User currentUser = new User();
    SharedPrefs preferences;
    ArrayList<String> imageURLs = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_room_popup);

        //hide action bar
        getActionBar().hide();

        // make this activity look like a pop up
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthOfPopup = displayMetrics.widthPixels;
        int heightOfPopup = Math.floorDiv(displayMetrics.heightPixels, 2);
        getWindow().setLayout(widthOfPopup, heightOfPopup + 50);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(params);

        //initialize variables
        preferences = new SharedPrefs(this);

        //Get values from Intent
        String temp = getIntent().getStringExtra("roomSelectedToBook");
        selectedRoom = new Gson().fromJson(temp, Room.class);
        String hotel = preferences.getString("selectedHotel");
        selectedHotel = new Gson().fromJson(hotel, Hotel.class);

        //function calls and variable declaration here
        dbRef = FirebaseDatabase.getInstance().getReference("main");
        selectedRoom = new Gson().fromJson(temp, Room.class);

        //shared Prefs
        String usr = preferences.getString("currentUser");
        currentUser = new Gson().fromJson(usr, User.class);

        //Firebase Storage
        Log.i("TAG-Room", "onCreate: --->" + selectedHotel.hotelName + "<-->" + selectedRoom.roomNumber);
        storageRef = FirebaseStorage.getInstance().getReference(selectedHotel.hotelName + "/" + selectedRoom.roomNumber + "/");

        // Function Calls here
        setupStorage();
        setupViews();

    }


    private void setupViews() {
        //finding values with IDs
        btnBookRoom = findViewById(R.id.btnBookNow_Booking);
        roomNo = findViewById(R.id.roomNo_Booking);
        roomDetail = findViewById(R.id.roomDetail_Booking);
        roomFare = findViewById(R.id.roomFare_Booking);
        userBooking = findViewById(R.id.username_Booking);
        phoneNum = findViewById(R.id.userPhone_Booking);

        //set values of Views
        roomNo.setText(getString(R.string.string_roomNo) + selectedRoom.roomNumber);
        roomFare.setText(getString(R.string.string_fare) + selectedRoom.roomFare);
        roomDetail.setText(selectedRoom.roomDetail + "");
        userBooking.setText(currentUser.name + "");
        phoneNum.setText(getString(R.string.string_Phone) + currentUser.phone);


        btnBookRoom.setOnClickListener(v -> {
            //update database on click of Room
            alertForYesNo();
        });
    }

    //function to display alert to ask user whether he is willing to book the room or not
    private void alertForYesNo() {

        TextView title = new TextView(this);
        title.setText(getString(R.string.sure_you_want_to_book));
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
                        selectedRoom.currentStatus = "Pending";
                        int num = Integer.parseInt(selectedRoom.roomNumber);
                        String numStr = String.valueOf(num);
                        dbRef.child("hotelsList").child(selectedHotel.hotelName).child("rooms").child(numStr).setValue(selectedRoom);
                        Toast.makeText(BookRoomPopup.this, "Done!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BookRoomPopup.this, SpecificHotel.class);
                        startActivity(intent);
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

    private void setupStorage() {
        storageRef.listAll()
                .addOnSuccessListener(listResult -> {
                    Log.i("TAG-Storage", "onSuccess: -->   Entered");

                    for (StorageReference prefix : listResult.getPrefixes()) {
                        // All the prefixes under listRef.
                        // You may call listAll() recursively on them.
                        //Log.i("TAG-Storage", "onSuccess: ---> " + prefix.toString());
                    }

                    for (StorageReference item : listResult.getItems()) {
                        // All the items under listRef.
                        //Log.i("TAG-Storage", "onSuccess: ---> " + item.toString());
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            imageURLs.add(uri.toString());
                            displayImage(uri.toString());
//                            Log.i("TAG---", "setupStorage: --> " + imageURLs.toString());
//                            Log.i("TAG---", "setupStorage: --> " + uri);

                        });
                    }
//                    Log.i("TAG---", "setupStorage:hehe --> " + imageURLs.toString());
                })
                .addOnFailureListener(e -> {
                    // Uh-oh, an error occurred!
                    Log.e("TAG-Storage", "onFailure: --> ", e);
                });

    }

    private void displayImage(String imageURL) {
        // Display image from URL
        ProgressBar progressBar = findViewById(R.id.imageLoadProgress);
        TextView text = findViewById(R.id.loadingText);
        imgRoom = findViewById(R.id.imageOfRoom1);

        //show progress bar and hide image view
        imgRoom.setVisibility(View.GONE);
        text.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        //loading image with picasso
        Picasso.with(this).load(imageURL)
                .resize(350, 350)
                .into(imgRoom, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        imgRoom.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        text.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        Log.e("TAG-LoadImg", "onError: --> Error in Loading Image");

                    }
                });


    }
}