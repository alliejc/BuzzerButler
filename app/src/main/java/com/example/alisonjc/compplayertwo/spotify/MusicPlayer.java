package com.example.alisonjc.compplayertwo.spotify;


import android.content.Context;
import android.util.Log;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.SpotifyPlayer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MusicPlayer {

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


}
