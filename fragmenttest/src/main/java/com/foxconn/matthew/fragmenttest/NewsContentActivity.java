package com.foxconn.matthew.fragmenttest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Matthew on 2017/8/15.
 */

public class NewsContentActivity extends AppCompatActivity {

    public static void actionStart(Context context, String newsTitle, String newsContent) {
        Intent intent = new Intent(context, NewsContentActivity.class);
        intent.putExtra("title", newsTitle);
        intent.putExtra("content", newsContent);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);
        Intent intent = getIntent();
        String newsTitle = intent.getStringExtra("title");
        String newsContent = intent.getStringExtra("content");
        NewsContentFragment newsContentFragment =
                (NewsContentFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.news_content_fragment);
        newsContentFragment.refresh(newsTitle, newsContent);
    }
}
