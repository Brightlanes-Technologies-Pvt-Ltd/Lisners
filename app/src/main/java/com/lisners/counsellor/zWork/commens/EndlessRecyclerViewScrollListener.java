package com.lisners.counsellor.zWork.commens;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private int currentPage = 0;
    private boolean loading = true;
    private final LayoutManager mLayoutManager;
    private int previousTotalItemCount = 0;
    private int visibleThreshold = 1;

    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView recyclerView);
    public abstract int getStartPage();

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        this.currentPage = getStartPage();
    }

    public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        this.currentPage = getStartPage();
        //this.visibleThreshold *= layoutManager.getSpanCount();
    }

    public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        this.currentPage = getStartPage();
        //this.visibleThreshold *= layoutManager.getSpanCount();
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
    public void onScrolled(RecyclerView view, int dx, int dy) {


       /* if (isLoading && recyclerView.getAdapter().getItemCount() > 1) {

            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            System.out.println("last item :" + lastVisibleItemPosition);
            System.out.println("total item :" + recyclerView.getAdapter().getItemCount());
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1) {
                System.out.println("page :" + pagecount);

                isLoading = false;
                ++pagecount;
                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 0);

            }
        }*/




        int lastVisibleItemPosition = 0;
        int totalItemCount = this.mLayoutManager.getItemCount()-1;


        LayoutManager layoutManager = this.mLayoutManager;
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            lastVisibleItemPosition = getLastVisibleItem(((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null));
        } else if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }





/*
        if(lastVisibleItemPosition != RecyclerView.NO_POSITION)
*/





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
        /*Log.e("loading", "===>"+loading);
        Log.e("last_visible", "===>"+(visibleThreshold+lastVisibleItemPosition));
        Log.e("total_item", "===>"+totalItemCount);*/

        if (!this.loading && this.visibleThreshold + lastVisibleItemPosition > totalItemCount) {
            this.currentPage++;
            onLoadMore(this.currentPage, totalItemCount, view);
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