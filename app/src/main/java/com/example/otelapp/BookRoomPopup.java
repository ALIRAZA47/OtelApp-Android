package com.example.otelapp;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.google.android.material.card.MaterialCardView;

public class BookRoomPopup extends Activity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_room_popup);

        getActionBar().hide();
        // make this activity look like a pop up
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthOfPopup = displayMetrics.widthPixels - 20;
        int heightOfPopup = Math.floorDiv(displayMetrics.heightPixels,  2);;
        getWindow().setLayout(widthOfPopup , heightOfPopup+7);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.y = 10;
        getWindow().setAttributes(params);

    }
}