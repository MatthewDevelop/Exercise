package com.foxconn.matthew.boardcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.foxconn.matthew.bestbroadcasttest.BaseActivity;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    //private IntentFilter intentFilter;
    //private LocalReceiver localReceiver;
    //private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //localBroadcastManager = LocalBroadcastManager.getInstance(this);
        //localReceiver = new LocalReceiver();
        //intentFilter = new IntentFilter();
        //intentFilter.addAction("com.foxconn.matthew.broadcasttest.MY_LOCALBROADCAST");
        //localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    public void OnClick(View v) {
        Log.e(TAG, "OnClick: ");
        Intent intent = new Intent("com.foxconn.matthew.broadcasttest.FORCE_OFFLINE");
        sendBroadcast(intent);
        //Intent intent = new Intent("com.foxconn.matthew.broadcasttest.MY_LOCALBROADCAST");
        //localBroadcastManager.sendBroadcast(intent);
        //Intent intent = new Intent("com.foxconn.matthew.broadcasttest.MY_BROADCAST");
        //sendBroadcast(intent, "com.foxconn.matthew.broadcasttest.MY_PERMISSION");
        //sendOrderedBroadcast(intent, "com.foxconn.matthew.broadcasttest.MY_PERMISSION");
        //sendBroadcast(intent);
    }

    /*class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Received in local receiver", Toast.LENGTH_SHORT).show();
        }
    }*/
}
