package com.example.charityapplicationsigninup.problems;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charityapplicationsigninup.R;
import com.example.fblikeapp.PostsAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ViewProblemsActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProblemDatabaseReference;
    private ChildEventListener childEventListener;
    //new adapter
    private RecyclerView mRecyclerView;
    private PostsAdapter mPostsAdapter;
    ArrayList<Problem> problems;
    private int catNum;
    //todo progressbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_problems);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //get category number
        catNum = getIntent().getIntExtra("catNum", -1);
        mProblemDatabaseReference = mFirebaseDatabase.getReference().child("problems");
        //offline database and update
        mProblemDatabaseReference.keepSynced(true);
        // Initialize references to views
        //initialize recyclerview
        this.mRecyclerView = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        this.mRecyclerView.setLayoutManager(mLayoutManager);
        //initializing list
        problems = new ArrayList<>();
        mPostsAdapter = new PostsAdapter(problems, this);
        mRecyclerView.setAdapter(mPostsAdapter);
        DownloadProblems task = new DownloadProblems();
        task.execute();
    }

    private void attachDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                //called for every child
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Problem problem = dataSnapshot.getValue(Problem.class);
                    if (problem.getmCatNum() == catNum) {
                        problems.add(problem);
                        mPostsAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }

                //changing the position
                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            //to be used only when messages is triggered as it reference messages
            mProblemDatabaseReference.addChildEventListener(childEventListener);
        }
    }

    private class DownloadProblems extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //todo we need to speed up the performance
        }

        @Override
        protected Void doInBackground(Void... voids) {
            attachDatabaseReadListener();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
