package com.example.zhaoyfian.testwitaispeech;

import android.content.Intent;
import android.media.AudioFormat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import ai.wit.sdk.IWitListener;
import ai.wit.sdk.Wit;
import ai.wit.sdk.model.WitOutcome;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

public class MainActivity extends AppCompatActivity implements IWitListener {
    private Wit mWit;
    private Button mBtn;
    private Button mLoginBtn;
    private Button mLogoutBtn;
    private CallbackManager callbackManager;

    public static final List<String> FB_DEFAULT_PERMISSIONS = Arrays.asList("public_profile",
            "user_friends",
            "email");

//    private VoiceRecorder mVoiceRecorder;
//    private final VoiceRecorder.Callback mVoiceCallback = new VoiceRecorder.Callback() {
//        @Override
//        public void onVoiceStart() {
//
//        }
//
//        @Override
//        public void onVoice(byte[] data, int size) {
//            InputStream is = new ByteArrayInputStream(data);
////            PipedInputStream pis = new PipedInputStream();
//            mWit.streamRawAudio(is,
//                    "signed-integer",
//                    16,
//                    mVoiceRecorder.getSampleRate(),
//                    ByteOrder.LITTLE_ENDIAN);
//        }
//
//        @Override
//        public void onVoiceEnd() {
//
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWit = null;
        mWit = new Wit("LOVB4OFXBZ4ZS7AYVHIAZ54JPDLDS3EC", MainActivity.this);
        mWit.enableContextLocation(getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        Log.d("IVANDEBUG", "@@@@@@@@@@@ onSuccess result: " + loginResult);
//                        AccessToken token = loginResult.getAccessToken();
//                        final String acctoken = (token == null) ? null : token.getToken();
//                        if (acctoken != null) {
//                            mWit = new Wit(acctoken, MainActivity.this);
//                            mWit.enableContextLocation(getApplicationContext());
//                        } else {
//                            mWit = null;
//                        }
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        Log.d("IVANDEBUG", "@@@@@@@@@@@ onCancel");
//                        mWit = null;
//                    }
//
//                    @Override
//                    public void onError(FacebookException error) {
//                        Log.d("IVANDEBUG", "@@@@@@@@@@@ onError error: " + error);
//                        mWit = null;
//                    }
//                });

        mBtn = (Button) findViewById(R.id.button);
//        mBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toggle(mBtn);
//            }
//        });
        mBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        stopRecord();
                        break;
                }
                return false;
            }
        });
        mLoginBtn = (Button) findViewById(R.id.login);
//        mLoginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginManager.getInstance()
//                        .logInWithReadPermissions(MainActivity.this, FB_DEFAULT_PERMISSIONS);
//            }
//        });
        mLogoutBtn = (Button) findViewById(R.id.logout);
//        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginManager.getInstance().logOut();
//            }
//        });
    }

    private void stopRecord() {
        if (mWit != null) {
            mWit.stopListening();
        }
//        if (mVoiceRecorder != null) {
//            mVoiceRecorder.stop();
//            mVoiceRecorder = null;
//        }
    }

    private void startRecord() {
        try {
            mWit.startListening();
        } catch (Throwable e) {
            e.printStackTrace();
        }
//        if (mVoiceRecorder != null)
//            mVoiceRecorder.stop();
//        mVoiceRecorder = new VoiceRecorder(mVoiceCallback);
//        mVoiceRecorder.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // facebook 将登录结果转发到在 onCreate() 中创建的 callbackManager
        if (null != callbackManager) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toggle(View v) {
        if (mWit != null) {
            try {
                mWit.toggleListening();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void witDidGraspIntent(ArrayList<WitOutcome> witOutcomes, String messageId, Error error) {
        Log.d("IVANDEBUG", "@@@@@@@@@2 witDidGraspIntent error: " + error);
        TextView jsonView = (TextView) findViewById(R.id.jsonView);
        jsonView.setMovementMethod(new ScrollingMovementMethod());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (error != null) {
            jsonView.setText(error.getLocalizedMessage());
            return ;
        }
        String jsonOutput = gson.toJson(witOutcomes);
        jsonView.setText(jsonOutput);
        ((TextView) findViewById(R.id.txtText)).setText("Done!");
    }

    @Override
    public void witDidStartListening() {
        Log.d("IVANDEBUG", "@@@@@@@@@2 start");
        ((TextView) findViewById(R.id.txtText)).setText("Witting...");
    }

    @Override
    public void witDidStopListening() {
        Log.d("IVANDEBUG", "@@@@@@@@@2 stop");
        ((TextView) findViewById(R.id.txtText)).setText("Processing...");
    }

    @Override
    public void witActivityDetectorStarted() {
        Log.d("IVANDEBUG", "@@@@@@@@@2 Listening");
        ((TextView) findViewById(R.id.txtText)).setText("Listening");
    }

    @Override
    public String witGenerateMessageId() {
        String id = "CUSTOM-ID" + UUID.randomUUID();
        Log.d("IVANDEBUG", "@@@@@@@@@2 witGenerateMessageId id: " + id);
        return id;
    }

//    public static class PlaceholderFragment extends Fragment {
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            // Inflate the layout for this fragment
//            return inflater.inflate(R.layout.wit_button, container, false);
//        }
//    }
}
