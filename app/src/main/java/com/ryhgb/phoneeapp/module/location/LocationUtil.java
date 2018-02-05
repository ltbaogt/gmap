package com.ryhgb.phoneeapp.module.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by ryutb on 05/02/2018.
 */

public class LocationUtil implements LocationListener {

    private static final String TAG = "LocationUtil";

    public interface AppLocationListener {
        void onLocationChanged(Location location, double distance);
    }

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 50;
    private static final long MIN_TIME_BW_UPDATES = 1000;

    private Context mContext;

    private Location mLocation;
    private LocationManager mLocationManager;
    private String mCurrentLocProvider;

    private AppLocationListener mListeners;

    private LocationUtil() {
    }

    private LocationUtil(Context ctx, AppLocationListener listener) {
        mContext = ctx;
        mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        mListeners = listener;
        mCurrentLocProvider = getBestProvider(mLocationManager);
        init(mCurrentLocProvider);
    }

    public static LocationUtil init(Context ctx, AppLocationListener listener) {
        return new LocationUtil(ctx, listener);
    }

    @SuppressLint("MissingPermission")
    public void init(String locationProvider) {
        if (mLocationManager != null) {
            mLocationManager.requestLocationUpdates(locationProvider,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            mLocation = mLocationManager.getLastKnownLocation(locationProvider);
        }
    }

    private String getBestProvider(LocationManager locationManager) {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        List<String> lProviders = locationManager.getProviders(false);
        for (int i = 0; i < lProviders.size(); i++) {
            Log.d("LocationTracking", lProviders.get(i));
        }
        return locationManager.getBestProvider(criteria, true); // null
    }

    private double getDistanceFromLatLongInKm(double lat1, double long1, double lat2, double long2) {
        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2 - lat1);
        double dLon = deg2rad(long2 - long1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    @Override
    public void onLocationChanged(Location location) {
        double distance = getDistanceFromLatLongInKm(mLocation.getLatitude(), mLocation.getLongitude(), location.getLatitude(), location.getLongitude());
        if (mListeners != null) {
            mListeners.onLocationChanged(location, distance);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, String.format(">>>onStatusChanged: %1$s, %2$s", provider, status));
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void removeUpdates() {
        mLocationManager.removeUpdates(this);
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        Location lastLocation = mLocationManager.getLastKnownLocation(mCurrentLocProvider);
        if (lastLocation != null) mLocation = lastLocation;
        return mLocation;
    }
}
