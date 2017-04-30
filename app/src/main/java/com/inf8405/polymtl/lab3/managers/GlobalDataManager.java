package com.inf8405.polymtl.lab3.managers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.entities.Artwork;
import com.inf8405.polymtl.lab3.entities.Museum;
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
    private ArrayList<Museum> _museums;
    private ListAdapter artworkAdaptor; // Link to adaptor needed to refresh data by GPS
    private boolean receiversRegistered;
    private int _online_status = -1;
    private int _gps_provider_status = -1;
    private int _battery_level = -1;
    private double _gps_Longitude;
    private double _gps_Latitude;
    
    public GlobalDataManager() {
        _artworks = new ArrayList<>();
        _museums = new ArrayList<>();
        receiversRegistered = false;
    }
    
    public GlobalDataManager(User _userData, DatabaseManager databaseManager) {
        this._userData = _userData;
        this._dbManager = databaseManager;
    }
    
    public ListAdapter getArtworkAdaptor() {
        return artworkAdaptor;
    }
    
    public void setArtworkAdaptor(ListAdapter artworkAdaptor) {
        this.artworkAdaptor = artworkAdaptor;
    }
    
    public ArrayList<Museum> get_museums() {
        return _museums;
    }
    
    public void set_museums(ArrayList<Museum> _museums) {
        this._museums = _museums;
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
            _gps_Latitude = _deviceLocation.getLatitude();
            _gps_Longitude = _deviceLocation.getLongitude();
            if (artworkAdaptor != null) {
                //Collections.sort(_artworks, new ArtworkDistanceComparator(_deviceLocation));
                ((BaseAdapter) artworkAdaptor).notifyDataSetChanged();
            }
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
    
    public CharSequence get_online_status_string() {
        return (_online_status == 1) ? "Online" : (_online_status == 0) ? "Offline" : "Error reading Internet status!";
    }
    
    public CharSequence get_battery_level_string() {
        return String.valueOf(_battery_level).concat("%");
    }
    
    public CharSequence get_GPS_provider_status_string() {
        return (_gps_provider_status == 1) ? "On" : (_gps_provider_status == 0) ? "Off" : "Error reading GPS data!";
    }
    
    public CharSequence get_GPS_latitude_string() {
        return String.valueOf(_gps_Latitude);
    }
    
    public CharSequence get_GPS_longitude_string() {
        return String.valueOf(_gps_Longitude);
    }
    
    public void set_GPS_latitude(double latitude) {
        _gps_Latitude = latitude;
    }
    
    public void set_GPS_longitude(double longitude) {
        _gps_Longitude = longitude;
    }
    
    public void update_online_status_on_the_main_activity(Context ctx) {
        Activity _act = (Activity) ctx;
        ((TextView) _act.findViewById(R.id.txt1)).setText(get_online_status_string());
        ((ImageView) _act.findViewById(R.id.img1)).setImageResource((_online_status == 1) ? R.drawable.online : (_online_status == 0) ? R.drawable.offline : R.drawable.error);
    }
    
    public void update_baterry_level_on_the_main_activity(Context ctx) {
        Activity _act = (Activity) ctx;
        ((TextView) _act.findViewById(R.id.txt2)).setText(get_battery_level_string());
    }
    
    public void update_GPS_status_on_the_main_activity(Context ctx) {
        Activity _act = (Activity) ctx;
        ((TextView) _act.findViewById(R.id.txt5)).setText(get_GPS_provider_status_string());
        ((ImageView) _act.findViewById(R.id.img5)).setImageResource((_gps_provider_status == 1) ? R.drawable.on : (_gps_provider_status == 0) ? R.drawable.off : R.drawable.error);
        ((TextView) _act.findViewById(R.id.txt3)).setText(get_GPS_latitude_string());
        ((TextView) _act.findViewById(R.id.txt4)).setText(get_GPS_longitude_string());
    }
}
