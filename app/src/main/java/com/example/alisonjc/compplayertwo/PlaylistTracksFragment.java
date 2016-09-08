package com.example.alisonjc.compplayertwo;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.alisonjc.compplayertwo.spotify.SpotifyPlayer;
import com.example.alisonjc.compplayertwo.spotify.SpotifyService;
import com.example.alisonjc.compplayertwo.spotify.model.playlist_tracklists.Item;
import com.example.alisonjc.compplayertwo.spotify.model.playlist_tracklists.PlaylistTracksList;
import com.google.inject.Inject;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.PlayerStateCallback;
import com.spotify.sdk.android.player.Spotify;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class PlaylistTracksFragment extends RoboFragment {

    @Inject
    private SpotifyService mSpotifyService;

    @Inject
    private SpotifyPlayer mSpotifyPlayer;

    @InjectView(R.id.tracksview)
    private ListView mListView;

    @InjectView(R.id.play)
    private ImageButton mPlayButton;

    @InjectView(R.id.pause)
    private ImageButton mPauseButton;

    @InjectView(R.id.seekerBarView)
    private SeekBar mSeekBar;

    @InjectView(R.id.musicCurrentLoc)
    private TextView mSongLocationView;

    @InjectView(R.id.musicDuration)
    private TextView mSongDurationView;

    @InjectView(R.id.radio_group)
    private RadioGroup mRadioGroup;

    @InjectView(R.id.one_minute_thirty)
    private RadioButton mOneThirtyMin;

    @InjectView(R.id.two_minutes)
    private RadioButton mTwoMin;

    private int mSongLocation;
    private Timer mTimer;
    private Handler seekHandler = new Handler();
    private Player mPlayer;
    private static PlaylistTracksAdapter mPlaylistTracksAdapter;
    private View rootView;
    private OnPlaylistTracksInteractionListener mListener;
    private String mPlaylistId;
    private String mUserId;
    private int mItemPosition = 0;
    private int mPauseTimeAt = 90000;
    private boolean mBeepPlayed = false;
    private Item mTrackItem;
    private String mTrackName;

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
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tracks, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSongLocationView.setText("0:00");
        mSongDurationView.setText(R.string.one_thirty_radio_button);

        mListView = (ListView) view.findViewById(R.id.tracksview);

        mPlayer = mSpotifyPlayer.getPlayer(getContext());

        playerControlsSetup();
        listViewSetup();
        startTimerTask();

        mListView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {

                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);

                return true; // ONLY if more data is actually being loaded; false otherwise
            }
        });


        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                onRadioButtonClicked(checkedId);
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mPlayer != null && fromUser) {
                    mPlayer.seekToPosition(progress);
                    mSeekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        super.onOptionsItemSelected(item);
//
//        switch (item.getItemId()) {
//
//            case R.menu.fragment_overflow:
//
//                MenuItem.OnMenuItemClickListener menuListener = new MenuItem.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//
//                        return false;
//                    }
//                };
//
//                return true;
//
//            case R.menu.main_overflow:
//
//                return false;
//
//            case android.R.id.home:
//
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    private void listViewSetup() {

        mPlaylistTracksAdapter = new PlaylistTracksAdapter(getActivity(), R.layout.item_track, new ArrayList<Item>());
        mListView.setAdapter(mPlaylistTracksAdapter);
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        mSpotifyService.getPlaylistTracks(mUserId, mPlaylistId).enqueue(new Callback<PlaylistTracksList>() {
            @Override
            public void onResponse(Call<PlaylistTracksList> call, Response<PlaylistTracksList> response) {
                if (response.isSuccess() && response.body() != null) {
                    updateListView(response.body().getItems());


                } else if (response.code() == 401) {
                    //add logout to interface
                    //userLogout();
                }
            }

            @Override
            public void onFailure(Call<PlaylistTracksList> call, Throwable t) {

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                setCurrentPlayingSong(position);
                playSong(position);
                showPauseButton();

            }
        });
    }

    private void playSong(int locationid) {

        mBeepPlayed = false;
        showPauseButton();
        setCurrentPlayingSong(locationid);
        mPlayer.play("spotify:track:" + mPlaylistTracksAdapter.getItem(locationid).getTrack().getId());
        setSeekBar();
    }

    private void startTimerTask() {

        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mPlayer.getPlayerState(new PlayerStateCallback() {
                    @Override
                    public void onPlayerState(PlayerState playerState) {

                        if (mSongLocation >= mPauseTimeAt - 10000 && !mBeepPlayed) {
                            playBeep();
                            mBeepPlayed = true;
                        }
                        if (mSongLocation >= mPauseTimeAt) {
                            mPlayer.pause();
                            onSkipNextClicked();
                        }
                    }
                });
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTimerTask, 1000, 1000);
    }

    private void setSeekBar() {

        if (mPlayer != null) {
            mPlayer.getPlayerState(new PlayerStateCallback() {

                @Override
                public void onPlayerState(PlayerState playerState) {

                    mSongLocation = playerState.positionInMs;
                    mSeekBar.setMax(mPauseTimeAt);
                    mSeekBar.setProgress(mSongLocation);

                    int seconds = ((mSongLocation / 1000) % 60);
                    int minutes = ((mSongLocation / 1000) / 60);

                    mSongLocationView.setText(String.format("%2d:%02d", minutes, seconds, 0));
                }
            });
        }

        seekHandler.postDelayed(run, 1000);
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            setSeekBar();
        }
    };

    private void playerControlsSetup() {

        View playerControls = rootView.findViewById(R.id.music_player);

        assert playerControls != null;
        playerControls.findViewById(R.id.skip_previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPreviousClicked();
            }
        });

        playerControls.findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayClicked();
            }
        });

        playerControls.findViewById(R.id.pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPauseClicked();
            }
        });

        playerControls.findViewById(R.id.skip_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSkipNextClicked();
            }
        });
    }

    private void listviewSelector() {

        mListView.clearChoices();
        mListView.setItemChecked(mItemPosition, true);
        mListView.smoothScrollToPosition(mItemPosition);
        mListView.setSelected(true);
        mPlaylistTracksAdapter.notifyDataSetChanged();
    }

    private void playBeep() {

        final MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.beep);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        });
    }

    private void setCurrentPlayingSong(int itemPosition) {

        this.mItemPosition = itemPosition;
        listviewSelector();


    }

    private void onPauseClicked() {

        if (mPlayer == null) {
            //Toast.makeText(this, "Please select a song", Toast.LENGTH_SHORT).show();
        } else {
            mPlayer.pause();
            showPlayButton();
        }
    }

    private void showPauseButton() {

        mPlayButton.setVisibility(View.GONE);
        mPauseButton.setVisibility(View.VISIBLE);
    }

    private void showPlayButton() {

        mPauseButton.setVisibility(View.GONE);
        mPlayButton.setVisibility(View.VISIBLE);
    }

    private void onPlayClicked() {

        if (mPlayer == null) {
            //Toast.makeText(this, "Please select a song", Toast.LENGTH_SHORT).show();
        } else {
            mPlayer.resume();
            showPauseButton();
        }
    }

    private void onSkipNextClicked() {

        if (mPlaylistTracksAdapter.getCount() <= mItemPosition + 1) {
            mItemPosition = 0;
            playSong(mItemPosition);
            mListView.setSelection(mItemPosition);
        } else {
            playSong(mItemPosition + 1);
        }
        if (mPlayer == null) {
            //Toast.makeText(this, "Please select a song", Toast.LENGTH_SHORT).show();
        }
    }

    private void onPreviousClicked() {

        if (mItemPosition < 1) {
            mItemPosition = 0;
            playSong(mItemPosition);
        } else {
            playSong(mItemPosition - 1);
        }
        if (mPlayer == null) {
            //Toast.makeText(this, "Please select a song", Toast.LENGTH_SHORT).show();
        }
    }

    public void onRadioButtonClicked(int id) {

        switch (id) {
            case R.id.one_minute_thirty:
                if (mOneThirtyMin.isChecked()) {
                    mSongDurationView.setText(R.string.one_thirty_radio_button);
                    mPauseTimeAt = 90000;
                }
                break;
            case R.id.two_minutes:
                if (mTwoMin.isChecked()) {
                    mSongDurationView.setText(R.string.two_minute_radio_button);
                    mPauseTimeAt = 120000;
                }
                break;
        }
    }

    private void updateListView(List<Item> items) {

        mPlaylistTracksAdapter.clear();
        mPlaylistTracksAdapter.addAll(items);
        mPlaylistTracksAdapter.notifyDataSetChanged();
    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(final int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter

        mSpotifyService.getPlaylistTracks(mUserId, mPlaylistId).enqueue(new Callback<PlaylistTracksList>() {
            @Override
            public void onResponse(Call<PlaylistTracksList> call, Response<PlaylistTracksList> response) {
                if (response.isSuccess() && response.body() != null) {
                    response.body().setOffset(offset);
                    mPlaylistTracksAdapter.addAll(response.body().getItems());
                    mPlaylistTracksAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PlaylistTracksList> call, Throwable t) {

            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String trackId) {
        if (mListener != null) {
            mListener.onPlaylistTrackSelected(trackId);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPlaylistTracksInteractionListener) {
            mListener = (OnPlaylistTracksInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTracksInteractionListener");
        }
    }

    @Override

    public void onDetach() {
        super.onDetach();
        Spotify.destroyPlayer(this);
        mListener = null;
    }

    @Override
    public void onPause() {

        Spotify.destroyPlayer(this);
        mTimer.cancel();
        mTimer.purge();
        seekHandler.removeCallbacks(run);
        mSeekBar.setProgress(0);
        super.onPause();
    }

    public interface OnPlaylistTracksInteractionListener {
        void onPlaylistTrackSelected(String trackName);
    }

}
