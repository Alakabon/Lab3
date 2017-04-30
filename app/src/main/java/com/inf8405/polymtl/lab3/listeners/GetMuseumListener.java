package com.inf8405.polymtl.lab3.listeners;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.entities.Museum;
import com.inf8405.polymtl.lab3.fragments.MuseumFragmentAdaptor;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;
import com.inf8405.polymtl.lab3.managers.ImageManager;

import java.util.ArrayList;

import static android.R.drawable.ic_menu_camera;

/**
 * Once again, for the moment being this is essentially a 1:1 copy of
 * @see GetArtworksListener
 * In theory the fields could be different but in our case they are not...
 * That being said, given that the names of the views are different this class is needed
 **/
public class GetMuseumListener implements GetDataListener {
    private static final String TAG = "GetMuseumListner";
    
    private Context ctx;
    private Activity browseMuseumActivity;
    
    public GetMuseumListener(Context ctx, Activity browseActivity) {
        this.ctx = ctx;
        this.browseMuseumActivity = browseActivity;
    }
    
    public Activity getBrowseActivity() {
        return browseMuseumActivity;
    }
    
    public void setBrowseActivity(Activity browseActivity) {
        this.browseMuseumActivity = browseActivity;
    }
    
    public Context getCtx() {
        return ctx;
    }
    
    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }
    
    @Override
    public void onSuccess(DataSnapshot dataSnapshot) {
        final ArrayList<Museum> museums = new ArrayList<>();
        
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Museum museum = snapshot.getValue(Museum.class);
            museums.add(museum);
        }
        
        ((GlobalDataManager) ctx.getApplicationContext()).set_museums(museums);
        
        final ListView listView = (ListView) browseMuseumActivity.findViewById(R.id.browse_museum_listview);
        
        listView.setAdapter(new MuseumFragmentAdaptor(browseMuseumActivity.getApplicationContext(), museums));
        
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                showInfoPopup((Museum) listView.getAdapter().getItem(pos));
            }
        });
    }
    
    @Override
    public void onFailed(DatabaseError databaseError) {
        Log.w(TAG, "Failed to retrieve museums ", databaseError.toException());
    }
    
    private void showInfoPopup(Museum museum) {
        Dialog popupWindow = new Dialog(browseMuseumActivity.getWindow().getContext());
        popupWindow.setContentView(R.layout.popup_museum_info_layout);
        
        // Setting fields
        TextView name = (TextView) popupWindow.findViewById(R.id.popup_museum_info_name);
        name.setText(museum.getName());
        
        TextView description = (TextView) popupWindow.findViewById(R.id.popup_museum_info_description);
        description.setText(museum.getDescription());
        
        TextView address = (TextView) popupWindow.findViewById(R.id.popup_museum_info_address);
        address.setText(museum.getAddress());
        
        Bitmap decodedPhoto = ImageManager.decodeImageFromString(museum.getPhotoURL());
        ImageView photo = (ImageView) popupWindow.findViewById(R.id.popup_museum_info_image);
        
        if (decodedPhoto != null) {
            photo.setImageBitmap(decodedPhoto);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                photo.setImageDrawable(browseMuseumActivity.getApplicationContext().getDrawable(ic_menu_camera));
            } else {
                photo.setImageDrawable(browseMuseumActivity.getApplicationContext().getResources().getDrawable(ic_menu_camera));
            }
        }
        
        popupWindow.setCancelable(true);
        popupWindow.setCanceledOnTouchOutside(true);
        popupWindow.show();
    }
}