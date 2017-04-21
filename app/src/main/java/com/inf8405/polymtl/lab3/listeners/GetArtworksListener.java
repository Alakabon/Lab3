package com.inf8405.polymtl.lab3.listeners;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.entities.Artwork;
import com.inf8405.polymtl.lab3.fragments.ArtworkFragmentAdaptor;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;

import java.util.ArrayList;

public class GetArtworksListener implements GetDataListener {
    private static final String TAG = "GetArtworksListner";
    
    private Context ctx;
    private Activity browseActivity;
    
    public GetArtworksListener(Context ctx, Activity browseActivity) {
        this.ctx = ctx;
        this.browseActivity = browseActivity;
    }
    
    public Activity getBrowseActivity() {
        return browseActivity;
    }
    
    public void setBrowseActivity(Activity browseActivity) {
        this.browseActivity = browseActivity;
    }
    
    public Context getCtx() {
        return ctx;
    }
    
    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }
    
    @Override
    public void onStart() {
        
    }
    
    @Override
    public void onSuccess(DataSnapshot dataSnapshot) {
        ArrayList<Artwork> artworks = new ArrayList<>();
        
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Artwork artwork = snapshot.getValue(Artwork.class);
            artworks.add(artwork);
        }
        
        ((GlobalDataManager) ctx.getApplicationContext()).set_artworks(artworks);
        
        ListView listView = (ListView) browseActivity.findViewById(R.id.browse_artwork_listview);
        listView.setAdapter(new ArtworkFragmentAdaptor(browseActivity.getApplicationContext(), artworks));
    }
    
    @Override
    public void onFailed(DatabaseError databaseError) {
        Log.w(TAG, "Failed to retrieve artworks ", databaseError.toException());
    }
}
