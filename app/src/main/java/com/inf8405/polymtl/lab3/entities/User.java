package com.inf8405.polymtl.lab3.entities;

import java.util.HashMap;

/**
 * Class used to store the user's info
 * The favorites are only stored on firebase, not locally
 **/

public class User {
    private static final String TAG = "User";
    
    private String id;
    private String name;
    private String password;
    private HashMap<String, String> favorites; // Ugly at the moment since it's a duplication but firebase hates arrays... Open to better ideasC
    
    public User() {
        
    }
    
    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.favorites = new HashMap<>();
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
    
    public HashMap<String, String> getFavorites() {
        if (favorites == null){
            favorites = new HashMap<>();
        }
        return favorites;
    }
    
    public void setFavorites(HashMap<String, String> favorites) {
        this.favorites = favorites;
    }
}
