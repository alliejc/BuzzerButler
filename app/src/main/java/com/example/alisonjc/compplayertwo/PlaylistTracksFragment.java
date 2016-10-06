package com.example.alisonjc.compplayertwo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
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

import com.example.alisonjc.compplayertwo.spotify.SpotifyPlayer;
import com.example.alisonjc.compplayertwo.spotify.SpotifyService;
import com.example.alisonjc.compplayertwo.spotify.model.playlist_tracklists.Item;
import com.example.alisonjc.compplayertwo.spotify.model.playlist_tracklists.PlaylistTracksList;
import com.example.alisonjc.compplayertwo.tracks.PlaylistTracksRecyclerAdapter;
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

public class PlaylistTracksFragment extends RoboFragment {

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
    private Handler seekHandler = new Handler();
    private Player mPlayer;
    private PlaylistTracksRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Item> mPlaylistTracksList;
    private View rootView;
    private OnPlaylistTracksInteractionListener mListener;
    private String mPlaylistId;
    private String mUserId;
    private int mItemPosition = 0;
    private int mPauseTimeAt = 90000;
    private boolean mBeepPlayed = false;
    private String trackName = "";
    private Drawable dividerDrawable;

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
        mPlayer = mSpotifyPlayer.getPlayer(getContext());

        startTimerTask();
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

            mSongLocationView.setText("0:00");
            mSongDurationView.setText(R.string.one_thirty_radio_button);

        dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.recycler_view_divider);

        playerControlsSetup();
        setSeekBar();

        mPlaylistTracksList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = (new PlaylistTracksRecyclerAdapter(getContext(), mPlaylistTracksList, new PlaylistTracksRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item, int position) {
                mItemPosition = position;
                trackName = item.getTrack().getName();
                mListener.onPlaylistTrackSelected(trackName);
                playSong(mItemPosition);
                showPauseButton();
            }
        }));

        mRecyclerView.setAdapter(mAdapter);

        mSpotifyService.getPlaylistTracks(mUserId, mPlaylistId).enqueue(new Callback<PlaylistTracksList>() {
            @Override
            public void onResponse(Call<PlaylistTracksList> call, Response<PlaylistTracksList> response) {
                if (response.isSuccess() && response.body() != null) {
                    mAdapter.updateAdapter(response.body().getItems());
                } else if (response.code() == 401) {
                    //add logout to interface
                    //userLogout();
                }
            }

            @Override
            public void onFailure(Call<PlaylistTracksList> call, Throwable t) {

            }
        });

        mRecyclerView.addOnScrollListener(new EndlessScrollListener(){
                @Override
                public boolean onLoadMore(int page, int totalItemsCount) {
                    customLoadMoreDataFromApi(page);
                    return true;
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

    private void playSong(int locationid) {

        mBeepPlayed = false;
        showPauseButton();
        setCurrentPlayingSong(locationid);
        mPlayer.play("spotify:track:" + mPlaylistTracksList.get(locationid).getTrack().getId());
        onButtonPressed(mPlaylistTracksList.get(locationid).getTrack().getName());
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
        mAdapter.notifyItemChanged(itemPosition);
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

        if (mAdapter.getItemCount() <= mItemPosition +1) {
            mItemPosition = 0;
            playSong(mItemPosition);
            mRecyclerView.smoothScrollToPosition(mItemPosition);
        } else {
            playSong(mItemPosition + 1);
            mRecyclerView.smoothScrollToPosition(mItemPosition);
        }
        if (mPlayer == null) {
            //Toast.makeText(this, "Please select a song", Toast.LENGTH_SHORT).show();
        }
    }

    private void onPreviousClicked() {

        if (mItemPosition < 1) {
            mItemPosition = 0;
            playSong(mItemPosition);
            mRecyclerView.smoothScrollToPosition(mItemPosition);
        } else {
            playSong(mItemPosition - 1);
            mRecyclerView.smoothScrollToPosition(mItemPosition);
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
                    mAdapter.updateAdapter(response.body().getItems());
                }
            }

            @Override
            public void onFailure(Call<PlaylistTracksList> call, Throwable t) {
            }
        });

    }

    public void onButtonPressed(String trackName) {
        if (mListener != null) {
            mListener.onPlaylistTrackSelected(trackName);
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
        super.onPause();
        Spotify.destroyPlayer(this);
        mTimer.cancel();
        mTimer.purge();
        seekHandler.removeCallbacks(run);
        mSeekBar.setProgress(0);
    }

    public interface OnPlaylistTracksInteractionListener {
        void onPlaylistTrackSelected(String trackName);
    }

}
