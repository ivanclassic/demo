package com.example.zhaoyfian.speechrecognizerdemo;

import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements RecognitionListener {

    private SpeechRecognizer mRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(this);
        findViewById(R.id.button).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mRecognizer.cancel();
                        Intent intent = new Intent();
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                        mRecognizer.startListening(intent);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        mRecognizer.stopListening();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d("IVANDEBUG", "@@@@ onReadyForSpeech params: " + params);
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d("IVANDEBUG", "@@@@ onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.d("IVANDEBUG", "@@@@ onRmsChanged rmsdB: " + rmsdB);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d("IVANDEBUG", "@@@@ onBufferReceived buffer: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.d("IVANDEBUG", "@@@@ onEndOfSpeech");
    }

    @Override
    public void onError(int error) {
        Log.d("IVANDEBUG", "@@@@ onError error: " + error);
    }

    @Override
    public void onResults(Bundle results) {
        Log.d("IVANDEBUG", "@@@@ onResults results: " + results);
        ArrayList<String> nbest = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Log.d("IVANDEBUG", ">>>>>>>>>>>>> " + Arrays.toString(nbest.toArray(new String[nbest.size()])));
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.d("IVANDEBUG", "@@@@ onPartialResults partialResults: " + partialResults);

        ArrayList<String> nbest = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (nbest.size() > 0) {
            Log.d("IVANDEBUG", "============ " + Arrays.toString(nbest.toArray(new String[0])));
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d("IVANDEBUG", "@@@@ onEvent eventType: " + eventType + ", params: " + params);
    }
}
