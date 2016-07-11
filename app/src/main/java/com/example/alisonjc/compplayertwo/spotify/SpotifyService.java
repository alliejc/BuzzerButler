package com.example.alisonjc.compplayertwo.spotify;


import com.example.alisonjc.compplayertwo.spotify.model.playlists.SpotifyUser;
import com.example.alisonjc.compplayertwo.spotify.model.playlists.UserPlaylists;
import com.example.alisonjc.compplayertwo.spotify.model.tracklists.PlaylistTracksList;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class SpotifyService {

    private String mToken = "";
    private String mUserId = "";



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

    public Call<PlaylistTracksList> getPlaylistTracks(String playlistId) {
        return mSpotifyService.getPlaylistTracks(mToken, mUserId, playlistId);
    }

    public Call<UserPlaylists> getUserPlayLists(){
        return mSpotifyService.getUserPlayLists("Bearer " + mToken, mUserId);
    }

    public Call<SpotifyUser> getCurrentUser(String token){

        mToken = token;
        return mSpotifyService.getCurrentUser("Bearer " + token);
    }

    public boolean isLoggedIn(){
        return (mUserId.equals("") || mToken.equals(""));
    }
}

