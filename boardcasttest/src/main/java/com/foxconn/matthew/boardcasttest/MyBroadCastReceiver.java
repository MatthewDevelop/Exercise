package com.foxconn.matthew.boardcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

public class MyBroadCastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBdCastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.e(TAG, "onReceive: ");
        Toast.makeText(context, "Received in my broadcast receiver", Toast.LENGTH_SHORT).show();
        abortBroadcast();
    }


}
