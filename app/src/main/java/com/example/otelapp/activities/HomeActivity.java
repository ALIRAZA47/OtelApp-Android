package com.example.otelapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.otelapp.R;
import com.example.otelapp.activities.signin_signup.LoginActivity;
import com.example.otelapp.models.User;
import com.example.otelapp.utils.SharedPrefs;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

public class HomeActivity extends AppCompatActivity {
    //Firebase variables here
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference dbRef;
    private Time time;
    private GregorianCalendar timeCal;

    //UI variables
    AdView bannerAdView,bannerAdView2,bannerAdView3,bannerAdView4,bannerAdView5,bannerAdView6,bannerAdView7,bannerAdView8;
    NavigationView navigationView;
    MaterialToolbar appBar;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Button btnBookNow;
    TextView cWeatherMain, cWeatherTemp, timeTxt, dateTxt;
    TextView txtHeader;

    //Ordinary variables
    private User currentUser = new User();
    String userPhoneNumber;
    private ArrayList<User> users = new ArrayList<>();
    SharedPrefs sharedPref;

    private User cUser = new User();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        MobileAds.initialize(this); //initialize ads

        timeTxt = findViewById(R.id.timeNow);
        dateTxt = findViewById(R.id.date);

        time = new Time();
        SimpleTimeZone stz = new SimpleTimeZone(0*0*0*-1*-18000, "PK Time");
        Calendar calendar = new GregorianCalendar(stz);
        Date tt = new Date();
        calendar.setTime(tt);

        dateTxt.setText(calendar.get(Calendar.DATE) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR));

        //// run a thread for time
        Thread myThread = null;

        Runnable runnable = new CountDownRunner();
        myThread= new Thread(runnable);
        myThread.start();

        //navigation view
        navigationView = findViewById(R.id.navigationViewHome);
        View headView = navigationView.getHeaderView(0);
        txtHeader = headView.findViewById(R.id.currentUserEmailHeaderNav);


        //variables declarations here
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        sharedPref = new SharedPrefs(this);
        btnBookNow = findViewById(R.id.btnBookNow);
        btnBookNow.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        //function calls
        setupViews();
    }

    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    TextView txtCurrentTime= (TextView)findViewById(R.id.timeNow);
                    Date dt = new Date();
                    int hours = dt.getHours();
                    int minutes = dt.getMinutes();
                    int seconds = dt.getSeconds();
                    String curTime = hours + ":" + minutes + ":" + seconds;
                    txtCurrentTime.setText(curTime);
                }catch (Exception e) {}
            }
        });
    }


    class CountDownRunner implements Runnable {
        // @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }



    private void setupViews() {
        requestAndSetWeather();
        setupDrawerLayout();
        setupAds();
        retrieveDataFromDatabaseForUser();



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
        //cWeatherTempMin = findViewById(R.id.currentWeatherTempMin);
        //cWeatherTempMax = findViewById(R.id.currentWeatherTempMax);
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
                        //cWeatherTempMin.setText("Min: " + jsonObject_main.get("temp_min"));
                        //cWeatherTempMax.setText("Max: " + jsonObject_main.get("temp_max"));


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
                                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
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
                startActivity(intent);
                //drawerLayout.closeDrawers();
            }
            return true;
        });


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

    private void saveUserToSharedPref(User currentUser) {
        String user = new Gson().toJson(currentUser);
        sharedPref.removeKeyPair("currentUser");
        sharedPref.setString("currentUser", user);
        txtHeader.setText(currentUser.name + "");
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

