package com.example.zhaoyfian.testwitaispeech;

import android.app.Application;
import android.util.Log;

import com.facebook.FacebookSdk;

/**
 * Created by zhaoyfian on 17-2-8.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.setAutoLogAppEventsEnabled(true);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Log.d("IVANDEBUG", "@@@@@@@@@ id: " + FacebookSdk.getApplicationId());
        Log.d("IVANDEBUG", "@@@@@@@@@ name: " + FacebookSdk.getApplicationName());
    }
}
