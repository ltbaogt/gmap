package com.ryhgb.phoneeapp.module.location;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.ryhgb.phoneeapp.util.RealtimeDatabaseUtil;

/**
 * Created by ryutb on 05/02/2018.
 */

public class LocationTracking extends Service implements LocationUtil.AppLocationListener {

    LocationUtil mLocationUtil;
    RealtimeDatabaseUtil mRealtimeDatabaseUtil;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationUtil = LocationUtil.init(getApplicationContext(), this);
        mRealtimeDatabaseUtil = RealtimeDatabaseUtil.init(getApplicationContext(), "Location");
        mRealtimeDatabaseUtil.writeLocation(System.currentTimeMillis(), mLocationUtil.getLocation());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationUtil.removeUpdates();
        mLocationUtil = null;
        mRealtimeDatabaseUtil.gc();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public void onLocationChanged(Location location, double distance) {
        Log.d("LocationTracking", String.format(">>>ChangedDistance %1$skm", distance));
        Toast.makeText(this, String.format("%1$s,%2$s", location.getLatitude(), location.getLongitude()), Toast.LENGTH_SHORT).show();
        mRealtimeDatabaseUtil.writeLocation(System.currentTimeMillis(), location);
    }
}
