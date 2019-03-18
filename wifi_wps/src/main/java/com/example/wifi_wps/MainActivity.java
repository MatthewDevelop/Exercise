package com.example.wifi_wps;

import android.app.Activity;
import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.IWifiManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

import static java.util.Locale.CHINA;

public class MainActivity extends Activity {
    private WifiManager mWifiManager;


    private static final String EXTRA_ENABLE_NEXT_ON_CONNECT = "wifi_enable_next_on_connect";

    // this boolean extra specifies whether to auto finish when connection is established
    private static final String EXTRA_AUTO_FINISH_ON_CONNECT = "wifi_auto_finish_on_connect";

    // this boolean extra shows a custom button that we can control
    protected static final String EXTRA_SHOW_CUSTOM_BUTTON = "wifi_show_custom_button";

    // show a text regarding data charges when wifi connection is required during setup wizard
    protected static final String EXTRA_SHOW_WIFI_REQUIRED_INFO = "wifi_show_wifi_required_info";

    // this boolean extra is set if we are being invoked by the Setup Wizard
    private static final String EXTRA_IS_FIRST_RUN = "firstRun";


    private static final int WIFI_DIALOG_ID = 1;
    private static final int WPS_PBC_DIALOG_ID = 2;
    private static final int WPS_PIN_DIALOG_ID = 3;
    private static final int WIFI_SKIPPED_DIALOG_ID = 4;
    private static final int WIFI_AND_MOBILE_SKIPPED_DIALOG_ID = 5;

    public static final String WPS_LISTENER = "android.net.wifi.WifiManager$WpsListener";
    WpsDialog wps;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wps = new WpsDialog(this, 0);
        Button wpsbtn = (Button) findViewById(R.id.custom_button);
        wpsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wps.show();
                WindowManager.LayoutParams params = wps.getWindow().getAttributes();
                params.width = 700;
                params.height = 500;
                wps.getWindow().setAttributes(params);
            }
        });

        //更改语言
        //updateLanguage(CHINA);


    }



  /*  private void updateLanguage(Locale locale) {


        try { IActivityManager am = ActivityManagerNative.getDefault();
            Configuration config = am.getConfiguration();
            config.locale =locale;
            // indicate this isn't some passing default - the user wants this remembered
            config.userSetLocale = true;
            am.updateConfiguration(config);
            // Trigger the dirty bit for the Settings Provider.
            BackupManager.dataChanged("com.android.providers.settings");
        } catch (RemoteException e) {
            // Intentionally left blank
        }
    }*/


}