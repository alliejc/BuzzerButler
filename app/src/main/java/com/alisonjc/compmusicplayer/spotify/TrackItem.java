package com.alisonjc.compmusicplayer.spotify;

import com.alisonjc.compmusicplayer.TrackItemInterface;
import com.alisonjc.compmusicplayer.spotify.model.playlist_tracklists.Item;


public class TrackItem implements TrackItemInterface {

    private String mArtist;
    private String mSongName;
    private String mUri;

    public TrackItem(Item playlistTrackItem) {
        this.mArtist = playlistTrackItem.getTrack().getArtists().get(0).getName();
        this.mSongName = playlistTrackItem.getTrack().getName();
        this.mUri = playlistTrackItem.getTrack().getUri();
    }

    public TrackItem(com.alisonjc.compmusicplayer.spotify.model.UserTracks.Item userTrackItem) {
        this.mArtist = userTrackItem.getTrack().getArtists().get(0).getName();
        this.mSongName = userTrackItem.getTrack().getName();
        this.mUri = userTrackItem.getTrack().getUri();
    }

    @Override
    public String getArtist() {
        return mArtist;
    }

    @Override
    public String getSongName() {
        return mSongName;
    }

    @Override
    public String getUri(){
        return mUri;
    }
}
