package com.example.alisonjc.compplayertwo;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alisonjc.compplayertwo.spotify.SpotifyService;
import com.example.alisonjc.compplayertwo.spotify.model.playlists.Item;
import com.example.alisonjc.compplayertwo.spotify.model.playlists.UserPlaylists;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import roboguice.fragment.RoboFragment;


public class PlaylistFragment2 extends RoboFragment {

    @Inject
    private SpotifyService mSpotifyService;

    private PlaylistViewHolder mViewHolder;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PlaylistRecyclerAdapter mAdapter;
    private List<Item> mPlaylistItemList = new ArrayList<Item>();
    private int itemPosition;
    private Item mItem;


    public PlaylistFragment2() {
    }

    public static PlaylistFragment2 newInstance() {
        PlaylistFragment2 fragment = new PlaylistFragment2();

        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_playlist, container);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        listViewSetup();

        //plug in linear layoutManager
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //plug in Adapter
        mAdapter = new PlaylistRecyclerAdapter(getContext(), mPlaylistItemList);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;

    }


    private void listViewSetup() {

        mSpotifyService.getUserPlayLists().enqueue(new Callback<UserPlaylists>() {
            @Override
            public void onResponse(Call<UserPlaylists> call, Response<UserPlaylists> response) {
                if (response.isSuccess() && response.body() != null) {
                    updateListView(response.body().getItems());
                } else if (response.code() == 401) {
                    //userLogout();
                }
            }

            @Override
            public void onFailure(Call<UserPlaylists> call, Throwable t) {

            }
        });
    }
    private void updateListView(List<Item> items) {

        mAdapter.clearAdapter();

    }
}


