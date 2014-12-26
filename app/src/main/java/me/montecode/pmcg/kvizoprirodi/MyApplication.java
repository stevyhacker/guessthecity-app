package me.montecode.pmcg.kvizoprirodi;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by stevyhacker on 24.11.14..
 */
public class MyApplication extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();
        CalligraphyConfig.initDefault("fonts/Courgette-Regular.ttf", R.attr.fontPath);
        PMCGKvizZnanjaApp.init(this);
    }


}
