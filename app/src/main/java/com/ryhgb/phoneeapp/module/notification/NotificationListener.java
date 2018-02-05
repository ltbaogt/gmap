package com.ryhgb.phoneeapp.module.notification;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.ryhgb.phoneeapp.util.RealtimeDatabaseUtil;

/**
 * Created by ryutb on 05/02/2018.
 */

public class NotificationListener extends NotificationListenerService {
    private static final String TAG = "NotificationListener";
    RealtimeDatabaseUtil mRealtimeDatabaseUtil;

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRealtimeDatabaseUtil = RealtimeDatabaseUtil.init(this, "Notification");

    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        mRealtimeDatabaseUtil.writeNotification(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // Implement what you want here
    }
}
