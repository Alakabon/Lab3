package com.inf8405.polymtl.lab3.activities;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    private GlobalDataManager _gdm;
    private User _user;
    private SQLLiteManager sqldb;
    private SharedPreferences _sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkPermission();

        _sharedPref = this.getSharedPreferences("PREF_DATA", Context.MODE_PRIVATE);
        String lastUserName = getLastUsedUsername();

        //getGoogleUsername();
        sqldb = new SQLLiteManager(this);
        _user = sqldb.getUser(lastUserName);
        if (_user == null) {
            _user = new User(null, lastUserName, lastUserName);
            sqldb.addUser(_user);
        }
        fillLoginFields();

        _gdm = ((GlobalDataManager) getApplicationContext());

        if (_gdm.get_dbManager() == null) {
            // first init of db_manager
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            DatabaseManager dbManager = new DatabaseManager(this);
            ((GlobalDataManager) getApplicationContext()).set_dbManager(dbManager);
        } else {
            _gdm.get_dbManager().logOff();
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
        fillLoginFields();
    }

    //___________________________________________________________________________________________________________________________________//
    // Reading saved preferences (e.g., Last username) for loading proper user object from Firebase
    // If it was empty, strip out the username from the GMail address
    // Otherwise, will create a random username
    private String getLastUsedUsername() {
        String _username = _sharedPref.getString(getString(R.string.login_name), "");
        if (_username.isEmpty()) {
            List<String> _emails = new LinkedList<String>();
            for (Account account : AccountManager.get(this).getAccountsByType("com.google"))
                _emails.add(account.name);

            if (!_emails.isEmpty() && _emails.get(0) != null) {
                String[] parts = _emails.get(0).split("@");
                if (parts.length > 1) return parts[0];
            }

            return "User".concat(String.valueOf(new Random().nextInt(2000 - 1000) + 1000));
        }
        return _username;
    }

    private void fillLoginFields() {
        ((EditText) findViewById(R.id.login_name)).setText(_user.getName());
        ((EditText) findViewById(R.id.login_password)).setText(_user.getPassword());
        ((TextView) findViewById(R.id.txt6)).setText(_user.getId());
    }

    //___________________________________________________________________________________________________________________________________//
    private void applySavedLocalProfile() {
        SharedPreferences.Editor _editor = _sharedPref.edit();
        _editor.putString(getString(R.string.login_name), ((EditText) findViewById(R.id.login_name)).getText().toString());
        _editor.apply();
    }

    //___________________________________________________________________________________________________________________________________//
    // Using SharedPreferences to store preferences of the application when application is closing
    @Override
    public void onStop() {
        super.onStop();
        applySavedLocalProfile();
        sqldb.updateUser(new User(_user.getId(), ((EditText) findViewById(R.id.login_name)).getText().toString(), ((EditText) findViewById(R.id.login_password)).getText().toString()));
    }

    private String returnPermission(String permission) {
        return permission.concat(": ").concat((ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) ? "GRANTED\n" : "DENIED\n").replace("android.permission.", "");
    }

    private void checkPermission() {

        String msg = returnPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        msg += returnPermission(Manifest.permission.ACCESS_NETWORK_STATE);
        msg += returnPermission(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
        msg += returnPermission(Manifest.permission.ACCESS_CHECKIN_PROPERTIES);
        msg += returnPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        msg += returnPermission(Manifest.permission.ACCESS_WIFI_STATE);
        msg += returnPermission(Manifest.permission.ACCOUNT_MANAGER);
        msg += returnPermission(Manifest.permission.BATTERY_STATS);
        msg += returnPermission(Manifest.permission.CAMERA);
        msg += returnPermission(Manifest.permission.CONTROL_LOCATION_UPDATES);
        msg += returnPermission(Manifest.permission.INTERNET);
        msg += returnPermission(Manifest.permission.LOCATION_HARDWARE);

        ((TextView) findViewById(R.id.txt7)).setText(msg);
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
//            Toast.makeText(this, getResources().getString(R.string.orientation_msg), Toast.LENGTH_LONG).show();
//    }
}

