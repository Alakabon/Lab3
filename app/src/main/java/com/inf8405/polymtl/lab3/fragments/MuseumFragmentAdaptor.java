package com.inf8405.polymtl.lab3.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.entities.Museum;
import com.inf8405.polymtl.lab3.managers.ImageManager;

import java.util.ArrayList;

import static android.R.drawable.ic_menu_camera;

/**
 * For the moment being, this is essentially a 1:1 copy of
 *
 * @see ArtworkFragmentAdaptor
 * In theory the fields could be different but in our case they are not...
 * That being said, given that the names of the views are different this class is needed
 **/
public class MuseumFragmentAdaptor extends ArrayAdapter<Museum> {
    private static final int characterLimit = 50;
    
    public MuseumFragmentAdaptor(Context context, ArrayList<Museum> museums) {
        super(context, 0, museums);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Museum museum = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_browse_museum, parent, false);
        }
        
        TextView nameField = (TextView) convertView.findViewById(R.id.fragment_museum_name);
        TextView descriptionField = (TextView) convertView.findViewById(R.id.fragment_museum_description);
        
        Bitmap decodedPhoto = ImageManager.decodeImageFromString(museum.getPhotoURL());
        ImageView photo = (ImageView) convertView.findViewById(R.id.fragment_museum_image);
        
        if (decodedPhoto != null) {
            photo.setImageBitmap(decodedPhoto);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                photo.setImageDrawable(getContext().getApplicationContext().getDrawable(ic_menu_camera));
            } else {
                photo.setImageDrawable(getContext().getResources().getDrawable(ic_menu_camera));
            }
        }
        
        nameField.setText(museum.getName());
        
        String fullDescription = museum.getDescription();
        if (fullDescription.length() >= characterLimit) {
            String shortDesc = fullDescription.substring(0, characterLimit);
            shortDesc = shortDesc + "...";
            descriptionField.setText(shortDesc);
        } else {
            descriptionField.setText(museum.getDescription());
        }
        
        return convertView;
    }
    
    public void refresh() {
        this.notifyDataSetChanged();
    }
}
