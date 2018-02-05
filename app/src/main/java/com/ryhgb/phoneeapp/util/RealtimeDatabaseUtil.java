package com.ryhgb.phoneeapp.util;

import android.app.Notification;
import android.content.Context;
import android.location.Location;
import android.service.notification.StatusBarNotification;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ryhgb.phoneeapp.module.location.AppFirebaseLocation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ryutb on 05/02/2018.
 */

public class RealtimeDatabaseUtil {

    private static final String TAG = "RealtimeDatabaseUtil";

    FirebaseDatabase mDatabase;
    String type;

    private RealtimeDatabaseUtil() {
    }

    private RealtimeDatabaseUtil(Context ctx, String type) {
        // Write a message to the database
        mDatabase = FirebaseDatabase.getInstance();
        this.type = type;
    }

    public static RealtimeDatabaseUtil init(Context ctx, String type) {
        return new RealtimeDatabaseUtil(ctx, type);
    }

    private DatabaseReference getRootDb() {
        return mDatabase.getReference(android.os.Build.MODEL);
    }

    private DatabaseReference getGroupData(String rootData) {
        return getRootDb().child(type).child(rootData);
    }

    public void writeLocation(long dateInMilli, Location location) {
        if (mDatabase != null) {
            getGroupData(getCurrentFullDay(dateInMilli)).child(getCurrentHourMinus(dateInMilli))
                    .setValue((AppFirebaseLocation.getInstance(dateInMilli, location)));
        }
    }

    private String getCurrentFullDay(long millisecond) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy", Locale.US);
        return sdf.format(new Date(millisecond));
    }

    private String getCurrentHourMinus(long millisecond) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH-mm", Locale.US);
        return sdf.format(new Date(millisecond));
    }

    private String getCurrentHourMinusSecond(long millisecond) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss", Locale.US);
        return sdf.format(new Date(millisecond));
    }

    public void gc() {
        mDatabase = null;
    }

    public void writeNotification(StatusBarNotification sbn) {
        if (mDatabase != null) {
            getGroupData(getCurrentFullDay(System.currentTimeMillis()))
                    .child(sbn.getPackageName().replace(".","_"))
                    .child(getCurrentHourMinusSecond(System.currentTimeMillis()))
                    .setValue(sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT));
        }
    }
}
