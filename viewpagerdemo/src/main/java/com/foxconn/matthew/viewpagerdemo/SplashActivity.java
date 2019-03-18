package com.foxconn.matthew.viewpagerdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Matthew on 2017/7/26.
 */

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private boolean isFirst = false;

    private static final int GO_GUIDE = 1;
    private static final int GO_HOME = 2;

    private static final long SPLASH_DELAY = 3000;

    private static final String SHAREDPREFERENCE_NAME = "is_first";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e(TAG, "handleMessage: " + msg.what);
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
            }
        }
    };

    private void goGuide() {
        Log.e(TAG, "goGuide: ");
        Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }

    private void goHome() {
        Log.e(TAG, "goHome: ");
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.e(TAG, "onCreate: ");
        init();
    }

    private void init() {
        Log.e(TAG, "init: ");
        SharedPreferences sp = getSharedPreferences(SHAREDPREFERENCE_NAME, MODE_PRIVATE);
        isFirst = sp.getBoolean("isFirst", true);
        Log.e(TAG, "" + isFirst);
        if (isFirst) {
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY);
        }
    }
}
