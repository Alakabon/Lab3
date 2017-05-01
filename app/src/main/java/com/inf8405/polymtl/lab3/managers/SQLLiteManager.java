package com.inf8405.polymtl.lab3.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.inf8405.polymtl.lab3.entities.User;

/**
 * Class in charge of handling the local db storage
 */

public class SQLLiteManager extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "INF8405";
    // Contacts table name
    private static final String TABLE_PREF = "PREF";
    // PREF Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "username";
    private static final String KEY_PASS = "password";
    
    public SQLLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PREF + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_PASS + " TEXT" + ")");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREF);
        
        // Creating tables again
        onCreate(db);
    }
    
    // Adding new user
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_PASS, user.getPassword());
        
        // Inserting Row and Closing database connection
        db.insert(TABLE_PREF, null, values);
        db.close();
    }
    
    // Getting one user
    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PREF + " WHERE " + KEY_ID + "='" + id + "'", null);
        User user = null;
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0)
                user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            cursor.close();
        } else {
            user = null;
        }
        
        return user;
    }
    
    // Getting one user
    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PREF + " WHERE " + KEY_NAME + "='" + username + "'", null);
        User user = null;
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0)
                user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            cursor.close();
        }
        
        return user;
    }
    
    // Getting users Count
    public int getUsersCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PREF, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }
    
    // Updating a user
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_PASS, user.getPassword());
        // updating row
        return db.update(TABLE_PREF, values, KEY_ID + "=?", new String[]{user.getId()});
    }
    
    // Deleting a user
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PREF, KEY_ID + "=?", new String[]{user.getId()});
        db.close();
    }
}
