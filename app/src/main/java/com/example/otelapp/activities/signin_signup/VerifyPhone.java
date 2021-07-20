package com.example.otelapp.activities.signin_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.otelapp.R;
import com.example.otelapp.activities.MainActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhone extends AppCompatActivity {

    FirebaseAuth mAuth;
    String mVerificationID;
    Button verifyBTN;
    EditText enteredOTP;
    String phoneNumber;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        Log.i("TAG-Verify", "verifyPhoneNum: Entered in Verify Activity");

        mAuth = FirebaseAuth.getInstance();
        verifyPhoneNum();


    }

    private void verifyPhoneNum() {
        verifyBTN = findViewById(R.id.confirmOTPButton);
        enteredOTP = findViewById(R.id.enteredOTP);
        progressBar = findViewById(R.id.progressBar);
        enteredOTP = findViewById(R.id.enteredOTP);
        progressBar.setVisibility(View.GONE);
        phoneNumber = getIntent().getStringExtra("PhoneNumber");
        Log.e("TAG-Verify", "verifyPhoneNum: Entered in Verify Activity and Phone Number is --> "+phoneNumber);
        sendVerificationCodeToUser();
        verifyBTN.setOnClickListener(v -> {
            String code = String.valueOf(enteredOTP.getText());
            if(code.isEmpty()){
                Toast.makeText(this, "No OTP Entered ", Toast.LENGTH_SHORT).show();
            }
            else{
                verifyCode(code);
            }
        });
    }



    private void sendVerificationCodeToUser() {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+92"+phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    //Callback/functionlity for what to do after verification code is sent or Not
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationID = s;
            Log.e("TAG-Verify", "onCodeSent: Code is --> "+mVerificationID );
            

        }
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.e("TAG-Verify", "onVerificationCompleted: Code is --> "+phoneAuthCredential );
            if(phoneAuthCredential != null){
                progressBar.setVisibility(View.VISIBLE);
                signInUserByCredentials(phoneAuthCredential);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.i("TAG-Verify", "onVerificationFailed: Error is -->"+e.getMessage());
            Toast.makeText(VerifyPhone.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }



    };

    private void verifyCode(String verificationCodeByUser) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationID, verificationCodeByUser);
        signInUserByCredentials(credential);
    }

    private void signInUserByCredentials(PhoneAuthCredential credential) {
        progressBar.setVisibility(View.VISIBLE);
        Log.i("TAG-Verify", "signInUserByCredentials: ---->  Entered Here");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhone.this, task -> {
                   if(task.isSuccessful()){
                       Toast.makeText(this, "User Has been Logged in as "+mAuth.getCurrentUser().getPhoneNumber(), Toast.LENGTH_SHORT).show();
                       Intent gotoMainActivity = new Intent(this, MainActivity.class);
                       gotoMainActivity.putExtra("phoneNumber", phoneNumber);
                       startActivity(gotoMainActivity);
                       finish();
                   }
                   else{
                       Log.e("SignInException", "signInUserByCredentials: ", task.getException());
                   }
                });
    }

}