package com.disebud.puninar_absensi.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SharePrefManager {
    private static SharePrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "MonitoringDashboard";

    public static final String KEY_LAT     = "latitude";
    public static final String KEY_LONG     = "longitude";
    public static final String KEY_COUNTRY    = "country";
    public static final String KEY_KEC     = "kecamatan";
    public static final String KEY_ADRESS         = "adress";
    public static final String KEY_LOGIN        = "keySuccessLogin";
    public static final String KEY_ADMIN        = "keyAdmin";
    public static final String KEY_TELP         = "keyTelp";
    public static final String KEY_TOKEN        = "keyToken";
    public static final String KEY_TAGGED       = "TAGGED";
    public static final String KEY_LINE         = "keyLine";
    public static final String KEY_PLANT        = "keyPlant";
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    public SharePrefManager(Context context) {
        mCtx = context;
        sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public static synchronized SharePrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharePrefManager(context);
        }
        return mInstance;
    }





    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public String getSPString(String keySP){
        return sp.getString(keySP, "");
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String key7SP, boolean value){
        spEditor.putBoolean(key7SP, value);
        spEditor.commit();
    }

    public Boolean getSPBoolean(String key){
        return sp.getBoolean(key, false);
    }

    public Boolean getSPSudahLogin(){
        return sp.getBoolean(KEY_LOGIN, false);
    }


    public Boolean getAdmin(){
        return sp.getBoolean(KEY_ADMIN, false);
    }
    public Boolean getLogin(){
        return sp.getBoolean(KEY_LOGIN, false);
    }

    public String getSPLine(){
        return sp.getString(KEY_LINE, "");
    }
    public String getSPPlant(){
        return sp.getString(KEY_PLANT, "");
    }

    public String getTelp(){
        return sp.getString(KEY_TELP, "");
    }

    public String getPhotos(){
        return sp.getString(KEY_TAGGED, "");
    }
    public String getToken(){
        return sp.getString(KEY_TOKEN, "");
    }



    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

}

