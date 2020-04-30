package com.example.charityapplicationsigninup.ui.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.charityapplicationsigninup.R;
import com.example.charityapplicationsigninup.accounts.WelcomeActivity;
import com.example.charityapplicationsigninup.categories.Categories;
import com.example.charityapplicationsigninup.categories.CategoryAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mCategoryDatabaseReference;
    private ChildEventListener childEventListener;
    private ListView mCategoryListView;
    private CategoryAdapter mCategoryAdapter;
    private View root;
    private EditText searchEdit;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        searchEdit = root.findViewById(R.id.search_edt);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mCategoryDatabaseReference = mFirebaseDatabase.getReference().child("categories");
        //offline database and update
        mCategoryDatabaseReference.keepSynced(true);
        // Initialize references to views
        mCategoryListView = (ListView) root.findViewById(R.id.list);
        //initializing list
        ArrayList<Categories> categories = new ArrayList<>();
        mCategoryAdapter = new CategoryAdapter(getContext(), R.layout.category_card, categories);
        mCategoryListView.setAdapter(mCategoryAdapter);
        // First, hide loading indicator so error message will be visible
        View loadingIndicator = root.findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        //downloading categories from firebase
        DownloadCategories task = new DownloadCategories();
        task.execute();
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCategoryAdapter.getFilter().filter(s);
                mCategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        if (mCurrentUser == null) {
            Intent loginIntent = new Intent(getActivity(), WelcomeActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }
    }

    private void attachDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                //called for every child
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    //it will be deserialized
                    Categories category = dataSnapshot.getValue(Categories.class);
                    mCategoryAdapter.add(category);
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
            mCategoryDatabaseReference.addChildEventListener(childEventListener);
        }
    }

    private class DownloadCategories extends AsyncTask<Void, Void, Void> {
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
            // Hide loading indicator because the data has been loaded
            View loadingIndicator = root.findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
        }
    }
}
