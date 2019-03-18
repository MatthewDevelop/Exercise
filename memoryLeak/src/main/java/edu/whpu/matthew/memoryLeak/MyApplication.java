package edu.whpu.matthew.memoryLeak;

import android.app.Application;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by matthew on 17-6-26.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("MyApplication",LeakCanary.isInAnalyzerProcess(this)+"");
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
