package com.kupang.androidbpbd.bpbdkupang.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.kupang.androidbpbd.bpbdkupang.model.Report;
import com.kupang.androidbpbd.bpbdkupang.model.User;
import com.kupang.androidbpbd.bpbdkupang.ui.autentikasi.LoginActivity;


/**
 * Created by Belal on 9/5/2017.
 */

//here for this class we are using a singleton pattern

public class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "bpbd";
    private static final String KEY_NAME = "keyusername";
    private static final String KEY_TELEPHONE = "keytelephone";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_ID = "keyid";

    private static final String SHARED_REPORT_PREF_NAME = "report";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_ADDRESS = "address";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getUserId());
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_TELEPHONE, user.getTelephoneNumber());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.apply();
    }

    public void saveReport(Report report) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_REPORT_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LATITUDE, report.getLatitude());
        editor.putString(KEY_LONGITUDE, report.getLongitude());
        editor.putString(KEY_ADDRESS, report.getAddress());
        editor.apply();
    }


    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, null) != null;
    }


    public void updateUser(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_TELEPHONE, user.getTelephoneNumber());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.apply();
    }


    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_TELEPHONE, null),
                sharedPreferences.getString(KEY_EMAIL, null)
        );
    }

    public Report getReport() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_REPORT_PREF_NAME, Context.MODE_PRIVATE);
        return new Report(
                sharedPreferences.getString(KEY_LATITUDE, ""),
                sharedPreferences.getString(KEY_LONGITUDE, ""),
                sharedPreferences.getString(KEY_ADDRESS, "")
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void deleteReport() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_REPORT_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}