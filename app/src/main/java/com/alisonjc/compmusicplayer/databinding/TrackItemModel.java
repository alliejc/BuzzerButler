package com.alisonjc.compmusicplayer.databinding;

import com.alisonjc.compmusicplayer.spotify.spotify_model.PlaylistTracksModel.Item;
import com.alisonjc.compmusicplayer.tracks.TrackItemInterface;


public class TrackItemModel implements TrackItemInterface {

    private String mArtist;
    private String mSongName;
    private String mUri;

    public TrackItemModel(Item playlistTrackItem) {
        this.mArtist = playlistTrackItem.getTrack().getArtists().get(0).getName();
        this.mSongName = playlistTrackItem.getTrack().getName();
        this.mUri = playlistTrackItem.getTrack().getUri();
    }

    public TrackItemModel(com.alisonjc.compmusicplayer.spotify.spotify_model.UserTracksModel.Item userTrackItem) {
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
