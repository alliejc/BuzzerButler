package com.example.alisonjc.compplayertwo.tracks;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.alisonjc.compplayertwo.DividerItemDecoration;
import com.example.alisonjc.compplayertwo.EndlessScrollListener2;
import com.example.alisonjc.compplayertwo.R;
import com.example.alisonjc.compplayertwo.spotify.SpotifyPlayer;
import com.example.alisonjc.compplayertwo.spotify.SpotifyService;
import com.example.alisonjc.compplayertwo.spotify.model.UserTracks.Item;
import com.example.alisonjc.compplayertwo.spotify.model.UserTracks.UserTracks;
import com.google.inject.Inject;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.PlayerStateCallback;
import com.spotify.sdk.android.player.Spotify;

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


    @BindView(R.id.play)
    ImageButton mPlayButton;

    @BindView(R.id.pause)
    ImageButton mPauseButton;

    @BindView(R.id.seekerBarView)
    SeekBar mSeekBar;

    @BindView(R.id.musicCurrentLoc)
    TextView mSongLocationView;

    @BindView(R.id.musicDuration)
    TextView mSongDurationView;

    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;

    @BindView(R.id.one_minute_thirty)
    RadioButton mOneThirtyMin;

    @BindView(R.id.two_minutes)
    RadioButton mTwoMin;

    private int mSongLocation;
    private Timer mTimer;
    private Player mPlayer;
    private OnTracksInteractionListener mListener;
    private Handler seekHandler = new Handler();
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private List<Item> mTracksList;
    private TracksRecyclerAdapter mAdapter;
    private View rootView;
    private int mItemPosition = 0;
    private int mPauseTimeAt = 90000;
    private boolean mBeepPlayed = false;
    private int mTotalTracks = 0;
    private int mOffset;

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
        mPlayer = mSpotifyPlayer.getPlayer(getContext());

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSongLocationView.setText("0:00");
        mSongDurationView.setText(R.string.one_thirty_radio_button);
        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.recycler_view_divider);

        playerControlsSetup();
        startTimerTask();

        mTracksList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = new TracksRecyclerAdapter(getContext(), mTracksList, new TracksRecyclerAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Item item, int position) {
                mItemPosition = position;
                mListener.onTrackSelected(item.getTrack().getName());
                playSong(position);
                showPauseButton();
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        mSpotifyService.getUserTracks(mOffset).enqueue(new Callback<UserTracks>() {
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

        mRecyclerView.addOnScrollListener(new EndlessScrollListener2(mLayoutManager, mTotalTracks) {
            @Override
            public void onLoadMore(int offset) {
                mOffset = offset + 20;
                loadMoreDataFromApi(mOffset);
            }

        });
    }

    public void loadMoreDataFromApi(final int offset) {

        mSpotifyService.getUserTracks(offset).enqueue(new Callback<UserTracks>() {
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

    private void playSong(int locationid) {

        mBeepPlayed = false;
        showPauseButton();
        setCurrentPlayingSong(locationid);
        smoothScroll(mItemPosition);
        mPlayer.play("spotify:track:" + mTracksList.get(locationid).getTrack().getId());
        onButtonPressed(mTracksList.get(locationid).getTrack().getName());
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
        setSeekBar();
        mAdapter.recyclerViewSelector(itemPosition);
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

        if (mAdapter.getItemCount() <= mItemPosition + 1) {
            mItemPosition = 0;
            playSong(mItemPosition);
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
        Spotify.destroyPlayer(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Spotify.destroyPlayer(this);
        mTimer.cancel();
        mTimer.purge();
        seekHandler.removeCallbacks(run);
        mSeekBar.setProgress(0);
    }

    public interface OnTracksInteractionListener {
        void onTrackSelected(String trackId);
    }
}
