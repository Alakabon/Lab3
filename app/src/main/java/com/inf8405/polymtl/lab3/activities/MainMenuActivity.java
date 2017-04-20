package com.inf8405.polymtl.lab3.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.inf8405.polymtl.lab3.R;

public class MainMenuActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    
        Button addArtworkBtn = (Button) findViewById(R.id.main_menu_btn_add_artwork);
        addArtworkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddArtworkActivity.class);
                startActivity(intent);
            }
        });
    
        Button browseArtworkBtn = (Button) findViewById(R.id.main_menu_btn_browse_artwork);
        browseArtworkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),BrowseArtworkActivity.class);
                startActivity(intent);
            }
        });
    
        Button viewFavoritesBtn = (Button) findViewById(R.id.main_menu_btn_view_favorite);
        viewFavoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ViewFavoritesActivity.class);
                startActivity(intent);
            }
        });
        
    }
}
