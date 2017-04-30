package com.inf8405.polymtl.lab3.listeners;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.inf8405.polymtl.lab3.entities.User;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;

/**
 * Class responsible for setting the user data with a proper sync
 **/

public class UserListener implements ValueEventListener {
    private Context _ctx;
    
    public UserListener(Context _ctx) {
        this._ctx = _ctx;
    }
    
    public Context get_ctx() {
        return _ctx;
    }
    
    public void set_ctx(Context _ctx) {
        this._ctx = _ctx;
    }
    
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() != null) {
            ((GlobalDataManager) _ctx.getApplicationContext()).set_userData(dataSnapshot.getValue(User.class));
        }
    }
    
    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.w("SyncUser", "Failed to sync user ", databaseError.toException());
    }
    
}
