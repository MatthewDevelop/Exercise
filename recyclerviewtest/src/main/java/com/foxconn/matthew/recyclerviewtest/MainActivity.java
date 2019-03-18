package com.foxconn.matthew.recyclerviewtest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements LoadDateScrollController.OnRecycleRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadDateScrollController controller;

    private List<ListItem> listItems = new ArrayList<>();

    private RecyclerAdapter mAdapter;
    private RecyclerView recyclerview;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_add:
                listItems.add(new ListItem(R.mipmap.ic_launcher, "hellohellohellohel" +
                        "lohellohellohellohello "));
                mAdapter.notifyItemInserted(0);
                recyclerview.scrollToPosition(0);
                break;
            case R.id.menu_del:
                listItems.remove(0);
                mAdapter.notifyItemRemoved(0);
                break;
            case R.id.menu_move:
                mAdapter.notifyItemMoved(3, 6);
                break;
            case R.id.menu_addmore:
                listItems.add(new ListItem(R.mipmap.ic_launcher, "hellohellohellohel" +
                        "lohellohellohellohello "));
                listItems.add(new ListItem(R.mipmap.ic_launcher, "hellohellohellohel" +
                        "lohellohellohellohello "));
                listItems.add(new ListItem(R.mipmap.ic_launcher, "hellohellohellohel" +
                        "lohellohellohellohello "));
                listItems.add(new ListItem(R.mipmap.ic_launcher, "hellohellohellohel" +
                        "lohellohellohellohello "));
                mAdapter.notifyItemRangeInserted(2, 6);
                recyclerview.scrollToPosition(2);
                break;
            case R.id.another_activity:
                Intent intent = new Intent(MainActivity.this, AnotherActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        recyclerview = (RecyclerView) findViewById(R.id.recycle_view);
        //GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 3);
        //layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.addItemDecoration(new DividerDecoration(MainActivity.this));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        controller = new LoadDateScrollController(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        recyclerview.addOnScrollListener(controller);
        swipeRefreshLayout.setOnRefreshListener(controller);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                initItems();
                mAdapter = new RecyclerAdapter(listItems);
                recyclerview.setAdapter(mAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000l);
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
    public void refresh() {
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                /*initItems();
                mAdapter.notifyDataSetChanged();*/
                listItems.add(new ListItem(R.mipmap.ic_launcher, "hellohellohellohel" +
                        "lohellohellohellohello "));
                mAdapter.notifyItemInserted(0);
                recyclerview.scrollToPosition(0);
                swipeRefreshLayout.setRefreshing(false);
                controller.setLoadData(false);
            }
        }, 2000);
    }

    @Override
    public void loadMore() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("正在加载~");
        pd.show();
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                initItems();
                mAdapter.notifyDataSetChanged();
                controller.setLoadData(false);
                pd.dismiss();
            }
        }, 2000);

    }
}
