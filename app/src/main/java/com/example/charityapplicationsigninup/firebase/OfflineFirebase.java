package com.example.charityapplicationsigninup.firebase;

import com.google.firebase.database.FirebaseDatabase;

public class OfflineFirebase extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
