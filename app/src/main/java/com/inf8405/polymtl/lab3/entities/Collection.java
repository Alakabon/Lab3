package com.inf8405.polymtl.lab3.entities;

import java.util.HashMap;

/**
 * Class needed to retrieve the data of the user's favorites correctly
 * It's a simple Hashmap with the artworks's id and name
 * Is only contained in
 * @see User
 **/

public class Collection {
    private static final String TAG = "Collection";
    
    private HashMap<String, String> artworkIds; // Ugly at the moment since it's a duplication but firebase hates arrays... Open to better ideas
    
    public Collection() {
    }
    
    public Collection(HashMap<String, String> artworkIds) {
        this.artworkIds = artworkIds;
    }
    
    public HashMap<String, String> getArtworkIds() {
        return artworkIds;
    }
    
    public void setArtworkIds(HashMap<String, String> artworkIds) {
        this.artworkIds = artworkIds;
    }
}
