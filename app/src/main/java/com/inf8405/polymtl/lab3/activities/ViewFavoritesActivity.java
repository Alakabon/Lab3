package com.inf8405.polymtl.lab3.activities;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.entities.Artwork;
import com.inf8405.polymtl.lab3.entities.User;
import com.inf8405.polymtl.lab3.fragments.ArtworkFragmentAdaptor;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;
import com.inf8405.polymtl.lab3.managers.ImageManager;

import static android.R.drawable.ic_menu_camera;

/**
 * Similar display to Browse artwork so it re-uses that layout with a different list of artworks.
 **/
public class ViewFavoritesActivity extends AppCompatActivity {
    private final String TAG = "ViewFavoritesActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_artwork);
        
        setupListView();
        
        /* Debug adder to make sure stuff works
        User user = ((GlobalDataManager) getApplicationContext()).get_userData();
        Artwork aw1 = ((GlobalDataManager) getApplicationContext()).get_artworks().get(0);
        Artwork aw2 = ((GlobalDataManager) getApplicationContext()).get_artworks().get(1);
        Artwork aw3 = ((GlobalDataManager) getApplicationContext()).get_artworks().get(2);
        
        ((GlobalDataManager) getApplicationContext()).get_dbManager().addToFavorites(user, aw1);
        ((GlobalDataManager) getApplicationContext()).get_dbManager().addToFavorites(user, aw2);
        ((GlobalDataManager) getApplicationContext()).get_dbManager().addToFavorites(user, aw3);
        
        ((GlobalDataManager) getApplicationContext()).get_dbManager().removeFromFavorites(user, aw3);
        */
    }
    
    private void setupListView() {
        
        final ListView listView = (ListView) findViewById(R.id.browse_artwork_listview);
        GlobalDataManager gdm = ((GlobalDataManager) getApplicationContext());
        
        ArtworkFragmentAdaptor adaptor = new ArtworkFragmentAdaptor(getApplicationContext(), ((GlobalDataManager) getApplicationContext()).get_favorites());
        
        listView.setAdapter(adaptor);
        ((GlobalDataManager) getApplicationContext()).setFavoritesAdaptor(adaptor);
        
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                showInfoPopup((Artwork) listView.getAdapter().getItem(pos));
            }
        });
    }
    
    private void showInfoPopup(Artwork artwork) {
        Dialog popupWindow = new Dialog(getWindow().getContext());
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
        Location currentLocation = ((GlobalDataManager) (getApplicationContext())).get_deviceLocation();
        float fDistance = currentLocation.distanceTo(artworkLocation) / 1000; //Distance in meters, divide by 1000 to get KM
        
        distance.setText(String.format(java.util.Locale.US, " Distance de %.2f KM", fDistance));
        
        Bitmap decodedPhoto = ImageManager.decodeImageFromString(artwork.getPhotoURL());
        ImageView photo = (ImageView) popupWindow.findViewById(R.id.popup_artwork_info_image);
        
        if (decodedPhoto != null) {
            photo.setImageBitmap(decodedPhoto);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                photo.setImageDrawable(getApplicationContext().getDrawable(ic_menu_camera));
            } else {
                photo.setImageDrawable(getApplicationContext().getResources().getDrawable(ic_menu_camera));
            }
        }
        
        popupWindow.setCancelable(true);
        popupWindow.setCanceledOnTouchOutside(true);
        popupWindow.show();
    }
}
