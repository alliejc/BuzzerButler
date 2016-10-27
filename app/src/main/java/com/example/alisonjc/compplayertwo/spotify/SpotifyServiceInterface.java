package com.example.alisonjc.compplayertwo.spotify;

import com.example.alisonjc.compplayertwo.spotify.model.UserTracks.UserTracks;
import com.example.alisonjc.compplayertwo.spotify.model.playlist_tracklists.PlaylistTracksList;
import com.example.alisonjc.compplayertwo.spotify.model.playlists.SpotifyUser;
import com.example.alisonjc.compplayertwo.spotify.model.playlists.UserPlaylists;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpotifyServiceInterface {

    //the logged in user information
    @GET("v1/me")
    Call<SpotifyUser> getCurrentUser(@Header("Authorization") String bearerToken);

    //logged in users saved tracks
    @GET("v1/me/tracks")
    Call<UserTracks> getUserTracks(@Header("Authorization") String bearerToken, @Query("offset") int offset, @Query("limit") int limit);

    //logged in users playlists
    @GET("v1/me/playlists")
    Call<UserPlaylists> getCurrentUserPlaylists(@Header("Authorization") String bearerToken);

    //logged in users playlists and userid
    @GET("v1/users/{user_id}/playlists")
    Call<UserPlaylists> getUserPlayLists(@Header("Authorization") String bearerToken, @Path("user_id") String userId);

    //playlist tracks for playlist for specific user
    @GET("/v1/users/{user_id}/playlists/{playlist_id}/tracks")
    Call<PlaylistTracksList> getPlaylistTracks(@Header("Authorization") String bearerToken,
                                               @Path("user_id") String userId, @Path("playlist_id") String playlistId, @Query("offset") int offset, @Query("limit") int limit);

}
