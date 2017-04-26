package com.inf8405.polymtl.lab3.activities;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.FirebaseDatabase;
import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ViewMapActivity extends AppCompatActivity implements
        OnMapReadyCallback
{
    private GlobalDataManager _gdm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);
        _gdm = ((GlobalDataManager) getApplicationContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //if (mapFragment == null)
        //{
            //mapFragment = SupportMapFragment.newInstance();
            //fm.beginTransaction().replace(R.id.map, mapFragment).commit();
        //}
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap)
    {
    //_map = googleMap;
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

        @Override
        public void onMapLongClick(LatLng point) {
            //addPlaceMarker(_map, point.latitude, point.longitude, "Nom", "Photo");
        }
        });
        //_map.setOnInfoWindowClickListener(this);
        //CameraUpdate center =CameraUpdateFactory.newLatLng(new LatLng(_gdm.getGPSLatitude(), _gdm.getGPSLongitude()));
        //CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        //addPersonMarker(_map, gdm.getGPSLatitude(), gdm.getGPSLongitude(), gdm.getUserData().getName());
        //googleMap.moveCamera(center);
        //googleMap.animateCamera(zoom);
    }
}
