package com.alisonjc.compmusicplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alisonjc.compmusicplayer.spotify.MusicPlayer;
import com.alisonjc.compmusicplayer.spotify.SpotifyService;
import com.alisonjc.compmusicplayer.tracks.OnControllerTrackChangeListener;
import com.google.inject.Inject;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import roboguice.fragment.RoboFragment;

public class MediaController extends RoboFragment implements OnControllerTrackChangeListener {

    @Inject
    private SpotifyService mSpotifyService;

    @Inject
    private MusicPlayer mMusicPlayer;

    @BindView(R.id.song_title)
    TextView mSongView;

    @BindView(R.id.artist)
    TextView mArtistView;

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

    private int mSongLocation = 0;
    private Handler seekHandler = new Handler();
    private Handler timerHandler = new Handler();
    private SpotifyPlayer mPlayer;
    private int mPauseTimeAt = 90000;
    private boolean mBeepPlayed = false;
    private View mPlayerControls;
    private int seconds = 0;
    private int minutes = 0;
    private OnControllerTrackChangeListener mOnControllerTrackChangeListener;
    private static final String TAG = "MediaController";

    public MediaController() {
    }

    public static MediaController newInstance() {
        return new MediaController();
    }

    private final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {
        @Override
        public void onSuccess() {
        }

        @Override
        public void onError(Error error) {
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.media_controls, container, false);
        mPlayerControls = rootView.findViewById(R.id.music_player);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playerControlsSetup();
        setTimer();
        setSeekBar();

        if (mPlayer == null) {
            mPlayer = mMusicPlayer.getPlayer(getContext());
        }
    }

    public void playSong(String songName, String artistName, String uri) {
        Log.d(TAG, "playSong");

        mBeepPlayed = false;
        setTimer();
        showPauseButton();
        setSeekBar();
        mPlayer.playUri(mOperationCallback, uri, 0, 0);
        mSongView.setText(songName + " - ");
        mArtistView.setText(artistName);
    }

    private void setTimer() {

        if (mPlayer != null) {

            mSongLocation = (int) mPlayer.getPlaybackState().positionMs;

            if (mSongLocation >= mPauseTimeAt - 10000 && !mBeepPlayed) {
                Log.d(TAG, "setTimerPlayBeep");
                mBeepPlayed = true;
                playBeep();

            } else if (mSongLocation >= mPauseTimeAt && mBeepPlayed) {
                Log.d(TAG, "SetTimeSkipNext");
                onSkipNextClicked();
            }
            timerHandler.postDelayed(timerRun, 1000);
        }
    }

    private Runnable timerRun = new Runnable() {
        @Override
        public void run() {
            setTimer();
        }
    };

    private void setSeekBar() {

        if (mPlayer != null) {

            mSeekBar.setMax(mPauseTimeAt);
            mSeekBar.setProgress(mSongLocation);

            seconds = ((mSongLocation / 1000) % 60);
            minutes = ((mSongLocation / 1000) / 60);

            mSongLocationView.setText(String.format("%2d:%02d", minutes, seconds, 0));
        }

        seekHandler.postDelayed(seekrun, 1000);
    }

    Runnable seekrun = new Runnable() {
        @Override
        public void run() {
            setSeekBar();
        }
    };

    private void playerControlsSetup() {

        mSongLocationView.setText(R.string.start_time);
        mSongDurationView.setText(R.string.one_thirty_radio_button);

        mPlayerControls.findViewById(R.id.skip_previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPreviousClicked();
            }
        });

        mPlayerControls.findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayClicked();
            }
        });

        mPlayerControls.findViewById(R.id.pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPauseClicked();
            }
        });

        mPlayerControls.findViewById(R.id.skip_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSkipNextClicked();
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
                Log.d("Seekbar", "onProgressChanged" + progress);

                if (mPlayer != null && fromUser) {

                    seconds = ((progress / 1000) % 60);
                    minutes = ((progress / 1000) / 60);

                    mPlayer.seekToPosition(mOperationCallback, progress);
                    mSongLocationView.setText(String.format("%2d:%02d", minutes, seconds, 0));
                    Log.i("Seekbar", "setProgress" + progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("Seekbar", "START" + seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("Seekbar", "STOP" + seekBar.getProgress());
                onProgressChanged(seekBar, seekBar.getProgress(), true);
            }
        });
    }

    private void playBeep() {
        Log.d(TAG, "PlayBeep");

        final MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.beep);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                Log.d(TAG, "MediaPlayerSTART");
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                    Log.d(TAG, "MediaPlayerRELEASE");
            }
        });
    }

    private void onPauseClicked() {

        if(mPlayer != null){
            mPlayer.pause(mOperationCallback);
            showPlayButton();
        } else {
            Toast.makeText(getActivity(), "Please select a song", Toast.LENGTH_SHORT).show();
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

        if(mPlayer != null){
            mPlayer.resume(mOperationCallback);
            showPauseButton();
        }
    }

    private void onSkipNextClicked() {
        Log.d(TAG, "onSkipClicked");

        if (mPlayer != null) {
            Log.d(TAG, "onSkipNextClickedPLAYERNOTNULL");
            mPlayer.skipToNext(mOperationCallback);
            onControllerTrackChange(true);

        } else {
            Log.d(TAG, "onSkipNextClickedPLAYERNULL");
        }
    }

    private void onPreviousClicked() {

        if (mPlayer != null) {
            mPlayer.skipToPrevious(mOperationCallback);
            onControllerTrackChange(false);
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

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
        clearPlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPlayer != null) {
            setSeekBar();
        }
        Log.d(TAG, "onResume");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnControllerTrackChangeListener) {
            mOnControllerTrackChangeListener = (OnControllerTrackChangeListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnControllerInteractionListener");
        }
    }

    @Override
    public void onControllerTrackChange(boolean skipforward) {

        if (mOnControllerTrackChangeListener != null) {
            Log.d(TAG, "onControllerTrackChangeNOTNULL");
            mOnControllerTrackChangeListener.onControllerTrackChange(skipforward);

        }else {
            Log.d(TAG, "onControllerTrackChangeNULL");
        }

    }

    public void clearPlayer() {
        Log.d(TAG, "clearPlayer");

        setSeekBar();
        setTimer();
        showPlayButton();

        if (mPlayer != null) {
            mPlayer.pause(mOperationCallback);
            Spotify.destroyPlayer(mPlayer);
        }
    }
}

