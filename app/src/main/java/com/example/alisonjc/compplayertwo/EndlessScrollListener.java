//package com.example.alisonjc.compplayertwo;
//
//import android.support.v7.widget.RecyclerView;
//import android.widget.AbsListView;
//
//public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
//
//    // The minimum number of items to have below current scroll position
//    // before loading more.
//    private int visibleThreshold = 10;
//
//    // The current offset index of loaded data
//    private int currentPage = 0;
//
//    // The total number of items in the dataset after the last load
//    private int previousTotalItemCount = 0;
//
//    // True if we are still waiting for the last set of data to load.
//    private boolean loading = true;
//
//    // Sets the starting page index
//    private int startingPageIndex = 0;
//
//
//    public EndlessScrollListener() {
//    }
//
//    public EndlessScrollListener(int visibleThreshold) {
//        this.visibleThreshold = visibleThreshold;
//    }
//
//    public EndlessScrollListener(int visibleThreshold, int startPage) {
//        this.visibleThreshold = visibleThreshold;
//        this.startingPageIndex = startPage;
//        this.currentPage = startPage;
//    }
//
//    @Override
//    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//        super.onScrollStateChanged(recyclerView, newState);
//    }
//
//    @Override
//    public void onScrolled(RecyclerView recyclerView, int horizontalScroll, int verticalScroll) {
//        super.onScrolled(recyclerView, horizontalScroll, verticalScroll);
//    }
//
//    // This happens many times a second during a scroll
//    // We are given a few useful parameters to help us work out if we need to load some more data,
//    // but first we check if we are waiting for the previous load to finish.
//
//    @Override
//    public void onScroll(RecyclerView recyclerView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//        if (totalItemCount < previousTotalItemCount) {
//            this.currentPage = this.startingPageIndex;
//            this.previousTotalItemCount = totalItemCount;
//            if (totalItemCount == 0) {
//                this.loading = true;
//            }
//        }
//
//        if (loading && (totalItemCount > previousTotalItemCount)) {
//            loading = false;
//            previousTotalItemCount = totalItemCount;
//            currentPage++;
//        }
//
//        if (!loading && (firstVisibleItem + visibleItemCount + visibleThreshold) >= totalItemCount) {
//            loading = onLoadMore(currentPage + 1, totalItemCount);
//        }
//    }
//
//    // Defines the process for actually loading more data based on page
//    // Returns true if more data is being loaded; returns false if there is no more data to load.
//    public abstract boolean onLoadMore(int page, int totalItemsCount);
//
//
//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//    }
//
//}
