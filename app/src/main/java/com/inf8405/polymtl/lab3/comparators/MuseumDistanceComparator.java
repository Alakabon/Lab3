package com.inf8405.polymtl.lab3.comparators;

import com.inf8405.polymtl.lab3.entities.Museum;

import java.util.Comparator;


/**
 * Comparator used to sort the resulting museums by distance from the device from closest to farthest
 * Did not end up being used due to lack of time
 **/
@Deprecated
public class MuseumDistanceComparator implements Comparator<Museum> {
    
    public MuseumDistanceComparator() {
    }
    
    @Override
    public int compare(Museum o1, Museum o2) {
        return 0;
    }
}
