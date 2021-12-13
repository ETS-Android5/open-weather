package com.example.openweather;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserPreferences {

    private final SharedPreferences userPrefs;
    private final Editor editor;

    UserPreferences(Activity activity) {
        super();
        userPrefs = activity.getSharedPreferences(activity.getString(R.string.prefs_file_key), Context.MODE_PRIVATE);
        editor = userPrefs.edit();
    }

    void save(String key, String text) {
        editor.putString(key, text);
        editor.apply();
    }

    String getValue(String key) {
        return userPrefs.getString(key, "");
    }

    void clearAll() {
        editor.clear();
        editor.apply();
    }

    void removeValue(String key) {
        editor.remove(key);
        editor.apply();
    }
}
