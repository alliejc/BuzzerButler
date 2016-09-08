package com.example.alisonjc.compplayertwo;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
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
import com.example.alisonjc.compplayertwo.spotify.model.UserTracks.Item;
import com.example.alisonjc.compplayertwo.spotify.model.UserTracks.UserTracks;
import com.google.inject.Inject;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.PlayerStateCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import roboguice.fragment.RoboFragment;


public class TracksFragment extends RoboFragment {

    @Inject
    private SpotifyService mSpotifyService;

    @Inject
    private SpotifyPlayer mSpotifyPlayer;

    @BindView (R.id.tracksview)
     ListView mListView;

    @BindView (R.id.play)
     ImageButton mPlayButton;

    @BindView (R.id.pause)
     ImageButton mPauseButton;

    @BindView (R.id.seekerBarView)
     SeekBar mSeekBar;

    @BindView (R.id.musicCurrentLoc)
     TextView mSongLocationView;

    @BindView (R.id.musicDuration)
     TextView mSongDurationView;

    @BindView (R.id.radio_group)
     RadioGroup mRadioGroup;

    @BindView (R.id.one_minute_thirty)
     RadioButton mOneThirtyMin;

    @BindView (R.id.two_minutes)
     RadioButton mTwoMin;

    private int mSongLocation;
    private Timer mTimer;
    private Player mPlayer;
    private Handler seekHandler = new Handler();
    private static TracksAdapter mTracksAdapter;
    private View rootView;
    private String mPlaylistId;
    private int mItemPosition = 0;
    private int mPauseTimeAt = 90000;
    private boolean mBeepPlayed = false;
    private Item mTrackItem;
    private String mTrackName;

    private OnTracksInteractionListener mListener;

    public TracksFragment() {
        // Required empty public constructor
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


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSongLocationView.setText("0:00");
        mSongDurationView.setText(R.string.one_thirty_radio_button);

        mListView = (ListView) view.findViewById(R.id.tracksview);

        listViewSetup();

        mPlayer = mSpotifyPlayer.getPlayer(getContext());

        mListView.setOnScrollListener(new EndlessScrollListener() {

            @Override
            public boolean onLoadMore(final int page, final int totalItemsCount) {


                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);

                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });


        playerControlsSetup();
        startTimerTask();


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

    public void customLoadMoreDataFromApi(final int offset) {

        mSpotifyService.getUserTracks().enqueue(new Callback<UserTracks>() {
            @Override
            public void onResponse(Call<UserTracks> call, Response<UserTracks> response) {
                if (response.isSuccess() && response.body() != null) {
                    response.body().setOffset(offset);
                    mTracksAdapter.addAll(response.body().getItems());
                    mTracksAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<UserTracks> call, Throwable t) {

            }
        });
    }

    private void listViewSetup() {

        mTracksAdapter = new TracksAdapter(getActivity(), R.layout.item_track, new ArrayList<Item>());
        mListView.setAdapter(mTracksAdapter);
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        mSpotifyService.getUserTracks().enqueue(new Callback<UserTracks>() {
            @Override
            public void onResponse(Call<UserTracks> call, Response<UserTracks> response) {
                if (response.isSuccess() && response.body() != null) {
                    updateListView(response.body().getItems());

                } else if (response.code() == 401) {
                    //add logout to interface
                    //userLogout();
                }
            }

            @Override
            public void onFailure(Call<UserTracks> call, Throwable t) {

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
        mPlayer.play("spotify:track:" + mTracksAdapter.getItem(locationid).getTrack().getId());
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
        mTracksAdapter.notifyDataSetChanged();
    }

    @TargetApi(Build.VERSION_CODES.M)
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

        if (mTracksAdapter.getCount() <= mItemPosition + 1) {
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

        mTracksAdapter.clear();
        mTracksAdapter.addAll(items);
        mTracksAdapter.notifyDataSetChanged();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String trackId) {
        if (mListener != null) {
            mListener.onTrackSelected(trackId);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTracksInteractionListener) {
            mListener = (OnTracksInteractionListener) context;
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

        //Spotify.destroyPlayer(this);
        mTimer.cancel();
        mTimer.purge();
        seekHandler.removeCallbacks(run);
        mSeekBar.setProgress(0);
        super.onPause();
    }

    public interface OnTracksInteractionListener {
        // TODO: Update argument type and name
        void onTrackSelected(String trackId);
    }
}
