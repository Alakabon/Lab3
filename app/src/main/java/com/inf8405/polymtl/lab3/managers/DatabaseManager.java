package com.inf8405.polymtl.lab3.managers;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inf8405.polymtl.lab3.entities.Artwork;
import com.inf8405.polymtl.lab3.entities.User;

import java.util.ArrayList;

/**
 * This class handles the communication to and from the database using firebase
 */

public class DatabaseManager {
    private static final String TAG = "DatabaseManager";
    
    private FirebaseDatabase _instance;
    private Context _ctx;
    private boolean _loggedIn;
    private ArrayList<Artwork> artworks;
    
    public DatabaseManager(Context ctx) {
        _ctx = ctx;
        _loggedIn = false;
        artworks = new ArrayList<>();
        _instance = FirebaseDatabase.getInstance();
    }
    
    public ArrayList<Artwork> getArtworks() {
        return artworks;
    }
    
    public void setArtworks(ArrayList<Artwork> artworks) {
        this.artworks = artworks;
    }
    
    public FirebaseDatabase get_instance() {
        return _instance;
    }
    
    public void set_instance(FirebaseDatabase _instance) {
        this._instance = _instance;
    }
    
    public Context get_ctx() {
        return _ctx;
    }
    
    public void set_ctx(Context _ctx) {
        this._ctx = _ctx;
    }
    
    public boolean is_loggedIn() {
        return _loggedIn;
    }
    
    public void set_loggedIn(boolean _loggedIn) {
        this._loggedIn = _loggedIn;
    }
    
    public boolean login(final String name, final String plainPassword) {
        //Retrieve user
        DatabaseReference userRef = _instance.getReference().child("root").child("users").child(name);
        
        // Read from the database
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User dbUser = dataSnapshot.getValue(User.class);
                    String dbPassword = PasswordManager.decryptPassword(dbUser.getPassword());
                    
                    if (plainPassword.equals(dbPassword)) {
                        set_loggedIn(true);
                    } else {
                        set_loggedIn(false);
                    }
                }
            }
            
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG + ":login", "Failed to find value for user " + name, error.toException());
            }
        });
        
        return _loggedIn;
    }
    
    public boolean registerUser(String name, String plainPassword) {
        
        try {
            DatabaseReference insertRef = _instance.getReference().child("root").child("users").child(name);
            
            String encryptedPassword = PasswordManager.encryptPassword(plainPassword);
            String pushId = insertRef.push().getKey();
            
            User newUser = new User(pushId, name, encryptedPassword);
            insertRef.setValue(newUser);
            
            return true;
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean addArtwork(Artwork artwork) {
        
        try {
            DatabaseReference insertRef = _instance.getReference().child("root").child("artworks").child(artwork.getName());
            
            String pushId = insertRef.push().getKey();
            artwork.setId(pushId);
            
            insertRef.setValue(artwork);
            
            return true;
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public ArrayList<Artwork> retrieveArtworks() {
        try {
            DatabaseReference userRef = _instance.getReference().child("root").child("artworks");
            final ArrayList<Artwork> tempArtworks = new ArrayList<>();
            // Read from the database
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Artwork artwork = snapshot.getValue(Artwork.class);
                            tempArtworks.add(artwork);
                        }
                        setArtworks(tempArtworks);
                    }
                }
                
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG + ":getArtw", "Failed to retrieve artworks ", error.toException());
                }
            });
            return artworks;
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return artworks;
    }
}
