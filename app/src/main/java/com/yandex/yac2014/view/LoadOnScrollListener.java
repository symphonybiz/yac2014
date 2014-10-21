package com.yandex.yac2014.view;

import android.widget.AbsListView;

/**
 * Created by 7times6 on 17.10.14.
 */
public abstract class LoadOnScrollListener implements AbsListView.OnScrollListener {

    private int currentFirstVisibleItem;
    private int currentVisibleItemCount;
    private int currentScrollState;
    private int totalItemCount;

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (firstVisibleItem > currentFirstVisibleItem) {
            onScrollDown();
        } else if (firstVisibleItem < currentFirstVisibleItem) {
            onScrollUp();
        }

        this.currentFirstVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = visibleItemCount;
        this.totalItemCount = totalItemCount;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.currentScrollState = scrollState;
        boolean bottomVisible = currentFirstVisibleItem + currentVisibleItemCount == totalItemCount;
        if (bottomVisible
                && currentVisibleItemCount > 0
                && currentScrollState == SCROLL_STATE_IDLE)
        {
            onDataLoadRequest();
        }
    }

    public abstract void onDataLoadRequest();

    public abstract void onScrollUp();

    public abstract void onScrollDown();

}
