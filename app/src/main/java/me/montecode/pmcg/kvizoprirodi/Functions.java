package me.montecode.pmcg.kvizoprirodi;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by stevyhacker on 25.7.14..
 */
public class Functions {

    public Boolean checkNetworkStatus(Context context){
        Boolean status = false;
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conManager != null) {
            NetworkInfo ni = conManager.getActiveNetworkInfo();
            if(ni != null && ni.isConnected()){
                status = true;
            }
            else {
                status = false;
            }
        }
        return status;
    }


    public String jsonToStringFromAssetFolder(String fileName, Context context) throws IOException
    {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(fileName);

        byte[] data = new byte[file.available()];
        file.read(data);
        file.close();
        return new String(data);
    }
}
