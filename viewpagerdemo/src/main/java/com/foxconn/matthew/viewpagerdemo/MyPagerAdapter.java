package com.foxconn.matthew.viewpagerdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

/**
 * Created by Matthew on 2017/7/26.
 */

class MyPagerAdapter extends PagerAdapter {
    private List<View> mListView;
    private Activity activity;
    private static final String SHAREDPREFERENCE_NAME = "is_first";

    public MyPagerAdapter(List<View> viewList, Activity activity) {
        this.mListView = viewList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        if (mListView != null)
            return mListView.size();
        return 0;
        //return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mListView.get(position));
        //container.addView(mListView.get(position%mListView.size()));
        if (activity != null) {
            if (position == mListView.size() - 1) {
                Button startButton = (Button) mListView.get(position).findViewById(R.id.start);
                startButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setGuide();
                        goHome();
                    }
                });
            }
        }
        return mListView.get(position);
    }

    private void goHome() {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    private void setGuide() {
        SharedPreferences sp = activity.getSharedPreferences(SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isFirst", false);
        editor.commit();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView(mListView.get(position));
        //super.destroyItem(container, position, object);
    }
}


