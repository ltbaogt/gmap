package com.ryhgb.phoneeapp.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by ryutb on 22/01/2018.
 */

public class PermissionUtil {
    public interface OnDrawOverlayCallback {
        void onGranted(boolean isGranted);
    }

    public static final int PERMISSION_ALL = 1;
    public static final int PERMISSION_DRAW_OVERLAY = 2;
    public static final String[] PERMISSIONS_READ_CONTACT = {Manifest.permission.READ_CONTACTS};
    public static final String[] PERMISSIONS_READ_PHONE_STATE = {Manifest.permission.READ_PHONE_STATE};
    public static final String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE};

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean hasDrawOverlay(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ctx != null) {
            return Settings.canDrawOverlays(ctx);
        }
        return true;
    }

    public static void requestDrawOverlay(Fragment fg) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + fg.getActivity().getPackageName()));
        fg.startActivityForResult(intent, PERMISSION_DRAW_OVERLAY);
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults, OnDrawOverlayCallback[] callbacks) {
        switch (requestCode) {
            case PERMISSION_ALL: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        //READ_CONTACTS
                        checkIsGranted(permissions[i], PERMISSIONS[0], grantResults[i], callbacks[i]);
                        //READ_PHONE_STATE
                        checkIsGranted(permissions[i], PERMISSIONS[1], grantResults[i], callbacks[i]);
                    }
                }
            }
        }
    }

    private static boolean checkIsGranted(String needCheckPermission, String permission, int grandResult, OnDrawOverlayCallback callback) {
        if (needCheckPermission.equalsIgnoreCase(permission)) {
            if (grandResult == PackageManager.PERMISSION_GRANTED) {
                if (callback != null) callback.onGranted(true);
                Log.e("PermissionFragment", ">>>onRequestPermissionsResult " + needCheckPermission + " granted");
                return true;
            } else {
                if (callback != null) callback.onGranted(false);
                Log.e("PermissionFragment", ">>>onRequestPermissionsResult " + needCheckPermission + " denied");
                return false;
            }
        }
        return false;
    }

    public static void onRequestDrawOverlayResult(Context ctx, int resultCode, Intent data, OnDrawOverlayCallback callback) {
        if (callback != null) {
            callback.onGranted(hasDrawOverlay(ctx));
        }
    }
    //New
}
