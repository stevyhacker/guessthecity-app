package me.montecode.simplequizapp.gradovi;

import android.app.Application;

/**
 * Created by stevyhacker on 24.11.14..
 */
public class MyApplication extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();
        KvizApp.init(this);
    }


}
