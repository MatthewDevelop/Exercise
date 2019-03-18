package com.foxconn.matthew.recyclerviewtest;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

/**
 * Created by Matthew on 2017/8/12.
 */

public class LoadMoreFooterView extends TextView implements SwipeTrigger, SwipeLoadMoreTrigger {
    private static final String TAG = "LoadMoreFooterView";

    public LoadMoreFooterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreFooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LoadMoreFooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public LoadMoreFooterView(Context context) {
        super(context);
    }

    @Override
    public void onLoadMore() {
        setText("onLoadMore");
        Log.e(TAG, "onLoadMore: ");
    }

    @Override
    public void onPrepare() {
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
        setText("onRelease");
        Log.e(TAG, "onRelease: ");
    }

    @Override
    public void onComplete() {
        setText("onComplete");
        Log.e(TAG, "onComplete: ");
    }

    @Override
    public void onReset() {
        setText("onReset");
        Log.e(TAG, "onReset: ");
    }
}
