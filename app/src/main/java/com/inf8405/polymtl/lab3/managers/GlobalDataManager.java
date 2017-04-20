package com.inf8405.polymtl.lab3.managers;

import android.app.Application;

import com.inf8405.polymtl.lab3.entities.User;

/**
 * This class handles the local data and keeps it alive in between activities
 */

public class GlobalDataManager extends Application {
    private User _userData;
    private DatabaseManager _dbManager;
    
    public GlobalDataManager() {
    }
    
    public GlobalDataManager(User _userData, DatabaseManager databaseManager) {
        this._userData = _userData;
    }
    
    public DatabaseManager get_dbManager() {
        return _dbManager;
    }
    
    public void set_dbManager(DatabaseManager _dbManager) {
        this._dbManager = _dbManager;
    }
    
    public User get_userData() {
        return _userData;
    }
    
    public void set_userData(User _userData) {
        this._userData = _userData;
    }
}
