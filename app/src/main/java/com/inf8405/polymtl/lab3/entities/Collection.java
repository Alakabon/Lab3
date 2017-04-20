package com.inf8405.polymtl.lab3.entities;

import java.util.HashMap;

public class Collection {
    private static final String TAG = "Collection";
    
    private String id;
    private String creatorId;
    private String description;
    private HashMap<String, String> artworkIds; // Ugly at the moment since it's a duplication but firebase hates arrays... Open to better ideas
    
    public Collection() {
    }
    
    public Collection(String id, String creatorId, String description, HashMap<String, String> artworkIds) {
        this.id = id;
        this.creatorId = creatorId;
        this.description = description;
        this.artworkIds = artworkIds;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getCreatorId() {
        return creatorId;
    }
    
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public HashMap<String, String> getArtworkIds() {
        return artworkIds;
    }
    
    public void setArtworkIds(HashMap<String, String> artworkIds) {
        this.artworkIds = artworkIds;
    }
}
