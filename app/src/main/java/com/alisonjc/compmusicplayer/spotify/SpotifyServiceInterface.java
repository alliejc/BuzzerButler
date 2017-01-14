package com.alisonjc.compmusicplayer.spotify;


import com.alisonjc.compmusicplayer.spotify.model.UserTracks.UserTracks;
import com.alisonjc.compmusicplayer.spotify.model.playlist_tracklists.PlaylistTracksList;
import com.alisonjc.compmusicplayer.spotify.model.playlists.SpotifyUser;
import com.alisonjc.compmusicplayer.spotify.model.playlists.UserPlaylists;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface SpotifyServiceInterface {

    //the logged in user information
    @GET("v1/me")
    Observable<SpotifyUser> getCurrentUser(@Header("Authorization") String bearerToken);

    //logged in users saved tracks
    @GET("v1/me/tracks")
    Observable<UserTracks> getUserTracks(@Header("Authorization") String bearerToken, @Query("offset") int offset, @Query("limit") int limit);

    //logged in users playlists
    @GET("v1/me/playlists")
    Observable<UserPlaylists> getCurrentUserPlaylists(@Header("Authorization") String bearerToken);

    //logged in users playlists and userid
    @GET("v1/users/{user_id}/playlists")
    Observable<UserPlaylists> getUserPlayLists(@Header("Authorization") String bearerToken, @Path("user_id") String userId);

    //playlist tracks for playlist for specific user
    @GET("/v1/users/{user_id}/playlists/{playlist_id}/tracks")
    Observable<PlaylistTracksList> getPlaylistTracks(@Header("Authorization") String bearerToken,
                                               @Path("user_id") String userId, @Path("playlist_id") String playlistId, @Query("offset") int offset, @Query("limit") int limit);

}
