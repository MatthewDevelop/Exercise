package com.foxconn.matthew.recyclerviewtest;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

/**
 * Created by Matthew on 2017/8/12.
 */

public class RefreshHeadView extends TextView implements SwipeRefreshTrigger, SwipeTrigger {
    private static final String TAG = "RefreshHeadView";

    public RefreshHeadView(Context context) {
        super(context);
    }

    public RefreshHeadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RefreshHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onRefresh() {
        //下拉到一定位置松开后调用此方法
        setText("onRefresh");
        Log.e(TAG, "onRefresh: ");
    }

    @Override
    public void onPrepare() {
        //下拉之前调用此方法
        setText("onPrepare");
        Log.e(TAG, "onPrepare: ");
    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean b1) {
        setText("onMove");
        if (!isComplete) {
            //当前Y轴偏移量大于控件高度时，标识下拉到界限，显示"松开已刷新"
            if (yScrolled > getHeight()) {

            } else {
                //未达到偏移量
            }
        }
        Log.e(TAG, "onMove: ");
    }

    @Override
    public void onRelease() {
        //达到一定滑动距离，松开时调用
        setText("onRelease");
        Log.e(TAG, "onRelease: ");

    }

    @Override
    public void onComplete() {
        //加载完成后调用此方法
        setText("onComplete");
        Log.e(TAG, "onComplete: ");

    }

    @Override
    public void onReset() {
        //重置
        setText("onReset");
        Log.e(TAG, "onReset: ");
    }


}
