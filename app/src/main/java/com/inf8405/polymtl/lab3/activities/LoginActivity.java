package com.inf8405.polymtl.lab3.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;
import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.listeners.LoginListener;
import com.inf8405.polymtl.lab3.managers.DatabaseManager;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;
import com.inf8405.polymtl.lab3.receivers.GPSManager;
import com.inf8405.polymtl.lab3.receivers.LowBatteryManager;
import com.inf8405.polymtl.lab3.receivers.NetworkManager;


public class LoginActivity extends AppCompatActivity {
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        final GlobalDataManager _gdm = ((GlobalDataManager) getApplicationContext());
        
        if (_gdm.get_dbManager() == null) {
            // first init of db_manager
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            DatabaseManager dbManager = new DatabaseManager(this);
            ((GlobalDataManager) getApplicationContext()).set_dbManager(dbManager);
        } else {
            _gdm.get_dbManager().set_loggedIn(false);
        }
        
        if(!_gdm.isReceiversRegistered()){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            
            registerReceiver(new LowBatteryManager(this), new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            registerReceiver(new NetworkManager(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            registerReceiver(new GPSManager(this), new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
            _gdm.setReceiversRegistered(true);
        }
        
        Button loginButton = (Button) findViewById(R.id.login_button_login);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                
                //Attempt login
                EditText nameField = (EditText) findViewById(R.id.login_name);
                EditText passwordField = (EditText) findViewById(R.id.login_password);
                
                LoginListener loginListener = new LoginListener(getApplicationContext(), nameField.getText().toString(), passwordField.getText().toString());
                _gdm.get_dbManager().login(loginListener);
                
            }
        });
        
        Button registerButton = (Button) findViewById(R.id.login_button_register);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameField = (EditText) findViewById(R.id.login_name);
                EditText passwordField = (EditText) findViewById(R.id.login_password);
                
                _gdm.get_dbManager().registerUser(nameField.getText().toString(), passwordField.getText().toString());
                
            }
        });
        
    }
}

