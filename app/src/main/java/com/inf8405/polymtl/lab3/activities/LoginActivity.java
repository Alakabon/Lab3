package com.inf8405.polymtl.lab3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.managers.DatabaseManager;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;


public class LoginActivity extends AppCompatActivity {
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        final GlobalDataManager _gdm = ((GlobalDataManager) getApplicationContext());
        
        if( _gdm.get_dbManager() == null){
            // first init of db_manager
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            DatabaseManager dbManager = new DatabaseManager(this);
            ((GlobalDataManager) getApplicationContext()).set_dbManager(dbManager);
        }
        else{
            _gdm.get_dbManager().set_loggedIn(false);
        }
        
        Button loginButton = (Button) findViewById(R.id.login_button_login);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                
                //Attempt login
                EditText nameField = (EditText) findViewById(R.id.login_name);
                EditText passwordField = (EditText) findViewById(R.id.login_password);
                
                _gdm.get_dbManager().login(nameField.getText().toString(), passwordField.getText().toString());
                if(_gdm.get_dbManager().is_loggedIn()){
                    Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),getString(R.string.login_error_failed),Toast.LENGTH_LONG).show();
                }
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

