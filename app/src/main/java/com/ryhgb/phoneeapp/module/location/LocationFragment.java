package com.ryhgb.phoneeapp.module.location;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ryhgb.phoneeapp.util.PermissionUtil;
import com.ryhgb.phoneeapp.R;

/**
 * Created by ryutb on 05/02/2018.
 */

public class LocationFragment extends Fragment {
    private static final int REQUEST_PERMISDION_CODE = 999;
    Button mBtnCheckLocation;
    Button mBtnStartLocation;
    Button mBtnStopLocation;
    Button mBtnEnableLocationService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mBtnCheckLocation = (Button) view.findViewById(R.id.btnGetLocation);
//        mBtnCheckLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkPermission();
//            }
//        });
//        mBtnStartLocation = (Button) view.findViewById(R.id.btnStartLocationService);
//        mBtnStartLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startService();
//            }
//        });
//        mBtnStopLocation = (Button) view.findViewById(R.id.btnStopLocationService);
//        mBtnStopLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stopService();
//            }
//        });
        mBtnEnableLocationService = (Button) view.findViewById(R.id.btnEnableNotificationAccess);
        mBtnEnableLocationService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToNotificationAccessSettings();

            }
        });
    }

    private void redirectToNotificationAccessSettings() {
        startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
    }

    private void stopService() {
        Intent service = new Intent(getContext(), LocationTracking.class);
        getContext().stopService(service);
    }

    private void startService() {
        Intent service = new Intent(getContext(), LocationTracking.class);
        getContext().startService(service);
    }

    private void checkPermission() {
        boolean hasPermission = false;
        if (PermissionUtil.hasPermissions(getContext(), Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            hasPermission = true;
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISDION_CODE);
        }
        if (hasPermission) {
            LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!gps_enabled && !network_enabled) {
                AlertDialog.Builder dBuilder = new AlertDialog.Builder(getContext());
                dBuilder.setMessage("GPS or Network khong duoc kich hoat");
                dBuilder.setPositiveButton("Kich Hoat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                });
                dBuilder.show();
            }
        }
    }

}
