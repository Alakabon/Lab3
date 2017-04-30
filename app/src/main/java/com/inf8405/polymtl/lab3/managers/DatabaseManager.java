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
import com.inf8405.polymtl.lab3.entities.Museum;
import com.inf8405.polymtl.lab3.entities.User;
import com.inf8405.polymtl.lab3.listeners.LoginListener;
import com.inf8405.polymtl.lab3.listeners.UserListener;

import java.util.ArrayList;

/**
 * This class handles the communication to and from the database using firebase
 */

public class DatabaseManager {
    private static final String TAG = "DatabaseManager";
    
    private FirebaseDatabase _instance;
    private ValueEventListener userListener;
    private Context _ctx;
    private boolean _loggedIn;
    
    
    public DatabaseManager(Context ctx) {
        _ctx = ctx;
        _loggedIn = false;
        _instance = FirebaseDatabase.getInstance();
        userListener = new UserListener(_ctx);
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
    
    public boolean login(final LoginListener loginListener) {
        //loginListener.onStart(); Nothing for the moment
        //Retrieve user
        DatabaseReference userRef = _instance.getReference().child("root").child("users").child(loginListener.getUserName());
        
        // Read from the database
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    loginListener.onSuccess(dataSnapshot);
                } else {
                    loginListener.onFailed(null);
                }
            }
            
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                loginListener.onFailed(error);
                _loggedIn = false;
                Log.w(TAG + ":login", "Failed to find value for user " + loginListener.getUserName(), error.toException());
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
    
    public boolean addMuseum(Museum museum) {
        
        try {
            DatabaseReference insertRef = _instance.getReference().child("root").child("museums").child(museum.getName());
            
            String pushId = insertRef.push().getKey();
            museum.setId(pushId);
            
            insertRef.setValue(museum);
            
            return true;
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void retrieveArtworks() {
        try {
            DatabaseReference userRef = _instance.getReference().child("root").child("artworks");
            // Read from the database
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //getDataListener.onSuccess(dataSnapshot);
                    if (dataSnapshot.getValue() != null) {
                        final ArrayList<Artwork> artworks = new ArrayList<>();
                        
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Artwork artwork = snapshot.getValue(Artwork.class);
                            artworks.add(artwork);
                        }
                        ((GlobalDataManager) _ctx.getApplicationContext()).set_artworks(artworks);
                    }
                }
                
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG + ":getArtw", "Failed to retrieve artworks ", error.toException());
                }
            });
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
    
    public void retrieveMuseums() {
        try {
            DatabaseReference userRef = _instance.getReference().child("root").child("museums");
            // Read from the database
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //getDataListener.onSuccess(dataSnapshot);
                    if (dataSnapshot.getValue() != null) {
                        final ArrayList<Museum> museums = new ArrayList<>();
                        
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Museum museum = snapshot.getValue(Museum.class);
                            museums.add(museum);
                        }
                        
                        ((GlobalDataManager) _ctx.getApplicationContext()).set_museums(museums);
                    }
                }
                
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG + ":getArtw", "Failed to retrieve artworks ", error.toException());
                }
            });
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
    
    public void syncUser(User dbUser) {
        if (_loggedIn) {
            try {
                DatabaseReference userRef = _instance.getReference().child("root").child("users").child(dbUser.getName());
                userRef.addValueEventListener(userListener);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void logOff() {
        if (_loggedIn) {
            try {
                User currentUser = ((GlobalDataManager) _ctx).get_userData();
                DatabaseReference userRef = _instance.getReference().child("root").child("users").child(currentUser.getName());
                userRef.removeEventListener(userListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
