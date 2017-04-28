package com.inf8405.polymtl.lab3.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.listeners.GetArtworksListener;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;


public class BrowseArtworkActivity extends AppCompatActivity{
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_artwork);
        
        GetArtworksListener getArtworksListener = new GetArtworksListener(getApplicationContext(),this);
        
        ((GlobalDataManager)getApplicationContext()).get_dbManager().retrieveArtworks(getArtworksListener);
        
        Toast.makeText(getApplicationContext(),"You are now in Browse Artwork",Toast.LENGTH_LONG).show();
    }
   
}
