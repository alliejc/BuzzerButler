package com.example.alisonjc.compplayertwo.spotify;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
    public SharedPreferences settings;


    public void setUserId(String userId) {
        mUserId = userId;
    }

    private final SpotifyServiceInterface mSpotifyService;

    public SpotifyService() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mSpotifyService = retrofit.create(SpotifyServiceInterface.class);
    }

    public Call<PlaylistTracksList> getPlaylistTracks(String userId, String playlistId) {
        return mSpotifyService.getPlaylistTracks("Bearer " + mToken, userId, playlistId);
    }

    public Call<UserPlaylists> getUserPlayLists(){
        return mSpotifyService.getUserPlayLists("Bearer " + mToken, mUserId);
    }

    public Call<SpotifyUser> getCurrentUser(String token){

        mToken = token;
        return mSpotifyService.getCurrentUser("Bearer " + mToken);
    }

    public Call<UserTracks> getUserTracks(){
        return mSpotifyService.getUserTracks("Bearer " + mToken);
    }

    public boolean isLoggedIn(){
        return (mUserId.equals("") || mToken.equals(""));
    }


    public Config getPlayerConfig(Context context){
        return new Config(context, mToken, CLIENT_ID);
    }

    public void userLogout(){
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("userId");
        editor.remove("token");
        editor.clear();
        editor.apply();
    }

    public void setPref(Context context){

        settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userId", mUserId);
        editor.putString("token", mToken);
        editor.apply();;
    }
}

