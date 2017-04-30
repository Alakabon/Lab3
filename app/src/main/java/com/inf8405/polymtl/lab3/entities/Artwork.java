package com.inf8405.polymtl.lab3.entities;

/**
 * Class used to store the info on Artworks, the bread and butter the app revolves around
 * Location is kept as 2 doubles to simplify the firebase extraction
 **/
public class Artwork {
    private static final String TAG = "Artwork";
    
    private String id;
    private String name;
    private String description;
    private String photoURL;
    private Double gpsX;
    private Double gpsY;
    
    public Artwork() {
    }
    
    public Artwork(String name, String description, String photoURL) {
        this.name = name;
        this.description = description;
        this.photoURL = photoURL;
    }
    
    public Artwork(String id, String name, String description, String photoURL, Double gpsX, Double gpsY) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photoURL = photoURL;
        this.gpsX = gpsX;
        this.gpsY = gpsY;
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
}
