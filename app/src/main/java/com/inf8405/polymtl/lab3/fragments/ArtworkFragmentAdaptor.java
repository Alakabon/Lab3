package com.inf8405.polymtl.lab3.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.entities.Artwork;
import com.inf8405.polymtl.lab3.managers.ImageManager;

import java.util.ArrayList;

import static android.R.drawable.ic_menu_camera;

public class ArtworkFragmentAdaptor extends ArrayAdapter<Artwork> {
    
    public ArtworkFragmentAdaptor(Context context, ArrayList<Artwork> users) {
        super(context, 0, users);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Artwork artwork = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_browse_artwork, parent, false);
        }
        // Lookup view for data population
        TextView nameField = (TextView) convertView.findViewById(R.id.fragment_name);
        TextView descriptionField = (TextView) convertView.findViewById(R.id.fragment_description);
        Bitmap decodedPhoto = ImageManager.decodeImageFromString(artwork.getPhotoURL());
        
        //TODO get actual picture back, decode with ImageManager and set
        ImageView photo = (ImageView) convertView.findViewById(R.id.fragment_image);
        
        if( decodedPhoto != null){
            photo.setImageBitmap(decodedPhoto);
        }
        else{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                photo.setImageDrawable(getContext().getApplicationContext().getDrawable(ic_menu_camera));
            } else {
                photo.setImageDrawable(getContext().getResources().getDrawable(ic_menu_camera));
            }
        }
        
        // Populate the data into the template view using the data object
        nameField.setText(artwork.getName());
        descriptionField.setText(artwork.getDescription());
        
        // Return the completed view to render on screen
        return convertView;
    }
}
