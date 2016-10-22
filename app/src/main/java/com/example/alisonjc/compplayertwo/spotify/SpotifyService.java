package com.example.alisonjc.compplayertwo.spotify;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.alisonjc.compplayertwo.BuildConfig;
import com.example.alisonjc.compplayertwo.spotify.model.UserTracks.UserTracks;
import com.example.alisonjc.compplayertwo.spotify.model.playlist_tracklists.PlaylistTracksList;
import com.example.alisonjc.compplayertwo.spotify.model.playlists.SpotifyUser;
import com.example.alisonjc.compplayertwo.spotify.model.playlists.UserPlaylists;
import com.spotify.sdk.android.player.Config;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class SpotifyService {

    private static final String CLIENT_ID = BuildConfig.CLIENT_ID;

    private String mToken = "";
    private String mUserId = "";
    public SharedPreferences myPrefs;
    private int mTotalTracks = 0;

    public void setUserId(String userId, Context context) {

        mUserId = userId;
        myPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        myPrefs.edit().putString("userId", mUserId).apply();

//        context.getSharedPreferences("userId", Context.MODE_PRIVATE).edit().putString("userId", mUserId).apply();
//        settings = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString("userId", mUserId);
//        editor.apply();

    }

    public void setToken(String token, Context context) {

        mToken = token;
        myPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        myPrefs.edit().putString("token", mToken).apply();

//        context.getSharedPreferences("token", Context.MODE_PRIVATE).edit().putString("token", mToken).apply();
//        settings = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString("token", mToken);
//        editor.apply();

    }

    private final SpotifyServiceInterface mSpotifyService;

    public SpotifyService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mSpotifyService = retrofit.create(SpotifyServiceInterface.class);
    }

    public Call<PlaylistTracksList> getPlaylistTracks(String userId, String playlistId, int offset, int limit) {
        return mSpotifyService.getPlaylistTracks("Bearer " + mToken, userId, playlistId, offset, limit);
    }

    public Call<UserPlaylists> getUserPlayLists() {
        return mSpotifyService.getUserPlayLists("Bearer " + mToken, mUserId);
    }

    public Call<SpotifyUser> getCurrentUser(String token) {
        mToken = token;
        return mSpotifyService.getCurrentUser("Bearer " + mToken);
    }

    public Call<UserTracks> getUserTracks(int offset, int limit) {
        return mSpotifyService.getUserTracks("Bearer " + mToken, offset, limit);
    }

    public boolean isLoggedIn() {
        return (mUserId.equals("") || mToken.equals(""));
    }


    public Config getPlayerConfig(Context context) {
        return new Config(context, mToken, CLIENT_ID);
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

