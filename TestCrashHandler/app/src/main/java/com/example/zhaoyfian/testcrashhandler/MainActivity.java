package com.example.zhaoyfian.testcrashhandler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = makeString();
                Log.d("IVANDEBUG", "make String result: " + s);
                s.length();
            }
        });
    }

    private String makeString() {
        int r = new Random().nextInt();
        Log.d("IVANDEBUG", "random int is: " + r);
        if (r % 2 == 0) {
            return "hello world!";
        } else {
            return null;
        }
    }
}
