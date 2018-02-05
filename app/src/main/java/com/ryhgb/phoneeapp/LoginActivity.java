package com.ryhgb.phoneeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ryhgb.phoneeapp.module.location.LocationFragment;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportFragmentManager().beginTransaction().add(R.id.mainContainer, new LocationFragment()).commit();
    }


}

