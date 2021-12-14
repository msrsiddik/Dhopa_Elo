package com.amanullah.dhopaelo;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class OfflineActivity extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
