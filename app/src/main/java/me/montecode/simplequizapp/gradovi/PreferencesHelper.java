package me.montecode.simplequizapp.gradovi;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by stevyhacker on 27.7.14..
 */
public class PreferencesHelper {

    private final String PREFS_NAME = "me.montecode.pmcg.kvizoprirodi.preferences";
    private Context context;

    public PreferencesHelper(Context context) {
        this.context = context;
    }

    public String getString(String key, String defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_NAME, 0);
        return pref.getString(key, defaultValue);
    }

    public void putString(String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

}
