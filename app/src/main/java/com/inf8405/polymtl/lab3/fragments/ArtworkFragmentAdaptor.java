package com.inf8405.polymtl.lab3.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.entities.Artwork;

import java.util.ArrayList;

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
        
        // Populate the data into the template view using the data object
        nameField.setText(artwork.getName());
        descriptionField.setText(artwork.getDescription());
        
        // Return the completed view to render on screen
        return convertView;
    }
}
