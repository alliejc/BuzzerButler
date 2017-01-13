package com.alisonjc.compmusicplayer.spotify;


import android.content.Context;
import android.content.SharedPreferences;

import com.alisonjc.compmusicplayer.BuildConfig;
import com.alisonjc.compmusicplayer.spotify.model.UserTracks.UserTracks;
import com.alisonjc.compmusicplayer.spotify.model.playlist_tracklists.PlaylistTracksList;
import com.alisonjc.compmusicplayer.spotify.model.playlists.SpotifyUser;
import com.alisonjc.compmusicplayer.spotify.model.playlists.UserPlaylists;
import com.spotify.sdk.android.player.Config;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class SpotifyService {

    private static final String CLIENT_ID = BuildConfig.CLIENT_ID;

    private static SpotifyService mSpotifyService;

    public static SpotifyService getSpotifyService() {
        if (mSpotifyService != null){
            return mSpotifyService;
        } else {
            mSpotifyService = new SpotifyService();
            return mSpotifyService;
        }
    }

    private String mToken = "";
    private String mUserId = "";
    public SharedPreferences myPrefs;
    private final SpotifyServiceInterface mSpotifyServiceInterface;

    @Inject
    public SpotifyService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mSpotifyServiceInterface = retrofit.create(SpotifyServiceInterface.class);
    }

    public void setUserId(String userId, Context context) {

        mUserId = userId;
        myPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        myPrefs.edit().putString("userId", mUserId).apply();
    }

    public void setToken(String token, Context context) {

        mToken = token;
        myPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        myPrefs.edit().putString("token", mToken).apply();
    }

    public Call<PlaylistTracksList> getPlaylistTracks(String userId, String playlistId, int offset, int limit) {
        return mSpotifyServiceInterface.getPlaylistTracks("Bearer " + mToken, userId, playlistId, offset, limit);
    }

    public Call<UserPlaylists> getUserPlayLists() {
        return mSpotifyServiceInterface.getUserPlayLists("Bearer " + mToken, mUserId);
    }

    public Call<SpotifyUser> getCurrentUser(String token) {
        mToken = token;
        return mSpotifyServiceInterface.getCurrentUser("Bearer " + mToken);
    }

    public Call<UserTracks> getUserTracks(int offset, int limit) {
        return mSpotifyServiceInterface.getUserTracks("Bearer " + mToken, offset, limit);
    }

    public Config getPlayerConfig(Context context) {
        return new Config(context, mToken, CLIENT_ID);
    }

    public boolean isLoggedIn() {
        return (mUserId.equals("") || mToken.equals(""));
    }

    public void userLogout(Context context) {
        this.setToken("", context);
        this.setUserId("", context);
        context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.remove("userId").apply();
        editor.remove("token").apply();
    }
}

