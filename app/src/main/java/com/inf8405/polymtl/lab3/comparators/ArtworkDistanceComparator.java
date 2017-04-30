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
    Location deviceLocation;
    
    public ArtworkDistanceComparator(Location deviceLocation) {
        this.deviceLocation = deviceLocation;
    }
    
    @Override
    public int compare(Artwork left, Artwork right) {
        Location leftLocation = new Location(TAG);
        Location rightLocation = new Location(TAG);

        leftLocation.setLatitude(left.getGpsY());
        leftLocation.setLongitude(left.getGpsX());
        rightLocation.setLatitude(left.getGpsY());
        rightLocation.setLongitude(left.getGpsX());
        
        float distanceDeviceLeft = deviceLocation.distanceTo(leftLocation);
        float distanceDeviceRight = deviceLocation.distanceTo(rightLocation);
        
        if(distanceDeviceLeft > distanceDeviceRight){
            return 1;
        }
        else if(distanceDeviceLeft < distanceDeviceRight ){
            return -1;
        }
        else{
            return 0;
        }
    }
}
