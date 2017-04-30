package com.inf8405.polymtl.lab3.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.inf8405.polymtl.lab3.managers.GlobalDataManager;

public class GPSManager extends BroadcastReceiver {
    private static final String TAG = "GPSManager";
    
    private static final double default_latitude = 45.50461055;
    private static final double default_longitude = -73.61444413;
    
    private Context _ctx;
    private boolean _isProviderEnabled;
    private LocationManager _lm;
    
    //___________________________________________________________________________________________________________________________________//
    public GPSManager(Context ctx) {
        _ctx = ctx;
        updateGPSProviderStatus(_ctx);
        
        if (_isProviderEnabled) {
            try {
                getLatestGPSLocation(_ctx);
                //Checking if there is still permission for accessing GPS location
                if (_ctx.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
                    // Minimum time interval between location updates, in milliseconds
                    // Default values will get continues location but it drown the battery
                    // Therefore, we will check if in low battery mode and change interval to 1 minutes instead of 10 seconds
                    int _minTime = ((GlobalDataManager) ctx.getApplicationContext()).get_battery_level() <= 15 ? 10 * 1000 : 60 * 1000;
                    
                    //Implementing LocationListener for concurrent location updates based in defined intervals
                    _lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, _minTime, 100, new LocationListener() {
                        
                        @Override
                        public void onLocationChanged(Location location) {
                            ((GlobalDataManager) _ctx.getApplicationContext()).set_deviceLocation(location);
                            ((GlobalDataManager) _ctx.getApplicationContext()).update_GPS_status_on_the_main_activity(_ctx);
                        }
                        
                        public void onStatusChanged(String s, int i, Bundle bundle) {
                        }
                        
                        public void onProviderEnabled(String s) {
                        }
                        
                        public void onProviderDisabled(String s) {
                        }
                    });
                }
            } catch (SecurityException ex) {
                Log.e(TAG, ex.getMessage());
            }
        }
    }
    
    //___________________________________________________________________________________________________________________________________//
    public static void getLatestGPSLocation(Context ctx) {
        try {
            LocationManager _lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
            ((GlobalDataManager) ctx.getApplicationContext()).set_deviceLocation(_lm.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        } catch (SecurityException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }
    
    //___________________________________________________________________________________________________________________________________//
    public void onReceive(Context context, Intent intent) {
        updateGPSProviderStatus(context);
    }
    
    //___________________________________________________________________________________________________________________________________//
    private void updateGPSProviderStatus(Context ctx) {
        _isProviderEnabled = false;
        GlobalDataManager _gdm = (GlobalDataManager) ctx.getApplicationContext();
        _gdm.setGPSProviderStatus(isGPSFunctional(ctx));
        _gdm.update_GPS_status_on_the_main_activity(ctx);
    }
    
    //___________________________________________________________________________________________________________________________________//
    // This method will check functionality of the gps provider (if it's on/off).
    private int isGPSFunctional(Context ctx) {
        try {
            _lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
            _isProviderEnabled = _lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            return _isProviderEnabled ? 1 : 0;
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            ex.printStackTrace();
            return -1;
        }
    }
    
    //Default values in case of GPS failure
    public static double getDefaultLatitude() {
        return 45.50461055;
    }
    
    public static double getDefaultLongitude() {
        return -73.61444413;
    }
}