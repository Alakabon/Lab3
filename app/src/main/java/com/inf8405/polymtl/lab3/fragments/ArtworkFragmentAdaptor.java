package com.inf8405.polymtl.lab3.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
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
import java.util.Comparator;

import static android.R.drawable.ic_menu_camera;

/**
 * Class used to assign the fields of an artwork to the layout of the fragment
 **/
public class ArtworkFragmentAdaptor extends ArrayAdapter<Artwork> {
    
    public ArtworkFragmentAdaptor(Context context, ArrayList<Artwork> artworks) {
        super(context, 0, artworks);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item
        Artwork artwork = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_browse_artwork, parent, false);
        }

        // Get fields
        TextView nameField = (TextView) convertView.findViewById(R.id.fragment_artwork_name);
        TextView descriptionField = (TextView) convertView.findViewById(R.id.fragment_artwork_description);

        Bitmap decodedPhoto = ImageManager.decodeImageFromString(artwork.getPhotoURL());
        ImageView photo = (ImageView) convertView.findViewById(R.id.fragment_artwork_image);
        
        if (decodedPhoto != null) {
            photo.setImageBitmap(decodedPhoto);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                photo.setImageDrawable(getContext().getApplicationContext().getDrawable(ic_menu_camera));
            } else {
                photo.setImageDrawable(getContext().getResources().getDrawable(ic_menu_camera));
            }
        }
        
        // Set fields
        nameField.setText(artwork.getName());
        Log.v("ArtworkFragment", "Name=" +  artwork.getName());
        descriptionField.setText(artwork.getDescription());
        Log.v("ArtworkFragment", "Description=" +  artwork.getDescription());
        
        // Return the completed view to render on screen
        return convertView;
    }
    
    @Override
    public int getCount() {
        return super.getCount();
    }
}
