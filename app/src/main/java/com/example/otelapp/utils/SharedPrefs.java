package com.example.otelapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import java.util.Map;
import java.util.Set;

public class SharedPrefs{
    Context context;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    //constructor
    public SharedPrefs(Context context) {
        this.context = context;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = prefs.edit();
    }

    public void setString(String key, String value){
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key){
        String value = prefs.getString(key, null);
        return value;
    }
    public void removeKeyPair(String key){
        editor.remove(key);
        editor.commit();
    }
    public void setLang(String lang){
        editor.putString("Lang", lang);
        editor.commit();
    }
    public String getLang(){
        String res = prefs.getString("Lang", "en");
        return res;
    }
}
