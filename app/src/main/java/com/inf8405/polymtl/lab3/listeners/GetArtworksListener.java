package com.inf8405.polymtl.lab3.listeners;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.entities.Artwork;
import com.inf8405.polymtl.lab3.fragments.ArtworkFragmentAdaptor;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;
import com.inf8405.polymtl.lab3.managers.ImageManager;

import java.util.ArrayList;

import static android.R.drawable.ic_menu_camera;

/**
 * Listener used to make sure the data retrieval is synced with the app's state
 * The onSuccess method is responsible for updating the local list of artworks and
 * applying the FragmentAdaptor to set the right fields in the right place
 *
 * Also responsible for the setup of the pop-ups once the list established by the use of
 * setOnItemClickListener
 **/
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
    public void onSuccess(DataSnapshot dataSnapshot) {
        final ArrayList<Artwork> artworks = new ArrayList<>();
        
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Artwork artwork = snapshot.getValue(Artwork.class);
            artworks.add(artwork);
        }
        
        ((GlobalDataManager) ctx.getApplicationContext()).set_artworks(artworks);
        
        final ListView listView = (ListView) browseActivity.findViewById(R.id.browse_artwork_listview);
        listView.setAdapter(new ArtworkFragmentAdaptor(browseActivity.getApplicationContext(), artworks));
        ((GlobalDataManager) ctx.getApplicationContext()).setArtworkAdaptor(listView.getAdapter());
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                showInfoPopup((Artwork) listView.getAdapter().getItem(pos));
            }
        });
    }
    
    @Override
    public void onFailed(DatabaseError databaseError) {
        Log.w(TAG, "Failed to retrieve artworks ", databaseError.toException());
    }
    
    private void showInfoPopup(Artwork artwork) {
        Dialog popupWindow = new Dialog(browseActivity.getWindow().getContext());
        popupWindow.setContentView(R.layout.popup_artwork_info_layout);
        
        // Setting fields
        TextView name = (TextView) popupWindow.findViewById(R.id.popup_artwork_info_name);
        name.setText(artwork.getName());
        
        TextView description = (TextView) popupWindow.findViewById(R.id.popup_artwork_info_description);
        description.setText(artwork.getDescription());
        
        TextView distance = (TextView) popupWindow.findViewById(R.id.popup_artwork_info_distance);
        Location artworkLocation = new Location(TAG);
        artworkLocation.setLatitude(artwork.getGpsY());
        artworkLocation.setLongitude(artwork.getGpsX());
        Location currentLocation = ((GlobalDataManager) (ctx.getApplicationContext())).get_deviceLocation();
        float fDistance = currentLocation.distanceTo(artworkLocation)/1000; //Distance in meters, divide by 1000 to get KM
        
        distance.setText(String.format(" Distance de %.2f KM", fDistance));
        
        Bitmap decodedPhoto = ImageManager.decodeImageFromString(artwork.getPhotoURL());
        ImageView photo = (ImageView) popupWindow.findViewById(R.id.popup_artwork_info_image);
        
        if (decodedPhoto != null) {
            photo.setImageBitmap(decodedPhoto);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                photo.setImageDrawable(browseActivity.getApplicationContext().getDrawable(ic_menu_camera));
            } else {
                photo.setImageDrawable(browseActivity.getApplicationContext().getResources().getDrawable(ic_menu_camera));
            }
        }
        
        popupWindow.setCancelable(true);
        popupWindow.setCanceledOnTouchOutside(true);
        popupWindow.show();
    }
}
