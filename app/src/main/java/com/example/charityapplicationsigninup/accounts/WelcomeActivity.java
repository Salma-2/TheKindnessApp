package com.example.charityapplicationsigninup.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.charityapplicationsigninup.MainActivity;
import com.example.charityapplicationsigninup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

/**
 * This class is the first class which appears to the user after the slide screens
 * It contains phone number input, country choose
 * Two buttons : One navigates to Register activity(Activity for sign-up) , The other navigates to verifyActivity(Activity for sign in)
 */
public class WelcomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private EditText p_number;
    private CountryCodePicker ccp;
    //int to show if the button clicked is sign in or sign up
    //0 sign up
    //1 sign in
    //-1 default
    private int signIn_signUp = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        p_number = findViewById(R.id.p_number);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(p_number);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                if (signIn_signUp == 0) {
                    Intent verIntent = new Intent(WelcomeActivity.this, RegisterActivity.class);
                    verIntent.putExtra("AuthCredentials", s);
                    startActivity(verIntent);
                    finish();
                }
                if (signIn_signUp == 1) {
                    Intent verIntent = new Intent(WelcomeActivity.this, VerifyActivity.class);
                    verIntent.putExtra("AuthCredentials", s);
                    startActivity(verIntent);
                    finish();
                }
            }
        };
    }
    //todo check internet connection

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        if (mCurrentUser != null) {
            mCurrentUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        //User still exists and credentials are valid
                        sendUserToHome();
                    } else {
                        //User has been disabled, deleted or login credentials are no longer valid,
                        //so send them to Login screen
                    }
                }
            });
        }
    }

    public void signUpButton(View view) {
        sendVerificationCode();
        signIn_signUp = 0;
    }

    public void signInButton(View view) {
        sendVerificationCode();
        signIn_signUp = 1;
    }

    public void sendVerificationCode() {
        if (p_number.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please insert your number", Toast.LENGTH_SHORT).show();
        } else {
            String phoneNumber = ccp.getFullNumberWithPlus();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,
                    60,
                    TimeUnit.SECONDS,
                    WelcomeActivity.this,
                    mCallbacks
            );
        }
    }

    private void sendUserToHome() {
        Intent homeIntent = new Intent(WelcomeActivity.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }
}
