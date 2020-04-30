package com.example.charityapplicationsigninup.problems;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.charityapplicationsigninup.MainActivity;
import com.example.charityapplicationsigninup.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class CreateProblem extends AppCompatActivity {
    //todo validate pics
    private static final String TAG = "PostActivity";
    //Problem info
    private Problem problem;
    private String problemDescription = "";
    private String problemTitle = "";
    private int isUrgent = 0;
    private String userPhone = "";
    private String userId = "";
    private int isRequested = 0;
    private String photoUri = "";
    private String currentPhotoPath;
    private String mAddress = "";
    private static final int REQUEST_IMAGE_PICKER = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    ArrayList<Uri> imageUriArray = new ArrayList<>();
    //views

    private EditText titleEditText;
    private EditText problemEditText;
    private Switch urgentButton;
    private Button photoPickerButton;
    private ProgressDialog progressDialog;
    private Button cameraButton;
    private ImageView imageView;
    private EditText regionText;
    private EditText cityText;
    private EditText streetText;
    private Spinner spinner;

    //Entry point for app to access the database
    private FirebaseDatabase mFirebaseDatabase;
    //referencing the problem info portion of db
    private DatabaseReference mProblemDatabaseReference;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mMediaStorageReference;
    private Uri selectedImageUri;
    //category number of the problem
    private int catNum;
    //boolean for checking image upload
    private boolean imageUploaded = false;
//todo check security rules

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_problem);
        //get category number
        catNum = getIntent().getIntExtra("catNum", -1);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        //path of the category in database
        mProblemDatabaseReference = mFirebaseDatabase.getReference().child("problems");
        mMediaStorageReference = mFirebaseStorage.getReference().child("media");

        // Initialize references to views
        titleEditText = (EditText) findViewById(R.id.problem_title);
        problemEditText = (EditText) findViewById(R.id.problem_description);
        urgentButton = (Switch) findViewById(R.id.urgent);
        photoPickerButton = (Button) findViewById(R.id.photoPickerButton);
        cameraButton = (Button) findViewById(R.id.cameraButton);
        imageView = (ImageView) findViewById(R.id.imageView);
        regionText = findViewById(R.id.address_region);
        cityText = findViewById(R.id.address_city);
        streetText = findViewById(R.id.address_street);

        //getting spinner of countries
        spinner = findViewById(R.id.spinner);
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
        for (String country : countries) {
            System.out.println(country);
        }
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, countries);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(countryAdapter);
        spinner.setSelection(5);

        //initializing problem
        problem = new Problem(userId
                , userPhone
                , problemTitle
                , problemDescription
                , photoUri
                , isUrgent,
                isRequested,
                mAddress);
        //listeners
        //Urgent switch
        urgentButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Yes urgent
                    isUrgent = 1;
                } else {
                    //Not urgent
                    isUrgent = 0;
                }
            }
        });
        // ImagePickerButton shows an image picker to upload a image
        photoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select photo"),
                        REQUEST_IMAGE_PICKER);
            }
        });
        //Camera Button
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICKER) {
                selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);
                fileUploader();
            }
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                imageView.setImageURI(selectedImageUri);
                fileUploader();
            }
        }
    }

    private void post() {
        //check if the user didn't write problem description or title.
        if (titleEditText.getText().toString().isEmpty() || problemEditText.getText().toString().isEmpty() || !imageUploaded
         || streetText.getText().toString().isEmpty() || regionText.getText().toString().isEmpty() || cityText.getText().toString().isEmpty()) {
            Toast.makeText(getBaseContext(), "Please complete all required info",
                    Toast.LENGTH_SHORT).show();
        } else {
            problemTitle = titleEditText.getText().toString();
            problemDescription = problemEditText.getText().toString();
            String country = spinner.getSelectedItem().toString();
            String city = cityText.getText().toString();
            String region = regionText.getText().toString();
            String street = streetText.getText().toString();

            String address = street + " st, " + region + ", " + city + ", " + country +".";

            mAddress = address;

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser mCurrentUser = mAuth.getCurrentUser();
            problem.setmId(mProblemDatabaseReference.push().getKey());
            problem.setmPhone(mCurrentUser.getPhoneNumber());
            problem.setmIsRequested(isRequested);
            problem.setmIsUrgent(isUrgent);
            problem.setmTitle(problemTitle);
            problem.setmProblem(problemDescription);
            problem.setmPhotoUrl(photoUri);
            problem.setmCatNum(catNum);
            problem.setmAddress(mAddress);
            mProblemDatabaseReference.child(problem.getmId()).setValue(problem);
            Toast.makeText(this, "Problem Posted Thank You!", Toast.LENGTH_SHORT).show();
            //send the user back to Home
            Intent i = new Intent(CreateProblem.this, MainActivity.class);
            startActivity(i);
        }
    }

    private void fileUploader() {
        final StorageReference photoRef =
                mMediaStorageReference.child(selectedImageUri.getLastPathSegment());
        //upload file to firebase storage
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        UploadTask uploadTask = photoRef.putFile(selectedImageUri);
        uploadTask.addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(CreateProblem.this, "Uploaded", Toast.LENGTH_SHORT).show();
                imageUploaded = true;
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
                    photoUri = downloadUri.toString();
                } else {
                    Toast.makeText(CreateProblem.this, "NULL", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Save the full-size photo
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //Take a photo with a camera app
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(getApplicationContext(), "Error while creating file.",
                        Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                selectedImageUri = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else
            Log.e(TAG, "Error");
        //Toast.makeText( this,"Error",Toast.LENGTH_LONG ).show();
    }


    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.post_check, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.post_done) {
            post();
        }
        return super.onOptionsItemSelected(item);
    }
}