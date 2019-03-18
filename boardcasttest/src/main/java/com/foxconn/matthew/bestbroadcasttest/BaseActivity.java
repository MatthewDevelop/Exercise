package com.foxconn.matthew.bestbroadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Matthew on 2017/8/22.
 */

public class BaseActivity extends AppCompatActivity {
    private OffLineBroadCastReceiver offLineBroadCastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.foxconn.matthew.broadcasttest.FORCE_OFFLINE");
        offLineBroadCastReceiver = new OffLineBroadCastReceiver();
        registerReceiver(offLineBroadCastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (offLineBroadCastReceiver != null)
            unregisterReceiver(offLineBroadCastReceiver);
        offLineBroadCastReceiver = null;
    }

    private class OffLineBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Warning!");
            builder.setMessage("You are forced to offline, please try to login again");
            builder.setCancelable(false);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCollector.finishAll();
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }
            });
            builder.show();
        }
    }
}
