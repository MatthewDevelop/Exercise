package edu.whpu.matthew.memoryLeak;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView mTextView;
    //private static Inner inner=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.tv);
        TextViewHelper.getInstance(this).setText(mTextView);
        //inner=new Inner();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "destroy");
        TextViewHelper.getInstance(this).removeTv();
    }

    class Inner{

    }
}
