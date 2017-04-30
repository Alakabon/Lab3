package com.inf8405.polymtl.lab3.activities;

import android.app.Dialog;
import android.content.Intent;
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

import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.entities.Artwork;
import com.inf8405.polymtl.lab3.fragments.ArtworkFragmentAdaptor;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;
import com.inf8405.polymtl.lab3.managers.ImageManager;

import static android.R.drawable.ic_menu_camera;


public class BrowseArtworkActivity extends AppCompatActivity {
    private final String TAG = "BrowseArtworkActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_artwork);
    
        setupListView();
    }
    
    private void setupListView() {
    
        final ListView listView = (ListView) findViewById(R.id.browse_artwork_listview);
        GlobalDataManager gdm = ((GlobalDataManager) getApplicationContext());
        
        ArtworkFragmentAdaptor adaptor = new ArtworkFragmentAdaptor(getApplicationContext(),  ((GlobalDataManager) getApplicationContext()).get_artworks());
        
        listView.setAdapter(adaptor);
        ((GlobalDataManager) getApplicationContext()).setArtworkAdaptor(adaptor);
        
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

        popupWindow.setCancelable(true);
        popupWindow.setCanceledOnTouchOutside(true);
        popupWindow.show();
    }
    
}
