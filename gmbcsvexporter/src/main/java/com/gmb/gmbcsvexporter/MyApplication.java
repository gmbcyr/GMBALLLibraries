package com.gmb.gmbcsvexporter;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by GMB on 12/20/2016.
 */

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public static String getDefaultDir(){

        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getAppContext());


        return pref.getString("txt_default_export_dir",".");
    }

    public static String getDefaultFileName(){

        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getAppContext());


        return pref.getString("txt_default_file_name","BBMexportedFile");
    }
}
