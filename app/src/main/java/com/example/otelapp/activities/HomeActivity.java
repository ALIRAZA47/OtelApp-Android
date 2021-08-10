package com.example.otelapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.otelapp.R;
import com.example.otelapp.activities.signin_signup.LoginActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class HomeActivity extends AppCompatActivity {

    //UI variables
    AdView bannerAdView,bannerAdView2,bannerAdView3,bannerAdView4,bannerAdView5,bannerAdView6,bannerAdView7,bannerAdView8;
    NavigationView navigationView;
    MaterialToolbar appBar;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Button btnBookNow;
    TextView cWeatherMain, cWeatherTemp, cWeatherTempMin, cWeatherTempMax;

    //Firebase variables
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        MobileAds.initialize(this); //initialize ads


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
        requestAndSetWeather();
        setupDrawerLayout();
        setupAds();



    }

    private void setupAds() {
        bannerAdView = findViewById(R.id.adView);
        bannerAdView2 = findViewById(R.id.adView2);
        bannerAdView3 = findViewById(R.id.adView3);
        bannerAdView4 = findViewById(R.id.adView4);
        bannerAdView5 = findViewById(R.id.adView5);
        bannerAdView6 = findViewById(R.id.adView6);
        bannerAdView7 = findViewById(R.id.adView7);
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAdView.loadAd(adRequest);
        bannerAdView2.loadAd(adRequest);
        bannerAdView3.loadAd(adRequest);
        bannerAdView4.loadAd(adRequest);
        bannerAdView5.loadAd(adRequest);
        bannerAdView6.loadAd(adRequest);
        bannerAdView7.loadAd(adRequest);
    }

    private void requestAndSetWeather() {
        String reqURL = "https://api.openweathermap.org/data/2.5/weather?q=mianwali&appid=3beba2c5a71008c8501d6a9f70a0372b&units=metric";
        cWeatherMain = findViewById(R.id.currentWeatherString);
        cWeatherTemp = findViewById(R.id.currentWeatherTemperature);
        cWeatherTempMin = findViewById(R.id.currentWeatherTempMin);
        cWeatherTempMax = findViewById(R.id.currentWeatherTempMax);
        DecimalFormat df = new DecimalFormat("##.#");

        //request for weather
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, reqURL, null,
                response -> {
                    Log.e("Weather-", "requestAndSetWeather: -- > response received");
                    // on success of request
                    try {
                        JSONObject jsonObject_main = response.getJSONObject("main");
                        JSONArray weather_array = response.getJSONArray("weather");
                        JSONObject jsonObject_weather = weather_array.getJSONObject(0);
                        cWeatherMain.setText("" + jsonObject_weather.get("description"));
                        cWeatherTemp.setText(df.format(Double.parseDouble(String.valueOf(jsonObject_main.get("temp")))) + getString(R.string.degree));
                        cWeatherTempMin.setText("Min: " + jsonObject_main.get("temp_min"));
                        cWeatherTempMax.setText("Max: " + jsonObject_main.get("temp_max"));


                    } catch (JSONException e) {
                        Log.e("Weather-Error", "requestAndSetWeather: --> " + e.getMessage());

                    }

                }, error -> {
            //error related to JSON request
            Log.e("Weather-Error1", "requestAndSetWeather: --> ", error);

        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
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
            if(item.getItemId() == R.id.navProfile){
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                //drawerLayout.closeDrawers();
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

