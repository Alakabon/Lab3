package com.inf8405.polymtl.lab3.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.entities.Artwork;
import com.inf8405.polymtl.lab3.entities.Museum;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;
import com.inf8405.polymtl.lab3.utilities.PermissionUtils;


/**
 * Activitée en charge l'affichage de la carte
 */

public class ViewMapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        OnMyLocationButtonClickListener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GlobalDataManager _gdm;
    private GoogleMap _map;
    private boolean _permissionDenied = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);
        _gdm = ((GlobalDataManager) getApplicationContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        _map = googleMap;
        _map.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        //  Sets the current camera position to Polytechnique and the zoom to street view
        LatLng poly = new LatLng(45.5045044, -73.6128185);
        _map.moveCamera(CameraUpdateFactory.newLatLngZoom(poly, 15));
        
        // Listeners for changes to the database so the app can update map markers
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    updateMarkers();
                }
            }
            
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    
    //Enables the My Location layer if the fine location permission has been granted.
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (_map != null) {
            // Access to the location has been granted to the app.
            _map.setMyLocationEnabled(true);
        }
    }
    
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Centering the map to current device location", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }
    
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (_permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            _permissionDenied = false;
        }
    }
    
    // Displays a dialog with error message explaining that the location permission is missing
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }
    
    private void updateMarkers() {
        _map.clear();
        // Populating artworks' markers on the map
        for (Artwork artwork : _gdm.get_artworks()) {
            _map.addMarker(new MarkerOptions()
                    .position(new LatLng(artwork.getGpsX(), artwork.getGpsY()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .title(artwork.getName())
                    .snippet(artwork.getDescription()));
        }
        // Populating museums' markers on the map
        for (Museum museum : _gdm.get_museums()) {
            _map.addMarker(new MarkerOptions()
                    .position(new LatLng(museum.getGpsX(), museum.getGpsY()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .title(museum.getName())
                    .snippet(museum.getDescription()));
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
