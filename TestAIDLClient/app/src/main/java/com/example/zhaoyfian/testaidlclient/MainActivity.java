package com.example.zhaoyfian.testaidlclient;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.zhaoyfian.testaidlserver.ITestAIDL;

public class MainActivity extends AppCompatActivity {
    private Handler handler = new Handler(Looper.getMainLooper());
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            test = ITestAIDL.Stub.asInterface(service);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    updateUI();
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            test = null;
        }
    };
    private ITestAIDL test;
    private TextView testView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testView = (TextView) findViewById(R.id.testui);
        Intent intent = new Intent("com.example.zhaoyfian.testaidlserver.MyService");
        intent.setPackage("com.example.zhaoyfian.testaidlserver");
        bindService(intent, conn, Service.BIND_AUTO_CREATE);
    }

    private void updateUI() {
        try {
            String result = test.sayHelloTo("Ivan", 3);
            testView.setText(result);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
