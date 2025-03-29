package com.bandapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class PermissionHandler {
    public static final int PERMISSION_REQUEST_CODE = 100;

    public static boolean checkLocationPermissions(Context context) {
        return checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) &&
               checkPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    public static boolean checkBluetoothPermissions(Context context) {
        return checkPermission(context, Manifest.permission.BLUETOOTH) &&
               checkPermission(context, Manifest.permission.BLUETOOTH_ADMIN) &&
               checkPermission(context, Manifest.permission.BLUETOOTH_SCAN) &&
               checkPermission(context, Manifest.permission.BLUETOOTH_CONNECT);
    }

    public static boolean checkNotificationPermission(Context context) {
        return checkPermission(context, Manifest.permission.POST_NOTIFICATIONS);
    }

    private static boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestLocationPermissions(Activity activity) {
        List<String> permissions = new ArrayList<>();
        
        if (!checkPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!checkPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissions.toArray(new String[0]), PERMISSION_REQUEST_CODE);
        }
    }

    public static void requestBluetoothPermissions(Activity activity) {
        List<String> permissions = new ArrayList<>();
        
        if (!checkPermission(activity, Manifest.permission.BLUETOOTH)) {
            permissions.add(Manifest.permission.BLUETOOTH);
        }
        if (!checkPermission(activity, Manifest.permission.BLUETOOTH_ADMIN)) {
            permissions.add(Manifest.permission.BLUETOOTH_ADMIN);
        }
        if (!checkPermission(activity, Manifest.permission.BLUETOOTH_SCAN)) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN);
        }
        if (!checkPermission(activity, Manifest.permission.BLUETOOTH_CONNECT)) {
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT);
        }
        
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissions.toArray(new String[0]), PERMISSION_REQUEST_CODE);
        }
    }

    public static void requestNotificationPermission(Activity activity) {
        if (!checkPermission(activity, Manifest.permission.POST_NOTIFICATIONS)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    PERMISSION_REQUEST_CODE);
        }
    }
} 