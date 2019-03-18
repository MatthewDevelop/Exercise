package com.foxconn.matthew.bestbroadcasttest;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 2017/8/22.
 */

public class ActivityCollector {
    public static List<Activity> activitys = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activitys.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activitys.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activitys) {
            if (!activity.isFinishing())
                activity.finish();
        }
    }
}
