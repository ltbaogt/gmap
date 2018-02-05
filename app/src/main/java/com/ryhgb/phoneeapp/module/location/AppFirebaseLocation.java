package com.ryhgb.phoneeapp.module.location;

import android.location.Location;

/**
 * Created by ryutb on 05/02/2018.
 */

public class AppFirebaseLocation {
    public static AppFirebaseLocation sInstance;
    public double latitude;
    public double longitude;

    public static AppFirebaseLocation getInstance(long time, Location location) {
        if (sInstance == null) sInstance = new AppFirebaseLocation(time, location);
        sInstance.setLocation(time, location);
        return sInstance;
    }

    public AppFirebaseLocation(long time, Location location) {
        setLocation(time, location);
    }

    public void setLocation(long time, Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }
}
