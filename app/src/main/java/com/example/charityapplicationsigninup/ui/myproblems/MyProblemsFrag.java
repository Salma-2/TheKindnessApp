package com.example.charityapplicationsigninup.ui.myproblems;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.charityapplicationsigninup.problems.Problem;
import com.example.charityapplicationsigninup.problems.MyProblemAdapter;
import com.example.charityapplicationsigninup.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyProblemsFrag extends Fragment {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProblemDatabaseReference;
    private ChildEventListener childEventListener;

    private ListView mProblemListView;
    private MyProblemAdapter mProblemAdapter;

    List<Problem> problems;

    ProgressDialog progressDialog;

    private int catNum;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.my_problems, container, false);

        mFirebaseDatabase = FirebaseDatabase.getInstance();


        mProblemDatabaseReference = mFirebaseDatabase.getReference().child("problems");

        //offline database and update
        mProblemDatabaseReference.keepSynced(true);

        // Initialize references to views
        mProblemListView = (ListView)root.findViewById(R.id.my_probs_list);

        //initializing list
        problems = new ArrayList<>();
        mProblemAdapter = new MyProblemAdapter(getContext(), R.layout.problem_card, problems);
        mProblemListView.setAdapter(mProblemAdapter);

        new AlertDialog.Builder(getContext())
                .setTitle("Delete Instruction")
                .setMessage("Hold Press on the Problem to delete!")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Thank You!", Toast.LENGTH_SHORT).show();
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        mProblemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                deleteProblem(position);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return false;
            }
        });


        DownloadProblems task = new DownloadProblems();
        task.execute();

        return root;
    }

    private void attachDatabaseReadListener(){

        if(childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                //called for every child
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    //it will be deserialized
                    Problem problem = dataSnapshot.getValue(Problem.class);
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser mCurrentUser = mAuth.getCurrentUser();
                    if (problem.getmPhone().equals(mCurrentUser.getPhoneNumber())){
                        mProblemAdapter.add(problem);
                    }

//                    progressDialog.dismiss();

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

    private class DownloadProblems extends AsyncTask<Void,Void,Void> {

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


    private void deleteProblem(int position){

        Problem problem = problems.get(position);
        mProblemDatabaseReference.child(problem.getmId()).removeValue();
        problems.remove(position);
        mProblemAdapter.notifyDataSetChanged();

    }


}
