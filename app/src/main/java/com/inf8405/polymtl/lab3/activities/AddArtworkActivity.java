package com.inf8405.polymtl.lab3.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.entities.Artwork;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;
import com.inf8405.polymtl.lab3.managers.ImageManager;
import com.inf8405.polymtl.lab3.receivers.GPSManager;

import java.io.ByteArrayOutputStream;

import static android.R.drawable.ic_menu_camera;

/**
 * Activitée en charge de l'ajout d'une oeuvre d'art
 * */
public class AddArtworkActivity extends AppCompatActivity {
    
    private static final String TAG = "AddArtworkActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 8405;  //Constant value which will be used to identify specific camera results
    
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
        
        Button photoBtnFromCamera = (Button) findViewById(R.id.add_artwork_btn_photo_from_camera);
        photoBtnFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Instruct Android to automatically access the device's camera.
                //MediaStore is a built-in Android class that handles all things media,
                //and ACTION_IMAGE_CAPTURE is the standard intent that accesses the device's camera application
                Intent _camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                
                //It's ensuring a camera app is available and accessible. It's important to perform this check,
                //Because if we launch our intent and there is no camera application present to handle it, our app will crash
                if (_camera.resolveActivity(getPackageManager()) != null)
                    startActivityForResult(_camera, REQUEST_IMAGE_CAPTURE);
                
                //The above line, launch the camera, and retrieve the resulting image
                //It will automatically trigger the callback method onActivityResult()
                //when the result of the activity is available
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
                
                GPSManager.getLatestGPSLocation(getApplicationContext());
                Location deviceLocation = _gdm.get_deviceLocation();
                
                artwork.setGpsX(deviceLocation.getLatitude());
                artwork.setGpsY(deviceLocation.getLongitude());
                
                artwork.setPhotoURL(encodedPhoto);
                
                boolean success = _gdm.get_dbManager().addArtwork(artwork);
                
                if (success) {
                    Toast.makeText(getApplicationContext(), getString(R.string.add_artwork_message_success), Toast.LENGTH_LONG).show();
                    resetPage();
                } else {
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            photo.setImageDrawable(getApplicationContext().getDrawable(ic_menu_camera));
        } else {
            photo.setImageDrawable(getResources().getDrawable(ic_menu_camera));
        }
    }
    
    //The result of the action we are launching will be returned automatically to this callback method
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                //Retrieve the image and display it on the ImageView
                Bitmap _photo = (Bitmap) data.getExtras().get("data");
                ((ImageView) findViewById(R.id.add_artwork_image)).setImageBitmap(_photo);
                setImageViewTag(_photo);
            }
        } catch (Exception ex) {
            ((ImageView) findViewById(R.id.add_artwork_image)).setImageResource(R.drawable.profile_error);
            setImageViewTagAsDefault();
            Log.e(TAG, ex.getMessage());
        }
    }
    
    private void setImageViewTagAsDefault() {
        ByteArrayOutputStream _stream = new ByteArrayOutputStream();
        Bitmap _photo = BitmapFactory.decodeResource(getResources(), 17301559);
        _photo.compress(Bitmap.CompressFormat.PNG, 100, _stream);
        ((ImageView) findViewById(R.id.add_artwork_image)).setTag(Base64.encodeToString(_stream.toByteArray(), Base64.DEFAULT));
    }
    
    private void setImageViewTag(Bitmap photo) {
        ByteArrayOutputStream _stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, _stream);
        ((ImageView) findViewById(R.id.add_artwork_image)).setTag(Base64.encodeToString(_stream.toByteArray(), Base64.DEFAULT));
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            Toast.makeText(this, getResources().getString(R.string.orientation_msg), Toast.LENGTH_LONG).show();
    }
}
