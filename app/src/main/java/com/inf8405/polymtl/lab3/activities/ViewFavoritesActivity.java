package com.inf8405.polymtl.lab3.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.entities.Artwork;
import com.inf8405.polymtl.lab3.fragments.ArtworkFragmentAdaptor;
import com.inf8405.polymtl.lab3.managers.DatabaseManager;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;
import com.inf8405.polymtl.lab3.managers.ImageManager;

import static android.R.drawable.ic_menu_camera;

/**
 * Similar display to Browse artwork so it re-uses that layout with a different list of artworks.
 **/
public class ViewFavoritesActivity extends AppCompatActivity {
    private final String TAG = "ViewFavoritesActivity";
    private ArtworkFragmentAdaptor adaptor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_artwork);
        
        setupListView();
        
    }
    
    private void setupListView() {
        
        final ListView listView = (ListView) findViewById(R.id.browse_artwork_listview);
        GlobalDataManager gdm = ((GlobalDataManager) getApplicationContext());
        
        adaptor = new ArtworkFragmentAdaptor(getApplicationContext(), ((GlobalDataManager) getApplicationContext()).get_favorites());
        
        listView.setAdapter(adaptor);
        ((GlobalDataManager) getApplicationContext()).setFavoritesAdaptor(adaptor);
        
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                showInfoPopup((Artwork) listView.getAdapter().getItem(pos));
            }
        });
    }
    
    private void showInfoPopup(final Artwork artwork) {
        Dialog popupWindow = new Dialog(getWindow().getContext());
        popupWindow.setContentView(R.layout.popup_artwork_info_layout);
        
        // Setting fields
        TextView name = (TextView) popupWindow.findViewById(R.id.popup_artwork_info_name);
        name.setText(artwork.getName());
        
        TextView description = (TextView) popupWindow.findViewById(R.id.popup_artwork_info_description);
        description.setText(artwork.getDescription());
        
        TextView distance = (TextView) popupWindow.findViewById(R.id.popup_artwork_info_distance);
        Location artworkLocation = new Location(TAG);
        artworkLocation.setLatitude(artwork.getGpsX());
        artworkLocation.setLongitude(artwork.getGpsY());
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
        
        final String facebookURL = "https://www.facebook.com/sharer/sharer.php?u=http%3A//www.google.com/maps/place/," + artwork.getGpsY().toString() + "," + artwork.getGpsX().toString();
        ImageButton facebook = ((ImageButton) popupWindow.findViewById(R.id.popup_artwork_info_imageButton));
        facebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookURL));
                startActivity(browserIntent);
            }
        });
        
        final String twitterURL = "https://twitter.com/home?status=http%3A//www.google.com/maps/place/," + artwork.getGpsY().toString() + "," + artwork.getGpsX().toString();
        ImageButton twitter = ((ImageButton) popupWindow.findViewById(R.id.popup_artwork_twitter_imageButton));
        twitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterURL));
                startActivity(browserIntent);
            }
        });
        
        final ImageButton favorite = ((ImageButton) popupWindow.findViewById(R.id.popup_artwork_favorite_imageButton));
        favorite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatabaseManager dbManager = ((GlobalDataManager) getApplicationContext()).get_dbManager();
                if (dbManager.removeFromFavorites(artwork)) {
                    ((GlobalDataManager) getApplicationContext()).get_favorites().remove(artwork);
                    adjustFavoritesIcon(favorite);
                    adaptor.refresh();
                    Toast.makeText(getApplicationContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                }
            }
            
        });
        
        
        popupWindow.setCancelable(true);
        popupWindow.setCanceledOnTouchOutside(true);
        popupWindow.show();
    }
    
    private void adjustFavoritesIcon(final ImageButton favorite) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            favorite.setImageDrawable(getApplicationContext().getDrawable(R.drawable.favorite_off));
        } else {
            favorite.setImageDrawable(getResources().getDrawable(R.drawable.favorite_off));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            Toast.makeText(this, getResources().getString(R.string.orientation_msg), Toast.LENGTH_LONG).show();
    }
}
