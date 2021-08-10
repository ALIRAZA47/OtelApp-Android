package com.example.otelapp.activities.signin_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otelapp.R;
import com.example.otelapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    // Database variables
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference dbRef;

    // dropdown menu variables
    Spinner genderDropDown;
    ArrayAdapter<String> dropDownAdapter;

    // variables (normal)
    String genderOfUser;

    // UI variables
    EditText email;
    EditText name;
    EditText password;
    EditText confirmPassword;
    EditText phone;
    EditText cnic;
    EditText address;
    TextView btnLogin;
    Button btnSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //initialize variables here
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();


        //if user clicks on "already have an account? Login" than this option will be triggered
        btnLogin = findViewById(R.id.haveAccountLogin);
        btnLogin.setOnClickListener(v -> {
            Intent gotoLoginActivity = new Intent(this, LoginActivity.class);
            startActivity(gotoLoginActivity);
        });

        // Function calls here
        setupViews();
    }

    private void setupViews() {
        //it will set drop down menu to select a gender
        showDropDownForGender();
        //function to signup user / add new user
        addNewUser();
    }

    // set drop down menu for gender
    private void showDropDownForGender() {
        genderDropDown = findViewById(R.id.genderDropDownMenu);
        String[] items = new String[] {getString(R.string.select_gender), getString(R.string.male), getString(R.string.female), getString(R.string.other_gender)};
        dropDownAdapter = new ArrayAdapter<String>(this, R.layout.drop_down_layout, items);
        genderDropDown.setAdapter(dropDownAdapter);

        genderDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderOfUser = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    // Add new User
    private void addNewUser() {
        //find edit-texts
        email = findViewById(R.id.signupEmail);
        name = findViewById(R.id.signupName);
        password = findViewById(R.id.signupPassword);
        confirmPassword = findViewById(R.id.signupPasswordConfirm);
        phone = findViewById(R.id.signupPhone);
        cnic = findViewById(R.id.signupCNIC);
        address = findViewById(R.id.signupAddress);
        // find button
        btnSubmit = findViewById(R.id.btnAddNewAccount);

        //get Strings from these Views



        //add new user
        //set listener for signup button
        btnSubmit.setOnClickListener(v -> {
            String emailStr = String.valueOf(email.getText());
            String nameStr = String.valueOf(name.getText());
            String passwordStr = String.valueOf(password.getText());
            String passwordConfirmStr = String.valueOf(confirmPassword.getText());
            String phoneStr = String.valueOf(phone.getText());
            String addressStr = String.valueOf(address.getText());
            String cnicStr = String.valueOf(cnic.getText());

            User newUser = new User(nameStr,
                    emailStr,
                    phoneStr,
                    cnicStr,
                    addressStr,
                    genderOfUser);

            Log.i("SignupFileds", "addNewUser: "+nameStr+emailStr+passwordStr+passwordConfirmStr+phoneStr+addressStr+genderOfUser);
            if(nameStr.isEmpty()){
                Toast.makeText(this, getString(R.string.name_required), Toast.LENGTH_SHORT).show();
                return;
            }
            else if (emailStr.isEmpty() ||passwordStr.isEmpty() || passwordConfirmStr.isEmpty()){
                Toast.makeText(this, getString(R.string.provide_email_pass), Toast.LENGTH_SHORT).show();
                return;
            }
            else if(!(passwordConfirmStr.equals(passwordStr))){
                Toast.makeText(this, getString(R.string.passwords_not_match), Toast.LENGTH_SHORT).show();
                return;
            }

            // now create variable and save in database
            else{
                dbRef.child("main").child("users").child(phoneStr).setValue(newUser);
                mAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()){
                            Toast.makeText(SignupActivity.this, getString(R.string.signup_success), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
                Intent gotoLoginActivity = new Intent(this, LoginActivity.class);
                startActivity(gotoLoginActivity);
                finish();
            }

        });
    }

}