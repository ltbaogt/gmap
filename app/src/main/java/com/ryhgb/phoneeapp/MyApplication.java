package com.ryhgb.phoneeapp;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Created by ryutb on 05/02/2018.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
