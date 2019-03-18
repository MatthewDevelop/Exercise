package cn.foxconn.matthew.uncaughtexception;

import android.app.Application;

/**
 * Created by Matthew on 2017/7/12.
 */

public class MyApplication extends Application {

    private MyHandler myHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        myHandler = MyHandler.getInstance();
        myHandler.init(this);
    }
}
