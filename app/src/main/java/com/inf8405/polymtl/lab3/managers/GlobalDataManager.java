package com.inf8405.polymtl.lab3.managers;

import android.app.Application;
import android.location.Location;
import android.widget.Toast;

import com.inf8405.polymtl.lab3.entities.Artwork;
import com.inf8405.polymtl.lab3.entities.User;

import java.util.ArrayList;

/**
 * This class handles the local data and keeps it alive in between activities
 */

public class GlobalDataManager extends Application {
    private static final String TAG = "GlobalDataManager";
    
    private User _userData;
    private Location _deviceLocation;
    private DatabaseManager _dbManager;
    private ArrayList<Artwork> _artworks;
    private boolean receiversRegistered;
    private int _online_status = -1;
    private int _gps_provider_status = -1;
    private int _battery_level = -1;
    
    public GlobalDataManager() {
        _artworks = new ArrayList<>();
        receiversRegistered = false;
    }
    
    public GlobalDataManager(User _userData, DatabaseManager databaseManager) {
        this._userData = _userData;
        this._dbManager = databaseManager;
    }
    
    public boolean isReceiversRegistered() {
        return receiversRegistered;
    }
    
    public void setReceiversRegistered(boolean receiversRegistered) {
        this.receiversRegistered = receiversRegistered;
    }
    
    public Location get_deviceLocation() {
        return _deviceLocation;
    }
    
    public void set_deviceLocation(Location _deviceLocation) {
        this._deviceLocation = _deviceLocation;
        if (_deviceLocation != null) {
            String location = "Longitude: " + _deviceLocation.getLongitude() + "  ,   Latitude: " + _deviceLocation.getLatitude();
            Toast.makeText(this, location, Toast.LENGTH_SHORT);
        }
    }
    
    public ArrayList<Artwork> get_artworks() {
        return _artworks;
    }
    
    public void set_artworks(ArrayList<Artwork> _artworks) {
        this._artworks = _artworks;
    }
    
    public DatabaseManager get_dbManager() {
        return _dbManager;
    }
    
    public void set_dbManager(DatabaseManager _dbManager) {
        this._dbManager = _dbManager;
    }
    
    public int get_online_status() {
        return _online_status;
    }
    
    public void set_online_status(int _online_status) {
        this._online_status = _online_status;
    }
    
    public int getGPSProviderStatus() {
        return _gps_provider_status;
    }
    
    public void setGPSProviderStatus(int status) {
        _gps_provider_status = status;
    }
    
    public int get_battery_level() {
        return _battery_level;
    }
    
    public void set_battery_level(int _battery_level) {
        this._battery_level = _battery_level;
    }
    
    public User get_userData() {
        return _userData;
    }
    
    public void set_userData(User _userData) {
        this._userData = _userData;
    }
    
}
