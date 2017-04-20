package com.inf8405.polymtl.lab3.entities;

import android.location.Location;

public class User {
    private static final String TAG = "User";
    
    private String id;
    private String name;
    private String password;
    private Location location;
    private Collection favorites;
    
    public User() {
        
    }
    
    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        //TODO add current location
        this.favorites = null;
        this.location = null;
    }
    
    public User(String id, String name, String password, Location location, Collection favorites) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.location = location;
        this.favorites = favorites;
    }
    

    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    public Collection getFavorites() {
        return favorites;
    }
    
    public void setFavorites(Collection favorites) {
        this.favorites = favorites;
    }
}
