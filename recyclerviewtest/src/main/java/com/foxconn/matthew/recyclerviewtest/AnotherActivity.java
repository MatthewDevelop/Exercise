package com.foxconn.matthew.recyclerviewtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Matthew on 2017/8/12.
 */

public class AnotherActivity extends AppCompatActivity implements OnRefreshListener, OnLoadMoreListener {
    private List<ListItem> listItems = new ArrayList<>();
    private RecyclerView recyclerview;
    private RecyclerAdapter mAdapter;
    private SwipeToLoadLayout swipeToLoadLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);
        recyclerview = (RecyclerView) findViewById(R.id.swipe_target);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.addItemDecoration(new DividerDecoration(AnotherActivity.this));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setRefreshing(true);
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                initItems();
                mAdapter = new RecyclerAdapter(listItems);
                recyclerview.setAdapter(mAdapter);
            }
        },2000l);
    }

    private void initItems() {
        for (int i = 1; i <= 30; i++) {
            ListItem listItem = new ListItem(R.mipmap.ic_launcher, getRandomName());
            listItems.add(listItem);
        }
    }

    private String getRandomName() {
        Random random = new Random();
        int length = random.nextInt(20) + 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < length; j++) {
            stringBuilder.append("Hello world ");
        }
        return stringBuilder.toString();
    }

    @Override
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                listItems.add(new ListItem(R.mipmap.ic_launcher, "hellohellohellohel" +
                        "lohellohellohellohello "));
                mAdapter.notifyItemInserted(0);
                recyclerview.scrollToPosition(0);
                swipeToLoadLayout.setRefreshing(false);
            }
        }, 2000l);
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                initItems();
                mAdapter.notifyDataSetChanged();
                swipeToLoadLayout.setLoadingMore(false);
            }
        }, 2000l);
    }
}
