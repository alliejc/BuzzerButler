package com.alisonjc.compmusicplayer.tracks;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alisonjc.compmusicplayer.EndlessScrollListener;
import com.alisonjc.compmusicplayer.R;
import com.alisonjc.compmusicplayer.RecyclerDivider;
import com.alisonjc.compmusicplayer.spotify.SpotifyService;
import com.alisonjc.compmusicplayer.spotify.model.playlist_tracklists.Item;
import com.alisonjc.compmusicplayer.spotify.model.playlist_tracklists.PlaylistTracksList;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import roboguice.fragment.RoboFragment;

public class PlaylistTracksFragment extends RoboFragment implements OnControllerTrackChangeListener, OnTrackSelectedListener {

    @Inject
    private SpotifyService mSpotifyService;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private TracksRecyclerAdapter<?> mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<Item> mPlaylistTracksList;
    private OnTrackSelectedListener mListener;
    private String mPlaylistId;
    private String mUserId;
    private int mItemPosition = 0;
    private int mTotalTracks = 0;
    private int mOffset;
    private int mLimit = 20;

    public PlaylistTracksFragment() {
    }

    public static PlaylistTracksFragment newInstance(String userId, String playlistId) {

        PlaylistTracksFragment fragment = new PlaylistTracksFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        args.putString("playlistId", playlistId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlaylistId = getArguments().getString("playlistId");
        mUserId = getArguments().getString("userId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recyclerview_list, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewSetup();
    }

    private void recyclerViewSetup() {

        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.recycler_view_divider);
        RecyclerView.ItemDecoration dividerItemDecoration = new RecyclerDivider(dividerDrawable);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mPlaylistTracksList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new TracksRecyclerAdapter<Item>(mPlaylistTracksList, getContext(), new TracksRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object item, int position) {
                mItemPosition = position;
                setCurrentPlayingSong(mItemPosition);
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        mSpotifyService.getPlaylistTracks(mUserId, mPlaylistId, mOffset, mLimit).enqueue(new Callback<PlaylistTracksList>() {
            @Override
            public void onResponse(Call<PlaylistTracksList> call, Response<PlaylistTracksList> response) {
                if (response.isSuccess() && response.body() != null) {
                    mTotalTracks = response.body().getTotal();
                    mAdapter.notifyDataSetChanged();
                    mPlaylistTracksList.addAll(response.body().getItems());
                    mAdapter.notifyDataSetChanged();

                } else if (response.code() == 401) {
                    //add logout to interface
                    //userLogout();
                }
            }

            @Override
            public void onFailure(Call<PlaylistTracksList> call, Throwable t) {
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

        mSpotifyService.getPlaylistTracks(mUserId, mPlaylistId, offset, mLimit).enqueue(new Callback<PlaylistTracksList>() {
            @Override
            public void onResponse(Call<PlaylistTracksList> call, Response<PlaylistTracksList> response) {
                if (response.isSuccess() && response.body() != null) {
                    mAdapter.notifyDataSetChanged();
                    mPlaylistTracksList.addAll(response.body().getItems());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PlaylistTracksList> call, Throwable t) {
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
        onSongSelected(mPlaylistTracksList.get(itemPosition).getTrack().getName(), mPlaylistTracksList.get(itemPosition).getTrack().getArtists().get(0).getName(), mPlaylistTracksList.get(itemPosition).getTrack().getUri());
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
