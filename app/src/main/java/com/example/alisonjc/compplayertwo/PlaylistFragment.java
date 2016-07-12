package com.example.alisonjc.compplayertwo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;

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
import roboguice.inject.InjectView;


public class PlaylistFragment extends RoboFragment {

    @InjectView(R.id.playlistview)
    private ListView mListView;

    @Inject
    private SpotifyService mSpotifyService;

    private PlaylistInteractionListener mListener;
    private static PlaylistAdapter mPlaylistAdapter;
    private String playlistId = "";


    public PlaylistFragment() {
        // Required empty public constructor
    }


    public static PlaylistFragment newInstance() {
        PlaylistFragment fragment = new PlaylistFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState == null) {
            mListView = (ListView) view.findViewById(R.id.playlistview);
            listViewSetup();
        }

    }

    private void listViewSetup() {

        mPlaylistAdapter = new PlaylistAdapter(getActivity(), R.layout.item_playlist, new ArrayList<Item>());

        mListView.setAdapter(mPlaylistAdapter);

        mSpotifyService.getUserPlayLists().enqueue(new Callback<UserPlaylists>() {
            @Override
            public void onResponse(Call<UserPlaylists> call, Response<UserPlaylists> response) {
                if (response.isSuccess() && response.body() != null) {
                    updateListView(response.body().getItems());
                } else if(response.code() == 401){
                    //add logout to interface
                    //userLogout();
                }
            }

            @Override
            public void onFailure(Call<UserPlaylists> call, Throwable t) {

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Item mItem = (Item) parent.getAdapter().getItem(position);
                String playlistId = mItem.getId();
                mListener.onPlaylistSelected(playlistId);

                Animation animation1 = new AlphaAnimation(0.1f, 0.3f);
                animation1.setDuration(1000);
                view.startAnimation(animation1);
            }
        });
    }

    private void updateListView(List<Item> items) {

        mPlaylistAdapter.clear();
        mPlaylistAdapter.addAll(items);
        mPlaylistAdapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String string) {
        if (mListener != null) {
            mListener.onPlaylistSelected(string);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PlaylistInteractionListener) {
            mListener = (PlaylistInteractionListener) context;
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



    public interface PlaylistInteractionListener {
        void onPlaylistSelected(String playlistId);
    }

}

