package com.inf8405.polymtl.lab3.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        ImageButton addArtworkBtn = (ImageButton) findViewById(R.id.main_menu_btn_add_artwork);
        addArtworkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddArtworkActivity.class);
                startActivity(intent);
            }
        });

        ImageButton addMuseumBtn = (ImageButton) findViewById(R.id.main_menu_btn_add_museum);
        addMuseumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddMuseumActivity.class);
                startActivity(intent);
            }
        });

        ImageButton browseMuseumBtn = (ImageButton) findViewById(R.id.main_menu_btn_browse_museums);
        browseMuseumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BrowseMuseumActivity.class);
                startActivity(intent);
            }
        });

        ImageButton browseArtworkBtn = (ImageButton) findViewById(R.id.main_menu_btn_browse_artwork);
        browseArtworkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BrowseArtworkActivity.class);
                startActivity(intent);
            }
        });

        ImageButton viewFavoritesBtn = (ImageButton) findViewById(R.id.main_menu_btn_view_favorite);
        viewFavoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewFavoritesActivity.class);
                startActivity(intent);
            }
        });

        ImageButton viewMapBtn = (ImageButton) findViewById(R.id.main_menu_btn_view_map);
        viewMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewMapActivity.class);
                startActivity(intent);
            }
        });

        // Sync Data to mobile and populate artworks and museums
        ((GlobalDataManager) getApplicationContext()).get_dbManager().retrieveArtworks();
        ((GlobalDataManager) getApplicationContext()).get_dbManager().retrieveMuseums();
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
//            Toast.makeText(this, getResources().getString(R.string.orientation_msg), Toast.LENGTH_LONG).show();
//    }
}
