package com.example.zhaoyfian.scrollactivitytest;

import android.app.Activity;
import android.os.Bundle;

import com.example.zhaoyfian.scrollactivitytest.widget.SlideFrameLayout;

/**
 * Created by zhaoyfian on 17-1-18.
 */

public class SlideActivity extends Activity {
    private SlideFrameLayout mSlider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSlider = new SlideFrameLayout(this);
        mSlider.bindActivity(this);
        setContentView(R.layout.activity_test);
    }
}
