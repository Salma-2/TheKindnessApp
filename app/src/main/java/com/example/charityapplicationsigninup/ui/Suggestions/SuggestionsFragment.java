package com.example.charityapplicationsigninup.ui.Suggestions;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.charityapplicationsigninup.MainActivity;
import com.example.charityapplicationsigninup.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SuggestionsFragment extends Fragment {

    private EditText sugg_et;
    private Button sugg_btn;

    //for firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseRef;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_suggestions, container, false);

        sugg_et = root.findViewById(R.id.sugg_et);
        sugg_btn = root.findViewById(R.id.suggest_btn);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mFirebaseDatabase.getReference();


        sugg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sugg_et.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please type your suggestion first!", Toast.LENGTH_SHORT).show();
                }else{
                    mDatabaseRef.child("suggestions").push().setValue(sugg_et.getText().toString());
                    Toast.makeText(getContext(), "Thank You!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);

                }
            }
        });

        return root;
    }
}
