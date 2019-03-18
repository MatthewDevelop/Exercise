package com.foxconn.matthew.viewpagerdemo;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager mPager;
    private List<View> listView;
    private ImageView cursor;
    private TextView tv1, tv2, tv3;
    private int offset = 0;
    private int currIndex = 0;
    private int bmpW;
    private MyOnClickListener myOnClickListener = new MyOnClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitImageView();
        InitTextView();
        InitViewPager();
    }

    private void InitTextView() {
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);

        tv1.setOnClickListener(myOnClickListener);
        tv2.setOnClickListener(myOnClickListener);
        tv3.setOnClickListener(myOnClickListener);

    }

    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.pager);
        listView = new ArrayList<View>();
        listView.add(getLayoutInflater().inflate(R.layout.pager1, null));
        listView.add(getLayoutInflater().inflate(R.layout.pager2, null));
        listView.add(getLayoutInflater().inflate(R.layout.pager3, null));
        mPager.setAdapter(new MyPagerAdapter(listView,null));
        //mPager.setCurrentItem(0);
        //mPager.setCurrentItem(listView.size()*100);
        mPager.setOnPageChangeListener(new MyOnPageChangeLister());
    }

    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a).getWidth();
        DisplayMetrics dm = new DisplayMetrics();//获取图片宽度
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;//获取屏幕分辨率宽度
        offset = (screenW / 3 - bmpW) / 2;//计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);//设置动画的初始位置
    }

    private class MyOnClickListener implements View.OnClickListener {

        /*private int index = 0;

        public MyOnClickListener(int indexNum) {
            index = indexNum;
        }*/

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv1:
                    mPager.setCurrentItem(0);
                    break;
                case R.id.tv2:
                    mPager.setCurrentItem(1);
                    break;
                case R.id.tv3:
                    mPager.setCurrentItem(2);
                    break;
            }

        }
    }

    private class MyOnPageChangeLister implements ViewPager.OnPageChangeListener {
        int one = offset * 2 + bmpW;//第一页到第二页便宜量
        int two = one * 2;//第一页到第三页的偏移量

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            switch (position) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    }
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    }
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    }
                    break;
            }
            currIndex = position;
            animation.setFillAfter(true);//true图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
