package com.example.charityapplicationsigninup.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

/**
 * This class is the same as registerClass but here we have the verification only
 * It's used when the user is an old user
 * ONLY TO SIGN IN
 */
public class VerifyActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mAuthCredentials;
    private EditText ver_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify);
        ver_code = findViewById(R.id.ver_code);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mAuthCredentials = getIntent().getStringExtra("AuthCredentials");
    }

    //todo check this
    @Override
    protected void onStart() {
        super.onStart();
    }

    public void checkVerCode(View view) {
        String verNum = ver_code.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthCredentials, verNum);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Verification Process", "signInWithCredential:success");
                            boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (isNewUser) {
                                Toast.makeText(VerifyActivity.this, "New User, Please sign up!", Toast.LENGTH_SHORT).show();
                                task.getResult().getUser().delete();
                                sendUsertoWelcome();
                            } else {
                                sendUserToHome();
                                Toast.makeText(VerifyActivity.this, "Welcome Back!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("Verification Process", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(VerifyActivity.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void sendUserToHome() {
        Intent homeIntent = new Intent(VerifyActivity.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }

    private void sendUsertoWelcome() {
        Intent welcomeIntent = new Intent(VerifyActivity.this, WelcomeActivity.class);
        welcomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        welcomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(welcomeIntent);
        finish();
    }
}
