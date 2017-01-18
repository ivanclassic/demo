package com.example.zhaoyfian.testaidlserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new TestAIDL();
    }

    public class TestAIDL extends ITestAIDL.Stub {

        @Override
        public String sayHelloTo(String name, int times) throws RemoteException {
            String result = "";
            for (int i = 0; i < times; i++) {
                result += "Hello, " + name + "|";
            }
            return result;
        }
    }
}
