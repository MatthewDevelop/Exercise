package edu.whpu.matthew.listviewtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements MyOnScrollListener.OnloadDataListener {

    //ListView展示的数据项
    private List<Student> data;
    //ListView控件
    private ListView mList;

    //自定义适配器
    MyAdapter adapter;

    //底部加载更多布局
    View footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //首先加载默认数据，这里设置为10条
        getData();
        //显示到ListView上
        showListView(data);
        //自定义的滚动监听事件
        MyOnScrollListener onScrollListener = new MyOnScrollListener(footer, data);
        //设置接口回调
        onScrollListener.setOnLoadDataListener(this);
        //设置ListView的滚动监听事件
        mList.setOnScrollListener(onScrollListener);
    }

    /**
     * 初始化ListView数据，默认设置为10条
     */
    private void getData() {
        data = new ArrayList<>();
        Student stu = null;
        for (int i = 0; i < 10; i++) {
            stu = new Student();
            stu.setName("姓名" + i);
            stu.setSex(i % 2 == 0 ? "男" : "女");
            data.add(stu);
        }
    }

    /**
     * 将数据加载到ListView上
     *
     * @param data
     */
    private void showListView(List<Student> data) {
        //首先判断适配器是否为空，首次运行肯定是为空的
        if (adapter == null) {
            //查到ListView控件
            mList = (ListView) findViewById(R.id.mList);
            //将底部加载一个加载更多的布局
            footer = LayoutInflater.from(this).inflate(R.layout.foot_boot, null);
            //初始状态为隐藏
            footer.setVisibility(View.GONE);
            //加入到ListView的底部
            mList.addFooterView(footer);
            //创建adpter数据
            adapter = new MyAdapter(data);
            //设置adapter
            mList.setAdapter(adapter);
        } else {
            //不为空，则刷新数据
            this.data = data;
            //提醒ListView重新更新数据
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onLoadData(List<Student> data) {
        //加载数据完成后，展示数据到ListView
        showListView(data);
    }
}

