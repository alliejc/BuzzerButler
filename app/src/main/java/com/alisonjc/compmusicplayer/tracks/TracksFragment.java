package com.alisonjc.compmusicplayer.tracks;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alisonjc.compmusicplayer.EndlessScrollListener;
import com.alisonjc.compmusicplayer.R;
import com.alisonjc.compmusicplayer.RecyclerDivider;
import com.alisonjc.compmusicplayer.spotify.SpotifyService;
import com.alisonjc.compmusicplayer.spotify.model.UserTracks.Item;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class TracksFragment extends Fragment implements OnControllerTrackChangeListener, OnTrackSelectedListener {

    private OnTrackSelectedListener mListener;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private List<Item> mTracksList;
    private TracksRecyclerAdapter mAdapter;
    private View rootView;
    private int mItemPosition = 0;
    private int mTotalTracks = 0;
    private int mOffset;
    private int mLimit = 20;
    private static final String TAG = "TracksFragment";
    private SpotifyService mSpotifyService = SpotifyService.getSpotifyService();

    public TracksFragment() {
    }

    public static TracksFragment newInstance() {
        TracksFragment fragment = new TracksFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recyclerview_list, container, false);
        ButterKnife.bind(this, rootView);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewSetup();
    }

    private void recyclerViewSetup(){

        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.recycler_view_divider);

        mTracksList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemDecoration dividerItemDecoration = new RecyclerDivider(dividerDrawable);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new TracksRecyclerAdapter<>(mTracksList, getContext(), new TracksRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object item, int position) {
                mItemPosition = position;
                setCurrentPlayingSong(position);
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        mSpotifyService.getUserTracks(mOffset, mLimit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userTracks -> {
                    if (userTracks != null) {
                        mTotalTracks = userTracks.getTotal();
                        mAdapter.notifyDataSetChanged();
                        mTracksList.addAll(userTracks.getItems());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mSpotifyService.userLogout(getContext());
                    }
                }, throwable -> {
                }, () -> {
                });

        mRecyclerView.addOnScrollListener(new EndlessScrollListener(mLayoutManager, mTotalTracks) {
            @Override
            public void onLoadMore(int offset) {
                mOffset = offset;
                loadMoreDataFromApi(mOffset);
            }
        });
    }

    public void loadMoreDataFromApi(final int offset) {

        mSpotifyService.getUserTracks(offset, mLimit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userTracks -> {
                    if (userTracks != null) {
                        mAdapter.notifyDataSetChanged();
                        mTracksList.addAll(userTracks.getItems());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mSpotifyService.userLogout(getContext());
                    }
                }, throwable -> {
                }, () -> {
                });
    }

    private void setCurrentPlayingSong(int itemPosition) {
        this.mItemPosition = itemPosition;
        mAdapter.recyclerViewSelector(mItemPosition);
        mRecyclerView.smoothScrollToPosition(mItemPosition);
        onSongSelected(mTracksList.get(itemPosition).getTrack().getName(), mTracksList.get(itemPosition).getTrack().getArtists().get(0).getName(), mTracksList.get(itemPosition).getTrack().getUri());
    }

    public void onSongSelected(String songName, String artistName, String uri) {
        if (mListener != null) {
            mListener.onTrackSelected(songName, artistName, uri);
        }
        Log.i(TAG, "onSongSelected");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTrackSelectedListener) {
            mListener = (OnTrackSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTracksInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void onControllerTrackChange(boolean skipforward) {

        if (skipforward) {
            if (mAdapter.getItemCount() <= mItemPosition + 1) {
                mItemPosition = 0;
                setCurrentPlayingSong(mItemPosition);
            } else {
                setCurrentPlayingSong(mItemPosition + 1);
            }

        } else {
            if (mItemPosition < 1) {
                mItemPosition = 0;
                setCurrentPlayingSong(mItemPosition);
            } else {
                setCurrentPlayingSong(mItemPosition - 1);
            }
        }
        Log.i(TAG, "onControllerTackChange");
    }

    @Override
    public void onTrackSelected(String trackName, String artistName, String uri) {
        Log.i(TAG, "onTackSelected");
    }
}
