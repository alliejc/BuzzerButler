package com.example.alisonjc.compplayertwo.tracks;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alisonjc.compplayertwo.EndlessScrollListener;
import com.example.alisonjc.compplayertwo.R;
import com.example.alisonjc.compplayertwo.RecyclerDivider;
import com.example.alisonjc.compplayertwo.spotify.SpotifyService;
import com.example.alisonjc.compplayertwo.spotify.model.UserTracks.Item;
import com.example.alisonjc.compplayertwo.spotify.model.UserTracks.UserTracks;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import roboguice.fragment.RoboFragment;


public class TracksFragment extends RoboFragment implements OnControllerTrackChangeListener, OnTrackSelectedListener {

    @Inject
    private SpotifyService mSpotifyService;

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

    public TracksFragment() {
    }

    public static TracksFragment newInstance() {
        TracksFragment fragment = new TracksFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tracks, container, false);
        ButterKnife.bind(this, rootView);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.tracks_recycler_view);

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

        mAdapter = new TracksRecyclerAdapter(getContext(), mTracksList, new TracksRecyclerAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Item item, int position) {
                mItemPosition = position;
                setCurrentPlayingSong(position);
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        mSpotifyService.getUserTracks(mOffset, mLimit).enqueue(new Callback<UserTracks>() {
            @Override
            public void onResponse(Call<UserTracks> call, Response<UserTracks> response) {
                if (response.isSuccess() && response.body() != null) {
                    mTotalTracks = response.body().getTotal();
                    mAdapter.updateAdapter(response.body().getItems());
                } else if (response.code() == 401) {

                }
            }

            @Override
            public void onFailure(Call<UserTracks> call, Throwable t) {
            }
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

        mSpotifyService.getUserTracks(offset, mLimit).enqueue(new Callback<UserTracks>() {
            @Override
            public void onResponse(Call<UserTracks> call, Response<UserTracks> response) {
                if (response.isSuccess() && response.body() != null) {
                    mAdapter.updateAdapter(response.body().getItems());
                }
            }

            @Override
            public void onFailure(Call<UserTracks> call, Throwable t) {

            }
        });
    }

    private void smoothScroll(int position) {
        mRecyclerView.smoothScrollToPosition(position);
    }

    private void setCurrentPlayingSong(int itemPosition) {
        this.mItemPosition = itemPosition;
        mAdapter.recyclerViewSelector(itemPosition);
        smoothScroll(itemPosition);
        onSongSelected(mTracksList.get(itemPosition).getTrack().getName(), mTracksList.get(itemPosition).getTrack().getArtists().get(0).getName(), mTracksList.get(itemPosition).getTrack().getUri());
    }

    public void onSongSelected(String songName, String artistName, String uri) {
        if (mListener != null) {
            mListener.onTrackSelected(songName, artistName, uri);
        }
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
    }

    @Override
    public void onPause() {
        super.onPause();
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
    }

    @Override
    public void onTrackSelected(String trackName, String artistName, String uri) {

    }
}
