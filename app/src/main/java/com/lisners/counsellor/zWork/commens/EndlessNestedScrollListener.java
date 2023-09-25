package com.lisners.counsellor.zWork.commens;

import android.view.View;
import android.view.ViewTreeObserver;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;


public abstract class EndlessNestedScrollListener implements ViewTreeObserver.OnScrollChangedListener {

    NestedScrollView nested_scroll;


    private int currentPage = 0;
    private boolean loading = true;
    private final LayoutManager mLayoutManager;
    private int previousTotalItemCount = 0;
    private int visibleThreshold = 1;

    public abstract void onLoadMore(int page, int totalItemsCount, NestedScrollView view);

    public abstract int getStartPage();

    public EndlessNestedScrollListener(NestedScrollView nested_scroll, LinearLayoutManager layoutManager) {
        this.nested_scroll = nested_scroll;
        this.mLayoutManager = layoutManager;
        this.currentPage = getStartPage();
    }


    public void setVisibleThreshold(int visibleThreshold2) {
        this.visibleThreshold = visibleThreshold2;
    }

    private int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    @Override
    public void onScrollChanged() {


        View view = nested_scroll.getChildAt(nested_scroll.getChildCount() - 1);
        int diff = (view.getBottom() - (nested_scroll.getHeight() + nested_scroll
                .getScrollY()));
        diff = diff - visibleThreshold;


        int totalItemCount = this.mLayoutManager.getItemCount() - 1;

        if (totalItemCount < this.previousTotalItemCount) {
            getClass();
            this.currentPage = getStartPage();
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        if (this.loading && totalItemCount > this.previousTotalItemCount) {
            this.loading = false;
            this.previousTotalItemCount = totalItemCount;
        }


        /*Log.e("loading", "===>" + loading);
        Log.e("last_visible", "===>" + diff);
        Log.e("total_item", "===>" + totalItemCount);*/

        if (!this.loading && diff <= 0) {
            this.currentPage++;
            onLoadMore(this.currentPage, totalItemCount, nested_scroll);
            this.loading = true;
        }

    }

    public void resetState() {
        getClass();
        this.currentPage = getStartPage();
        this.previousTotalItemCount = 0;
        this.loading = true;
    }


}
