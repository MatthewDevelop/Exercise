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

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

/**
 * Created by Matthew on 2017/8/14.
 */

public class CircleRefreshHeaderView extends RelativeLayout implements SwipeTrigger, SwipeRefreshTrigger {
    private CircleView mCircleView;
    private TextView mDesText;
    private ObjectAnimator anim;
    private boolean isRelease;

    public CircleRefreshHeaderView(Context context) {
        super(context);
        initViews();
    }

    public CircleRefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public CircleRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    public CircleRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews();
    }

    private void initViews() {
        int circleWidth =
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        mCircleView = new CircleView(getContext());
        LinearLayout.LayoutParams circleLayoutParams = new LinearLayout.LayoutParams(circleWidth, circleWidth);
        mCircleView.setLayoutParams(circleLayoutParams);
        mDesText = new TextView(getContext());
        LinearLayout.LayoutParams desLayoutParams =
                new LinearLayout.LayoutParams(circleWidth * 3, ViewGroup.LayoutParams.WRAP_CONTENT);
        desLayoutParams.gravity = Gravity.CENTER;
        desLayoutParams.setMargins(circleWidth / 2, 0, 0, 0);
        mDesText.setLayoutParams(desLayoutParams);
        mDesText.setTextSize(12);
        mDesText.setTextColor(Color.GRAY);
        mDesText.setText("下拉即可刷新");

        //添加线性的父布局
        LinearLayout linearLayout = new LinearLayout(getContext());
        RelativeLayout.LayoutParams llLayoutParams =
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llLayoutParams.addRule(CENTER_IN_PARENT);
        linearLayout.setLayoutParams(llLayoutParams);
        linearLayout.setPadding(10, 10, 10, 10);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(mCircleView);
        linearLayout.addView(mDesText);
        addView(linearLayout);
    }

    @Override
    public void onRefresh() {
        //开始刷新，启动动画
        anim = ObjectAnimator.ofFloat(mCircleView, "rotation", mCircleView.getRotation(), mCircleView.getRotation() + 360f).setDuration(500);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.start();
        mDesText.setText("正在加载数据~");
    }

    @Override
    public void onPrepare() {
        isRelease = false;
    }

    @Override
    public void onMove(int yScroll, boolean isComplete, boolean b1) {
        if (!isComplete) {
            if (Math.abs(yScroll) < getHeight()) {
                mDesText.setText("下拉刷新");
            } else {
                mDesText.setText("松开加载更多");
            }
            if (!isRelease)
                mCircleView.setRotation((float) yScroll / getHeight() * 360f);
        }
    }

    @Override
    public void onRelease() {
        isRelease = true;
    }

    @Override
    public void onComplete() {
        anim.cancel();
        mDesText.setText("加载完成");
    }

    @Override
    public void onReset() {
        //将动画置为初始状态
        mCircleView.setRotation(0f);
    }
}
