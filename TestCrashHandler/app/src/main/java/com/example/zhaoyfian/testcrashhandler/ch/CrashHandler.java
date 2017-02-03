package com.example.zhaoyfian.testcrashhandler.ch;

import android.content.Context;
import android.util.Log;

/**
 * Created by zhaoyfian on 17-2-3.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler instance;

    private CrashHandler(){}

    public synchronized static CrashHandler getInstance(){
        if (instance == null){
            instance = new CrashHandler();
        }
        return instance;
    }

    public void init(Context ctx){
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.d("IVANDEBUG", "here is uncaught exception, in thread: " + t.toString());
        Log.d("IVANDEBUG", "here is uncaught exception, throwable: " + e.toString());
        e.printStackTrace();
    }
}
