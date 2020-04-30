package com.example.charityapplicationsigninup.accounts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.charityapplicationsigninup.MainActivity;
import com.example.charityapplicationsigninup.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * This class has sign up + verification code
 */
public class RegisterActivity extends AppCompatActivity {
    //firebase auth global variable
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private StorageReference mMediaStorageReference;
    //image uro
    public Uri filePath;
    //editTexts
    private EditText uName;
    private EditText uNationalId;
    private EditText uAddress;
    //image view for the uploaded id card
    private ImageView id_image;
    //account verification
    private String mAuthCredentials;
    private EditText ver_code;
    private int photoUploadedSuccessfully = 0;
    private static final int RC_PHOTO_PICKER = 2;
    //uri of uploaded image
    private String idUri = null;

    /**
     * Normal onCreate method
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        //initializing firebase auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        //initializing database
        //firebase database
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        //database reference
        mDatabaseRef = mFirebaseDatabase.getReference();
        //storage reference
        //firebase storage
        FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
        mMediaStorageReference = mFirebaseStorage.getReference().child("userIDS");
        //initializing image view
        id_image = findViewById(R.id.id_image);
        //verification
        ver_code = findViewById(R.id.ver_code);
        mAuthCredentials = getIntent().getStringExtra("AuthCredentials");
        //initialize editTexts
        uName = findViewById(R.id.uName);
        uAddress = findViewById(R.id.userAddress);
        uNationalId = findViewById(R.id.uNationalId);
        //initialize buttons
        //upload  & choose buttons
        Button choose_btn = findViewById(R.id.choose_btn);
        Button upload_btn = findViewById(R.id.upload_btn);
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileUploader();
            }
        });
        choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choosing the file from gallery
                if (uNationalId.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please put your national ID first", Toast.LENGTH_SHORT).show();
                } else {
                    fileChooser();
                }
            }
        });
    }

    /**
     * This function chooses the file from gallery and starts activity for result
     */
    private void fileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"),
                RC_PHOTO_PICKER);
    }

    /**
     * This function uploads the photo chosen by the user to firebase storage
     */
    private void fileUploader() {
        //get a reference to store file at chat_photos/<FILENAME>
        final StorageReference photoRef =
                mMediaStorageReference.child(uNationalId.getText().toString() + filePath.getLastPathSegment());
        //upload file to firebase storage
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        UploadTask uploadTask = photoRef.putFile(filePath);
        uploadTask.addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                photoUploadedSuccessfully = 1;
                Toast.makeText(RegisterActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
            }
        });
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return photoRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    idUri = downloadUri.toString();
                    //id_image.setImageURI(downloadUri);
                } else {
                    // Handle failures
                    // ...
                    Toast.makeText(RegisterActivity.this, "Problem Occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * This function writes a new user with all his/her attributes to the realtime database.
     *
     * @param userName
     * @param telNum
     * @param highestEduLvl
     * @param nationalNum
     * @param nationalIdPhotoUrl
     * @param address
     * @param numOfReports
     * @param postedProblemsIds
     * @param helpRequests
     * @param seenOrNot
     */
    private void writeNewUser(String userName, String telNum, int highestEduLvl, String nationalNum, String nationalIdPhotoUrl, String address, int numOfReports, String postedProblemsIds, String helpRequests, int seenOrNot) {
        User user = new User(userName,
                telNum,
                highestEduLvl,
                nationalNum,
                nationalIdPhotoUrl,
                address,
                numOfReports,
                postedProblemsIds,
                helpRequests,
                seenOrNot);
        mDatabaseRef.child("users").child(telNum).setValue(user);
    }

    /**
     * This function gets the result of the intent, when the user gets a photo from gallery
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER) {
            filePath = data.getData();
            id_image.setImageURI(filePath);
        }
    }

    /**
     * This function checks the verification code if it's correct or not
     *
     * @param view
     */
    public void addUser(View view) {
        if (fieldsOk()) {
            String verNum = ver_code.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthCredentials, verNum);
            signInWithPhoneAuthCredential(credential);
        } else {
            Toast.makeText(this, "Please Complete the form.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This function starts Home Activity
     */
    private void sendUserToHome() {
        Intent homeIntent = new Intent(RegisterActivity.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }

    /**
     * This function validates the input fields of register
     *
     * @return
     */
    private boolean fieldsOk() {
        if (ver_code.getText().toString().isEmpty() || photoUploadedSuccessfully != 1 || uName.getText().toString().isEmpty() || uAddress.getText().toString().isEmpty() || uNationalId.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * this function authenticates the user (Authenticating the user on Firebase if the verification code is correct
     *
     * @param credential
     */
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Verification Process", "signInWithCredential:success");
                            boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (isNewUser) {
                                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                writeNewUser(uName.getText().toString(),
                                        user.getPhoneNumber(),
                                        2,
                                        uNationalId.getText().toString(),
                                        idUri,
                                        uAddress.getText().toString(),
                                        0,
                                        "",
                                        "",
                                        0);
                                Toast.makeText(RegisterActivity.this, "added successfully", Toast.LENGTH_SHORT).show();
                                sendUserToHome();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Old User, Please sign in", Toast.LENGTH_SHORT).show();
                                sendUserToHome();
                            }
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("Verification Process", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(RegisterActivity.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
