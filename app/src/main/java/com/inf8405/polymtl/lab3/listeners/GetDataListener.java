package com.inf8405.polymtl.lab3.listeners;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Simple interface based on
 **/
public interface GetDataListener {
    void onSuccess(DataSnapshot dataSnapshot);
    void onFailed(DatabaseError databaseError);
}
