package com.lisners.counsellor.zWork.commens;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.core.widget.NestedScrollView;


public abstract class PaginationNestedScrollListener implements ViewTreeObserver.OnScrollChangedListener {

    NestedScrollView nested_scroll;
    LinearLayout content_layout;


    private int currentPage = 0;
    private boolean loading = true;
    private int previousTotalItemCount = 0;
    private int visibleThreshold = 1;



    public abstract void onLoadMore(int page, int totalItemsCount, NestedScrollView view);

    public abstract int getStartPage();

    public PaginationNestedScrollListener(NestedScrollView nested_scroll, LinearLayout content_layout) {
        this.nested_scroll = nested_scroll;
        this.content_layout = content_layout;
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


        int totalItemCount = this.content_layout.getChildCount() ;



        if (totalItemCount < this.previousTotalItemCount) {
            getClass();
            this.currentPage = 0;
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
