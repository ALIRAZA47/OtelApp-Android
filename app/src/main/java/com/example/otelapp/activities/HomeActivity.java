package com.example.otelapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.otelapp.R;
import com.example.otelapp.activities.signin_signup.LoginActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    //UI variables
    NavigationView navigationView;
    MaterialToolbar appBar;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Button btnBookNow;

    //Firebase variables
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //variables declarations here
        mAuth = FirebaseAuth.getInstance();
        btnBookNow = findViewById(R.id.btnBookNow);
        btnBookNow.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        //function calls
        setupViews();
    }

    private void setupViews() {

        setupDrawerLayout();
    }

    //setup drawer navigation
    private void setupDrawerLayout() {
        navigationView = findViewById(R.id.navigationViewHome);
        appBar = findViewById(R.id.topAppBarHome);
        setSupportActionBar(appBar);
        drawerLayout = findViewById(R.id.drawerHome);
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


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

