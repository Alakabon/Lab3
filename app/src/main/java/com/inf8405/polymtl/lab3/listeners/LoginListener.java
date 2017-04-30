package com.inf8405.polymtl.lab3.listeners;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.inf8405.polymtl.lab3.R;
import com.inf8405.polymtl.lab3.activities.MainMenuActivity;
import com.inf8405.polymtl.lab3.entities.User;
import com.inf8405.polymtl.lab3.managers.GlobalDataManager;
import com.inf8405.polymtl.lab3.managers.PasswordManager;

public class LoginListener implements GetDataListener {
    private static String TAG = "LoginListener";
    
    private Context ctx;
    private String userName;
    private String plainPassword;
    
    public LoginListener(Context ctx, String userName, String plainPassword) {
        this.ctx = ctx;
        this.userName = userName;
        this.plainPassword = plainPassword;
    }
    
    public Context getCtx() {
        return ctx;
    }
    
    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getPlainPassword() {
        return plainPassword;
    }
    
    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }
    
    @Override
    public void onStart() {
        //Empty for the moment
    }
    
    @Override
    public void onSuccess(DataSnapshot dataSnapshot) {
        User dbUser = dataSnapshot.getValue(User.class);
        String dbPassword = PasswordManager.decryptPassword(dbUser.getPassword());
        
        if (plainPassword.equals(dbPassword)) {
            ((GlobalDataManager)ctx.getApplicationContext()).get_dbManager().set_loggedIn(true);
            ((GlobalDataManager)ctx.getApplicationContext()).get_dbManager().syncUser(dbUser);
            Intent intent = new Intent(ctx, MainMenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        } else {
            Toast.makeText(ctx, ctx.getString(R.string.login_error_failed_password), Toast.LENGTH_LONG).show();
        }
    }
    
    @Override
    public void onFailed(DatabaseError databaseError) {
        Toast.makeText(ctx, ctx.getString(R.string.login_error_failed_username), Toast.LENGTH_LONG).show();
    }
}
