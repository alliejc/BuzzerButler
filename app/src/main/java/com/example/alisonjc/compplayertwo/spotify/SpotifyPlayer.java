package com.example.alisonjc.compplayertwo.spotify;


import android.content.Context;
import android.util.Log;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.Spotify;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SpotifyPlayer {

    @Inject
    private SpotifyService mSpotifyService;

    private Player mPlayer;



    public Player getPlayer(Context context) {

        if (mPlayer != null) {

            return mPlayer;

        } else {

            final Config playerConfig = mSpotifyService.getPlayerConfig(context);
            playerConfig.useCache(false);

            mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {

                @Override
                public void onInitialized(Player player) {
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
