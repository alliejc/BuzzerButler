
package com.example.alisonjc.compplayertwo.spotify.model.UserTracks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Item {

    @SerializedName("added_at")
    @Expose
    private String addedAt;
    @SerializedName("track")
    @Expose
    private Track track;

    /**
     * 
     * @return
     *     The addedAt
     */
    public String getAddedAt() {
        return addedAt;
    }

    /**
     * 
     * @param addedAt
     *     The added_at
     */
    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }

    /**
     * 
     * @return
     *     The track
     */
    public Track getTrack() {
        return track;
    }

    /**
     * 
     * @param track
     *     The track
     */
    public void setTrack(Track track) {
        this.track = track;
    }

}
