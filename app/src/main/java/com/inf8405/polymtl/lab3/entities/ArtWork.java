package com.inf8405.polymtl.lab3.entities;

import android.location.Location;

public class ArtWork {
    String id;
    String name;
    Location location;
    
    public ArtWork(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
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
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
}
