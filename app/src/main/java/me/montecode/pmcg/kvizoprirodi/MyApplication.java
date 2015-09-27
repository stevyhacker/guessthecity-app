package me.montecode.pmcg.kvizoprirodi;

import android.app.Application;

/**
 * Created by stevyhacker on 24.11.14..
 */
public class MyApplication extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();
        PMCGKvizZnanjaApp.init(this);
    }


}
