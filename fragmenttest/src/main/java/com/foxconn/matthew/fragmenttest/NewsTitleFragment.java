package com.foxconn.matthew.fragmenttest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Matthew on 2017/8/15.
 */

public class NewsTitleFragment extends Fragment {

    private View view;
    private boolean isTwoPage;
    private NewsContentFragment newsContentFragment;
    private List<News> list;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.news_content_layout) != null) {
            isTwoPage = true;
            newsContentFragment = (NewsContentFragment) getFragmentManager().findFragmentById(R.id.news_content_fragment);
            newsContentFragment.refresh(list.get(0).getTitle(), list.get(0).getContent());
        } else {
            isTwoPage = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_title_frag, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.news_title_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        NewsAdapter adapter = new NewsAdapter(getNews());
        recyclerView.setAdapter(adapter);
        return view;
    }

    private List<News> getNews() {
        list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            News news = new News();
            news.setTitle("This is title" + i);
            news.setContent(getRandomContent("This is content" + i + "\n"));
            list.add(news);
        }
        return list;
    }

    private String getRandomContent(String content) {
        Random random = new Random();
        int length = random.nextInt(20) + 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(content);
        }
        return sb.toString();
    }

    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
        List<News> newsList;

        public NewsAdapter(List<News> newsList) {
            this.newsList = newsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.newsTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    News news = newsList.get(holder.getAdapterPosition());
                    if (isTwoPage) {
                        newsContentFragment.refresh(news.getTitle(), news.getContent());
                    } else {
                        NewsContentActivity.actionStart(parent.getContext(), news.getTitle(), news.getContent());
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.newsTitleTextView.setText(newsList.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView newsTitleTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                newsTitleTextView = (TextView) itemView.findViewById(R.id.news_title);
            }
        }
    }

}
