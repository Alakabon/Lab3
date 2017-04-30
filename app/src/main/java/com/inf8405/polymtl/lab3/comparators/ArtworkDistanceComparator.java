package com.inf8405.polymtl.lab3.comparators;

import android.location.Location;

import com.inf8405.polymtl.lab3.entities.Artwork;

import java.util.Comparator;

//TODO fill with proper comparison

/**
 * Comparator used to sort the resulting artworks by distance from the device from closest to farthest
 **/
public class ArtworkDistanceComparator implements Comparator<Artwork> {
    private static final String TAG = "ArtworkDistanceComparator";
    double deviceLocation;
    
    public ArtworkDistanceComparator(double deviceLocation) {
        this.deviceLocation = deviceLocation;
    }
    
    @Override
    public int compare(Artwork left, Artwork right) {
        Location leftLocation = new Location(TAG);
        Location rightLocation = new Location(TAG);
        
        //double distanceDeviceLeft = Location.distanceTo(deviceLocation);
        
        return 0;
    }
}
