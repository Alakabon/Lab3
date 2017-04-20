package com.inf8405.polymtl.lab3.activities;

import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.entities.Artwork;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;
import com.inf8405.polymtl.lab3.managers.ImageManager;

import static android.R.drawable.ic_menu_camera;

public class AddArtworkActivity extends AppCompatActivity {
    
    private GlobalDataManager _gdm;
    private Location location;
    
    public GlobalDataManager get_gdm() {
        return _gdm;
    }
    
    public void set_gdm(GlobalDataManager _gdm) {
        this._gdm = _gdm;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_artwork);
        
        _gdm = ((GlobalDataManager) this.getApplicationContext());
        location = new Location(this.getCallingPackage());
        
        Toast.makeText(getApplicationContext(), "You are now in Add Artwork", Toast.LENGTH_LONG).show();
        
        Button photoBtnFromCamera = (Button) findViewById(R.id.add_artwork_btn_photo_from_camera);
        photoBtnFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
        
        Button photoBtnFromFile = (Button) findViewById(R.id.add_artwork_btn_photo_from_file);
        photoBtnFromFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
        
        //TODO set resulting location from the map in the base class location
        Button launchMapBtn = (Button) findViewById(R.id.add_artwork_btn_map);
        launchMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
        
        Button addArtworkBtn = (Button) findViewById(R.id.add_artwork_btn_add_artwork);
        addArtworkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Artwork artwork = new Artwork();
                
                EditText nameField = (EditText) findViewById(R.id.add_artwork_name);
                String name = nameField.getText().toString();
                
                EditText descriptionField = (EditText) findViewById(R.id.add_artwork_description);
                String description = descriptionField.getText().toString();
                
                ImageView photo = (ImageView) findViewById(R.id.add_artwork_image);
                String encodedPhoto = ImageManager.encodeImageToString(((BitmapDrawable) photo.getDrawable()).getBitmap());
                
                artwork.setName(name);
                artwork.setDescription(description);
                artwork.setLocation(location);  //TODO set from the map in the base class
                artwork.setPhotoURL(encodedPhoto);
                
                boolean success = _gdm.get_dbManager().addArtwork(artwork);
                if(success){
                    Toast.makeText(getApplicationContext(), getString(R.string.add_artwork_message_success), Toast.LENGTH_LONG).show();
                    resetPage();
                }
                else{
                    Toast.makeText(getApplicationContext(), getString(R.string.add_artwork_message_failure), Toast.LENGTH_LONG).show();
                }
            }
        });
        
    }
    
    private void resetPage() {
        
        setLocation(new Location(getCallingPackage()));
        
        EditText nameField = (EditText) findViewById(R.id.add_artwork_name);
        nameField.setText(getString(R.string.add_artwork_name));
        
        EditText descriptionField = (EditText) findViewById(R.id.add_artwork_description);
        descriptionField.setText(getString(R.string.add_artwork_description));

        ImageView photo = (ImageView) findViewById(R.id.add_artwork_image);
        photo.setImageDrawable(getResources().getDrawable(ic_menu_camera));
    }
}
