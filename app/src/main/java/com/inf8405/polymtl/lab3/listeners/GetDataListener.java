package com.inf8405.polymtl.lab3.listeners;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public interface GetDataListener {
    public void onStart();
    public void onSuccess(DataSnapshot data);
    public void onFailed(DatabaseError databaseError);
}
