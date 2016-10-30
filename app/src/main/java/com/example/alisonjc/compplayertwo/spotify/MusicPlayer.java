package com.example.alisonjc.compplayertwo.spotify;


import android.content.Context;
import android.util.Log;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.SpotifyPlayer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MusicPlayer implements Player.NotificationCallback {

    @Inject
    private SpotifyService mSpotifyService;

    private SpotifyPlayer mPlayer;

    public SpotifyPlayer getPlayer(Context context) {

        if (mPlayer != null) {
            return mPlayer;

        } else {

            final Config playerConfig = mSpotifyService.getPlayerConfig(context);
            playerConfig.useCache(false);

            mPlayer = SpotifyPlayer.create(playerConfig, new SpotifyPlayer.InitializationObserver() {
                @Override
                public void onInitialized(SpotifyPlayer spotifyPlayer) {
                    spotifyPlayer.addNotificationCallback(MusicPlayer.this);

                }

                @Override
                public void onError(Throwable throwable) {
                    Log.e("PlaylistActivity", "Could not initialize player: " + throwable.getMessage());

                }
            });

            mPlayer.isInitialized();
            return mPlayer;
        }
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
    }

    @Override
    public void onPlaybackError(Error error) {
    }
}

