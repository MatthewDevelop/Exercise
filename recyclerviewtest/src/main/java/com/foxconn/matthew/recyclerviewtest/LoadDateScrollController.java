package com.foxconn.matthew.recyclerviewtest;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

/**
 * Created by Matthew on 2017/8/12.
 */

public class LoadDateScrollController extends RecyclerView.OnScrollListener implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "LoadDateScrollControlle";
    private LayoutManagerType mLayoutManagerType;
    private int mLastVisiablePosition;
    private int[] mLastPositions;
    private boolean isLoadData=false;
    private OnRecycleRefreshListener mListener;

    public LoadDateScrollController(OnRecycleRefreshListener mListener) {
        this.mListener = mListener;
    }

    interface OnRecycleRefreshListener {
        void refresh();

        void loadMore();
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleCount = layoutManager.getChildCount();
        int totalCount = layoutManager.getItemCount();
        Log.e(TAG, "onScrollStateChanged: " );
        if (visibleCount > 0
                && newState == RecyclerView.SCROLL_STATE_IDLE
                && mLastVisiablePosition >= totalCount - 1
                && !isLoadData) {
            //Log.e(TAG, "onScrollStateChanged: "+"loadMore" );
            if (mListener != null) {
                isLoadData = true;
                mListener.loadMore();
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        /**
         * 获取布局类型
         */
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        //第一次运行，获取布局类型
        if (mLayoutManagerType == null) {
            if (layoutManager instanceof LinearLayoutManager) {
                mLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT;
            } else if (layoutManager instanceof GridLayoutManager) {
                mLayoutManagerType = LayoutManagerType.GRID_LAYOUT;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                mLayoutManagerType = LayoutManagerType.STAGGERED_GRID_LAYOUT;
            } else {
                throw new RuntimeException("Error LayoutManager");
            }
        }

        switch (mLayoutManagerType) {
            case LINEAR_LAYOUT:
                mLastVisiablePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case GRID_LAYOUT:
                mLastVisiablePosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case STAGGERED_GRID_LAYOUT:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (mLastPositions == null) {
                    mLastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(mLastPositions);
                mLastVisiablePosition = findMax(mLastPositions);
                break;
            default:
                break;
        }


    }

    private int findMax(int[] mLastPositions) {
        int max = mLastPositions[0];
        for (int value : mLastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public void setLoadData(boolean loadData) {
        isLoadData = loadData;
    }

    @Override
    public void onRefresh() {
        if (mListener != null) {
            isLoadData = true;
            mListener.refresh();
        }
    }
}
