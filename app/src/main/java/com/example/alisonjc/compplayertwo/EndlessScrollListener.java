package com.example.alisonjc.compplayertwo;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private int mTotalItemCount;
    private int mOffset = 0;
    private int mVisibleItemCount = 0;
    private int mVisibleThreshold = 13;
    private int mFirstVisibleItem = 0;
    private int mPreviousTotal = 0;

    private boolean mLoading = true; // True if we are still waiting for the last set of data to load.
    private LinearLayoutManager mLinearLayoutManager;

    public EndlessScrollListener(LinearLayoutManager linearLayoutManager, int totalItemCount) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.mTotalItemCount = totalItemCount;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dy > 0) {
            mVisibleItemCount = recyclerView.getChildCount();
            mTotalItemCount = mLinearLayoutManager.getItemCount();
            mFirstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

            if (mLoading) {
                if (mTotalItemCount > mPreviousTotal) {
                    mLoading = false;
                    mPreviousTotal = mTotalItemCount;
                }
            }
            if (!mLoading && (mTotalItemCount - mVisibleItemCount) <= (mFirstVisibleItem + mVisibleThreshold)) {
                mOffset = mOffset + 20;
                onLoadMore(mOffset);
                mLoading = true;
            }
        }
    }

    // Returns true if more data is being loaded; returns false if there is no more data to load.
    public abstract void onLoadMore(int offset);

}