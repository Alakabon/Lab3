package com.inf8405.polymtl.lab3.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.inf8405.polymtl.lab3.R;

public class BrowseArtworkActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_artwork);
    
        Toast.makeText(getApplicationContext(),"You are now in Browse Artwork",Toast.LENGTH_LONG).show();
    }
}