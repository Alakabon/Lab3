package com.inf8405.polymtl.lab3.entities;

/**
 * Class used to store info on Museums
 * Very similar to
 *
 * @see Artwork in it's content  but has an adress string and no ratings
 **/
public class Museum {
    
    private String id;
    private String name;
    private String address;
    private String description;
    private String photoURL;
    private Double gpsX;
    private Double gpsY;
    
    public Museum() {
    }
    
    public Museum(String id, String name, String address, String description, String photoURL, Double gpsX, Double gpsY) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.photoURL = photoURL;
        this.gpsX = gpsX;
        this.gpsY = gpsY;
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
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getPhotoURL() {
        return photoURL;
    }
    
    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
    
    public Double getGpsX() {
        return gpsX;
    }
    
    public void setGpsX(Double gpsX) {
        this.gpsX = gpsX;
    }
    
    public Double getGpsY() {
        return gpsY;
    }
    
    public void setGpsY(Double gpsY) {
        this.gpsY = gpsY;
    }
}

