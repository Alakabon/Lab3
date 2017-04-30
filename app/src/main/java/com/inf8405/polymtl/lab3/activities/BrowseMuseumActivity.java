package com.inf8405.polymtl.lab3.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.listeners.GetMuseumListener;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;


public class BrowseMuseumActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_museum);
        
        GetMuseumListener getMuseumListener = new GetMuseumListener(getApplicationContext(), this);
        
        ((GlobalDataManager) getApplicationContext()).get_dbManager().retrieveMuseums(getMuseumListener);
    }
}
