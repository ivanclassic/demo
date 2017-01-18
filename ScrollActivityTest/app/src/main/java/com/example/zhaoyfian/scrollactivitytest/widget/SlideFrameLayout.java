package com.example.zhaoyfian.scrollactivitytest.widget;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.example.zhaoyfian.scrollactivitytest.R;

public class SlideFrameLayout extends FrameLayout {
    private static final int ANIMATION_DURING_TIME = 100;
    private static final int SHADOW_DP = 5;

    private DisplayMetrics mMetrics = null;

    private Activity mActivity;
    private Scroller mScroller;

    private int mLastX;
    private int mMyWidth;
    private boolean mFinished;
    private int mShadowWidth;
    private Drawable mShadow;

    public SlideFrameLayout(Activity activity) {
        this(activity, null);
    }

    public SlideFrameLayout(Activity activity, AttributeSet attrs) {
        this(activity, attrs, 0);
    }

    public SlideFrameLayout(Activity activity, AttributeSet attrs, int defStyleAttr) {
        super(activity, attrs, defStyleAttr);
        init(activity);
    }

    private void init(Activity activity) {
        mMetrics = activity.getResources().getDisplayMetrics();
        mActivity = activity;
        mScroller = new Scroller(activity);
        mShadowWidth = dp2px(SHADOW_DP);
        mShadow = getResources().getDrawable(R.drawable.left_shadow);
    }

    private int dp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, mMetrics);
    }

    public void bindActivity(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View child = decorView.getChildAt(0);
        decorView.removeView(child);
        addView(child);
        decorView.addView(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) event.getX();
                mMyWidth = getWidth();
                break;
            case MotionEvent.ACTION_MOVE:
                int rightMovedX = mLastX - (int) event.getX();
                if (rightMovedX < 0) {
                    scrollBy(rightMovedX, 0);
                }
                mLastX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (-getScrollX() > mMyWidth / 2) {
                    scrollClose();
                    mFinished = true;
                } else {
                    scrollBack();
                    mFinished = false;
                }
                break;
        }
        return true;
    }
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mLastMotionX = (int) event.getX();
//                mWidth = getWidth();
//                mMinX = mWidth / 10;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int rightMovedX = mLastMotionX - (int) event.getX();
//                if (getScrollX() + rightMovedX >= 0) {// 左侧即将滑出屏幕
//                    scrollTo(0, 0);
//                } else if ((int) event.getX() > mMinX) {// 手指处于屏幕边缘时不处理滑动
//                    scrollBy(rightMovedX, 0);
//                }
//                mLastMotionX = (int) event.getX();
//                break;
//            case MotionEvent.ACTION_UP:
//                if (-getScrollX() < mWidth / 2) {
//                    scrollBack();
//                    mIsFinish = false;
//                } else {
//                    scrollClose();
//                    mIsFinish = true;
//                }
//                break;
//        }
//        return true;
//    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        } else if (mFinished) {
            mActivity.finish();
        }
        super.computeScroll();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        mShadow.setBounds(0, 0, mShadowWidth, getHeight());
        canvas.translate(-mShadowWidth, 0);
        mShadow.draw(canvas);
        canvas.restore();
    }

    private void scrollBack() {
        int startX = getScrollX();
        int dx = -getScrollX();
        mScroller.startScroll(startX, 0, dx, 0, ANIMATION_DURING_TIME);
        invalidate();
    }

    private void scrollClose() {
        int startX = getScrollX();
        int dx = -getScrollX() - mMyWidth;
        mScroller.startScroll(startX, 0, dx, 0, ANIMATION_DURING_TIME);
        invalidate();
    }
}