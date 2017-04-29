package com.inf8405.polymtl.lab3.activities;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory ;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.FirebaseDatabase;
import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;
import com.inf8405.polymtl.lab3.utilities.PermissionUtils;

import android.os.Bundle;
import android.Manifest;
import android.support.annotation.NonNull;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ViewMapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        OnMyLocationButtonClickListener
{
    private GlobalDataManager _gdm;
    private GoogleMap _map;
    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);
        _gdm = ((GlobalDataManager) getApplicationContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap)
    {
        _map = googleMap;
        //googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

        //@Override
        //public void onMapLongClick(LatLng point) {
            //addPlaceMarker(_map, point.latitude, point.longitude, "Nom", "Photo");
        //}
        //});
        //_map.setOnInfoWindowClickListener(this);
        _map.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(_gdm.get_deviceLocation().getLatitude(), _gdm.get_deviceLocation().getLongitude()));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        _map.moveCamera(center);
        _map.animateCamera(zoom);
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
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
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }
    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    private void updateDeviceMarker()
    {
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(_gdm.get_deviceLocation().getLatitude(), _gdm.get_deviceLocation().getLongitude()));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        _map.addMarker(new MarkerOptions()
                .position(new LatLng(_gdm.get_deviceLocation().getLatitude(), _gdm.get_deviceLocation().getLatitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title("Your current position"));
        _map.moveCamera(center);
        _map.animateCamera(zoom);
    }


}
