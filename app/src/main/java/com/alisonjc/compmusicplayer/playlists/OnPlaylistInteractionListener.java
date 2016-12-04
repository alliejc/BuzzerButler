package com.alisonjc.compmusicplayer.playlists;


public interface OnPlaylistInteractionListener {
    void onPlaylistSelected(String userId, String playlistId, String playlistTitle);
}
