package com.foxconn.matthew.recyclerviewtest;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;


/**
 * Created by Matthew on 2017/8/14.
 */

public class CircleLoadMoreFooterView extends RelativeLayout implements SwipeLoadMoreTrigger, SwipeTrigger {
    private CircleView mCircleView;
    private boolean isRelease;
    private TextView mDesTextView;
    private ObjectAnimator anim;

    public CircleLoadMoreFooterView(Context context) {
        super(context);
        init();
    }


    public CircleLoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CircleLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        int circleWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        mCircleView = new CircleView(getContext());
        LinearLayout.LayoutParams circleLayoutParams = new LinearLayout.LayoutParams(circleWidth, circleWidth);
        mCircleView.setLayoutParams(circleLayoutParams);
        mDesTextView = new TextView(getContext());
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayoutParams.gravity = Gravity.CENTER;
        textLayoutParams.setMargins(circleWidth / 2, 0, 0, 0);
        mDesTextView.setLayoutParams(textLayoutParams);
        mDesTextView.setTextSize(12);
        mDesTextView.setTextColor(Color.GRAY);
        mDesTextView.setText("上拉即可加载");


        LinearLayout linearLayout = new LinearLayout(getContext());
        RelativeLayout.LayoutParams llLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        llLayoutParams.addRule(CENTER_IN_PARENT);
        linearLayout.setLayoutParams(llLayoutParams);
        linearLayout.setPadding(10, 10, 10, 10);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(mCircleView);
        linearLayout.addView(mDesTextView);
        addView(linearLayout);
    }

    @Override
    public void onLoadMore() {
        anim = ObjectAnimator.ofFloat(mCircleView, "rotation", mCircleView.getRotation(), mCircleView.getRotation() + 360f);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.start();
        mDesTextView.setText("正在加载~");
    }

    @Override
    public void onPrepare() {
        isRelease = false;
    }

    @Override
    public void onMove(int yScrolls, boolean isComplete, boolean b1) {
        if (!isComplete) {
            if (Math.abs(yScrolls) > getHeight()) {
                mDesTextView.setText("松开加载更多");
            } else {
                mDesTextView.setText("上拉加载");
            }
            if (!isRelease)
                mCircleView.setRotation((float) yScrolls / getHeight() * 360f);
        }
    }

    @Override
    public void onRelease() {
        isRelease = true;
    }

    @Override
    public void onComplete() {
        anim.cancel();
        mDesTextView.setText("加载完成");
    }

    @Override
    public void onReset() {
        mDesTextView.setRotation(0f);
    }
}
