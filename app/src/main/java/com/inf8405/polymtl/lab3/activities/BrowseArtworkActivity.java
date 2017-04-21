package com.inf8405.polymtl.lab3.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.entities.Artwork;
import com.inf8405.polymtl.lab3.fragments.ArtworkFragmentAdaptor;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;

import java.util.ArrayList;

public class BrowseArtworkActivity extends AppCompatActivity{
    
    private ArrayList<Artwork> artworks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_artwork);
        
        artworks = ((GlobalDataManager)getApplicationContext()).get_dbManager().retrieveArtworks();
        
        if (artworks == null){
            artworks = new ArrayList<>();
            Artwork testArtwork1 = new Artwork("TestName 1","TestDescription1","no_photo");
            Artwork testArtwork2 = new Artwork("TestName 2","TestDescription2","no_photo");
            artworks.add(testArtwork1);
            artworks.add(testArtwork2);
        }
        
        ListView listView = (ListView) findViewById(R.id.browse_artwork_listview);
        listView.setAdapter(new ArtworkFragmentAdaptor(this, artworks));
        Toast.makeText(getApplicationContext(),"You are now in Browse Artwork",Toast.LENGTH_LONG).show();
    }
    
}
