package com.inf8405.polymtl.lab3.entities;

import android.location.Location;
import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.HashMap;

public class Artwork {
    private static final String TAG = "Artwork";
    
    private String id;
    private String name;
    private String description;
    private String photoURL;
    private Double gpsX;
    private Double gpsY;
    private HashMap<String, Integer> ratings;
    private Double averageRating;
    private HashMap<String, String> comments;
    
    public Artwork() {
    }
    
    public Artwork(String name, String description, String photoURL) {
        this.name = name;
        this.description = description;
        this.photoURL = photoURL;
    }
    
    public Artwork(String id, String name, String description, String photoURL, Double gpsX, Double gpsY, HashMap<String, Integer> ratings, Double averageRating, HashMap<String, String> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photoURL = photoURL;
        this.gpsX = gpsX;
        this.gpsY = gpsY;
        this.ratings = ratings;
        this.averageRating = averageRating;
        this.comments = comments;
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
    
    public HashMap<String, Integer> getRatings() {
        return ratings;
    }
    
    public void setRatings(HashMap<String, Integer> ratings) {
        this.ratings = ratings;
    }
    
    public Double getAverageRating() {
        return averageRating;
    }
    
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
    
    public HashMap<String, String> getComments() {
        return comments;
    }
    
    public void setComments(HashMap<String, String> comments) {
        this.comments = comments;
    }
    
    @Exclude
    public float calculateAverageRating() {
        int ratingCount = 0;
        float sumRatings = 0;
        int currentRating;
        
        for (String s : ratings.keySet()) {
            // Make sure rating is not null;
            currentRating = ratings.get(s);
            if (!ratings.get(s).equals(null)) {
                sumRatings += currentRating;
                ratingCount++;
            } else {
                Log.d(TAG, "A null value was inserted in place of a rating, please be careful");
                return -1;
            }
        }
        if (ratingCount > 0) {
            return sumRatings / ratingCount;
        } else {
            return 0;
        }
    }
}
