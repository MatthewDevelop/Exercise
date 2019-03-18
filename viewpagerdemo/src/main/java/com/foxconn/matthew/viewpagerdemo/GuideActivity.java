package com.foxconn.matthew.viewpagerdemo;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 2017/7/26.
 */

public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,View.OnClickListener {


    private ViewPager mPager;
    private List<View> listView;
    private int currentIndex;
    private ImageView[] dots;
    private MyPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_guide);
        initView();
        initDots();
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        dots = new ImageView[listView.size()];
        for (int i = 0; i < listView.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);
        }
        currentIndex = 0;
        dots[currentIndex].setEnabled(false);
    }

    private void initView() {
        listView = new ArrayList<>();
        listView.add(getLayoutInflater().inflate(R.layout.guide_pager1, null));
        listView.add(getLayoutInflater().inflate(R.layout.guide_pager2, null));
        listView.add(getLayoutInflater().inflate(R.layout.guide_pager3, null));
        listView.add(getLayoutInflater().inflate(R.layout.guide_pager4, null));
        adapter = new MyPagerAdapter(listView, this);
        mPager = (ViewPager) findViewById(R.id.guid_pager);
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(this);
        //mPager.setCurrentItem(listView.size()*100);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setCurrentDots(position);
    }

    private void setCurrentDots(int position) {
        if (position < 0 || position > listView.size() - 1
                || currentIndex == position) {
            return;
        }
        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        int position= (int) v.getTag();
        setCurrentView(position);
        setCurrentDots(position);
    }

    private void setCurrentView(int position) {
        if (position < 0 || position > listView.size() - 1
                || currentIndex == position) {
            return;
        }
        mPager.setCurrentItem(position);
    }
}
