package com.inf8405.polymtl.lab3.activities;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Activitée en charge de la présentation de quelque données et du log-in
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login";
    private GlobalDataManager _gdm;
    private User _user;
    private SQLLiteManager sqldb;
    private SharedPreferences _sharedPref;
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getNFCAdapter();

        _sharedPref = this.getSharedPreferences("PREF_DATA", Context.MODE_PRIVATE);
        String lastUserName = getLastUsedUsername();

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

    // Filling login fields based on information of user object
    private void fillLoginFields() {
        ((EditText) findViewById(R.id.login_name)).setText(_user.getName());
        ((EditText) findViewById(R.id.login_password)).setText(_user.getPassword());
        ((TextView) findViewById(R.id.txt6)).setText(_user.getId());
    }

    private void applySavedLocalProfile() {
        SharedPreferences.Editor _editor = _sharedPref.edit();
        _editor.putString(getString(R.string.login_name), ((EditText) findViewById(R.id.login_name)).getText().toString());
        _editor.apply();
    }

    // Using SharedPreferences to store preferences of the application when application is closing
    @Override
    public void onStop() {
        super.onStop();
        applySavedLocalProfile();
        sqldb.updateUser(new User(_user.getId(), ((EditText) findViewById(R.id.login_name)).getText().toString(), ((EditText) findViewById(R.id.login_password)).getText().toString()));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            Toast.makeText(this, getResources().getString(R.string.orientation_msg), Toast.LENGTH_LONG).show();
    }

    //Interact with the hardware via the NfcAdapter class and shows corresponding messages
    private void getNFCAdapter() {
        try {
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);

            if (nfcAdapter == null) {
                // Stop here, we definitely need NFC
                Toast.makeText(this, getResources().getString(R.string.nfc_msg), Toast.LENGTH_LONG).show();
            } else {
                //see whether NFC is enabled or disabled
                if (!nfcAdapter.isEnabled()) {
                    Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(this, "Error reading NFC data.", Toast.LENGTH_LONG).show();
                }

                handleIntent(getIntent());
            }
        } catch (Exception ex) {
            Log.w(TAG, ex.getMessage());
        }
    }

    //Handling NFC Intent
    private void handleIntent(Intent intent) {
        //
//        Tag detectedTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
//        IntentFilter filter2     = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
//        IntentFilter[] readTagFilters = new IntentFilter[]{tagDetected,filter2};
        String action = intent.getAction();
        if (nfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            Parcelable[] _rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] _msgs = null;
            if (_rawMsgs != null) {
                _msgs = new NdefMessage[_rawMsgs.length];
                for (int i = 0; i < _rawMsgs.length; i++) {
                    _msgs[i] = (NdefMessage) _rawMsgs[i];
                }
            }
            buildTagViews(_msgs);
        } else {
            Toast.makeText(this, "NFC is supported.", Toast.LENGTH_SHORT).show();
        }
    }

    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;

        String text = "";
//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            // Get the Text
            text = "\n" + new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }

        AlertDialog.Builder _dlg = new AlertDialog.Builder(this);
        _dlg.setTitle(R.string.app_name);
        _dlg.setMessage(getResources().getString(R.string.msg_sync) + text);
        _dlg.setIcon(android.R.drawable.ic_dialog_info);
        _dlg.setCancelable(true);
        _dlg.setPositiveButton(R.string.msg_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //TODO: Favorite list sync
            }
        });

        _dlg.setNegativeButton(R.string.msg_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        _dlg.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
//        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
//            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        }
    }
}