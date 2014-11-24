package me.montecode.pmcg.kvizoprirodi;

import android.app.Application;
import android.content.Context;


/**
 * Created by stevyhacker on 27.7.14..
 */
public class PMCGKvizZnanjaApp extends Application {

    private static PMCGKvizZnanjaApp app;
    public static PreferencesHelper preferencesHelper;

    public static void init(Context context) {
        if (app == null)
            app = new PMCGKvizZnanjaApp(context);
    }

    public PMCGKvizZnanjaApp(Context context) {
        preferencesHelper = new PreferencesHelper(context);
    }

}
