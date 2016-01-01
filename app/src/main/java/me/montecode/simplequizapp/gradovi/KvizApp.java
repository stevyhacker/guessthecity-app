package me.montecode.simplequizapp.gradovi;

import android.app.Application;
import android.content.Context;


/**
 * Created by stevyhacker on 27.7.14..
 */
public class KvizApp extends Application {

    private static KvizApp app;
    public static PreferencesHelper preferencesHelper;

    public static void init(Context context) {
        if (app == null)
            app = new KvizApp(context);
    }

    public KvizApp(Context context) {
        preferencesHelper = new PreferencesHelper(context);
    }

}
