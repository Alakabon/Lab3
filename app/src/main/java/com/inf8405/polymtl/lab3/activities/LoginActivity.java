package com.inf8405.polymtl.lab3.activities;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.entities.User;
import com.inf8405.polymtl.lab3.listeners.LoginListener;
import com.inf8405.polymtl.lab3.managers.DatabaseManager;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;
import com.inf8405.polymtl.lab3.managers.SQLLiteManager;
import com.inf8405.polymtl.lab3.receivers.GPSManager;
import com.inf8405.polymtl.lab3.receivers.LowBatteryManager;
import com.inf8405.polymtl.lab3.receivers.NetworkManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class LoginActivity extends AppCompatActivity {


    GlobalDataManager _gdm;
    private User _user;
    private String DefaultUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getGoogleUsername();
        SQLLiteManager db = new SQLLiteManager(this);
        _user = db.getUser(DefaultUserName);
        if (_user == null) {
            loadDefaultUserProfile();
            db.addUser(_user);
            _user = db.getUser(DefaultUserName);
        }
        fillLoginFields();

        _gdm = ((GlobalDataManager) getApplicationContext());

        if (_gdm.get_dbManager() == null) {
            // first init of db_manager
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            DatabaseManager dbManager = new DatabaseManager(this);
            ((GlobalDataManager) getApplicationContext()).set_dbManager(dbManager);
        } else {
            _gdm.get_dbManager().set_loggedIn(false);
        }

        if (!_gdm.isReceiversRegistered()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            registerReceiver(new LowBatteryManager(this), new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            registerReceiver(new NetworkManager(this), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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

    @Override
    public void onResume() {
        super.onResume();
        ((TextView) findViewById(R.id.txt1)).setText(_gdm.get_online_status_string());
        ((TextView) findViewById(R.id.txt2)).setText(_gdm.get_battery_level_string());
        ((TextView) findViewById(R.id.txt5)).setText(_gdm.get_GPS_provider_status_string());
        ((TextView) findViewById(R.id.txt3)).setText(_gdm.get_GPS_latitude_string());
        ((TextView) findViewById(R.id.txt4)).setText(_gdm.get_GPS_longitude_string());
    }

    //___________________________________________________________________________________________________________________________________//
    // Reading saved preferences (e.g., Last username) for loading proper user object from Firebase
    // If it was empty, strip out the username from the GMail address
    // Otherwise, will create a random username
    private void getGoogleUsername() {
        List<String> _emails = new LinkedList<String>();
        for (Account account : AccountManager.get(this).getAccountsByType("com.google"))
            _emails.add(account.name);

        if (!_emails.isEmpty() && _emails.get(0) != null) {
            String[] parts = _emails.get(0).split("@");
            if (parts.length > 1) DefaultUserName = parts[0];
        } else {
            DefaultUserName = "NewUser";
        }
    }


    private void loadDefaultUserProfile() {
        _user = new User(null, DefaultUserName, DefaultUserName);
        fillLoginFields();
    }

    private void fillLoginFields(){
        ((EditText) findViewById(R.id.login_name)).setText(_user.getName());
        ((EditText) findViewById(R.id.login_password)).setText(_user.getPassword());
        ((TextView) findViewById(R.id.txt6)).setText(_user.getId());
    }
}

