package com.example.zhaoyfian.testcrashhandler;

import android.app.Application;

import com.example.zhaoyfian.testcrashhandler.ch.CrashHandler;

/**
 * Created by zhaoyfian on 17-2-3.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
        if(!(handler instanceof CrashHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance());
        }
    }
}
