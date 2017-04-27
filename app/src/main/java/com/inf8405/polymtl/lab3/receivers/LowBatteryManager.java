package com.inf8405.polymtl.lab3.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import com.inf8405.polymtl.lab3.managers.GlobalDataManager;


public class LowBatteryManager extends BroadcastReceiver {
    private static final String TAG = "LowBatteryManager";
    private Context _ctx;
    
    //___________________________________________________________________________________________________________________________________//
    public LowBatteryManager(Context ctx) {
        _ctx = ctx;
        readCurrentBatteryLevel();
    }
    
    //___________________________________________________________________________________________________________________________________//
    @Override
    public void onReceive(Context context, Intent intent) {
        readCurrentBatteryLevel();
    }
    
    //___________________________________________________________________________________________________________________________________//
    private void readCurrentBatteryLevel() {
        try {
            Intent _intent = _ctx.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            GlobalDataManager _gdm = (GlobalDataManager) _ctx.getApplicationContext();
            _gdm.set_battery_level(_intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1));
            _gdm.update_baterry_level_on_the_main_activity(_ctx);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }
}