package cn.foxconn.matthew.uncaughtexception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matthew on 2017/7/12.
 */

/**
 * 处理程序中未被捕获的异常
 */

public class MyHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "MyHandler";
    private static MyHandler instance;
    private Context mContext;
    //系统默认的未捕获异常处理类
    private Thread.UncaughtExceptionHandler mDefaulltHandler;
    //存储信息
    private Map<String, String> infos = new HashMap<>();
    //格式化时间
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private MyHandler() {

    }

    public static MyHandler getInstance() {
        if (instance == null) {
            synchronized (MyHandler.class) {
                if (instance == null)
                    instance = new MyHandler();
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.mContext = context;
        mDefaulltHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);

    }


    @Override
    public void uncaughtException(Thread t, Throwable e) {
        boolean res = handleException(e);
        if (!res && mDefaulltHandler != null) {
            mDefaulltHandler.uncaughtException(t, e);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
                //Log.e("error",e1+"");
                e1.printStackTrace();
            }
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

    }

    private boolean handleException(final Throwable ex) {
        if (ex == null)
            return false;
        new Thread() {
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                ex.printStackTrace();
                String err = ex.getMessage();
                Toast.makeText(mContext, "程序出错了~" + err, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        //收集信息
        collectDeviceInfo(mContext);
        //保存日志文件
        saveInfo2File(ex);
        return true;
    }

    /**
     * 将错误信息保存到文件中
     * @param ex
     * @return
     */

    private String saveInfo2File(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            writer.close();
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String filename = "crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = "/sdcard/crash/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + filename);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return filename;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }

    /**
     * 收集设备参数信息
     * @param mContext
     */
    private void collectDeviceInfo(Context mContext) {
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }
}

