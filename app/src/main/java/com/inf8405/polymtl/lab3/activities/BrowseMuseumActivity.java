package com.inf8405.polymtl.lab3.activities;


import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.entities.Museum;
import com.inf8405.polymtl.lab3.fragments.MuseumFragmentAdaptor;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;
import com.inf8405.polymtl.lab3.managers.ImageManager;

import static android.R.drawable.ic_menu_camera;


public class BrowseMuseumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_museum);

        setupListView();
    }

    private void setupListView() {
        final ListView listView = (ListView) findViewById(R.id.browse_museum_listview);

        listView.setAdapter(new MuseumFragmentAdaptor(getApplicationContext(), ((GlobalDataManager) getApplicationContext()).get_museums()));

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                showInfoPopup((Museum) listView.getAdapter().getItem(pos));
            }
        });
    }

    private void showInfoPopup(Museum museum) {
        Dialog popupWindow = new Dialog(getWindow().getContext());
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
                photo.setImageDrawable(getApplicationContext().getDrawable(ic_menu_camera));
            } else {
                photo.setImageDrawable(getApplicationContext().getResources().getDrawable(ic_menu_camera));
            }
        }

        popupWindow.setCancelable(true);
        popupWindow.setCanceledOnTouchOutside(true);
        popupWindow.show();
    }


}
