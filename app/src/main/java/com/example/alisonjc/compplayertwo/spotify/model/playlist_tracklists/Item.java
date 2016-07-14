
package com.example.alisonjc.compplayertwo.spotify.model.playlist_tracklists;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Item {

    @SerializedName("added_at")
    @Expose
    private String addedAt;
    @SerializedName("added_by")
    @Expose
    private AddedBy addedBy;
    @SerializedName("is_local")
    @Expose
    private Boolean isLocal;
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
     *     The addedBy
     */
    public AddedBy getAddedBy() {
        return addedBy;
    }

    /**
     * 
     * @param addedBy
     *     The added_by
     */
    public void setAddedBy(AddedBy addedBy) {
        this.addedBy = addedBy;
    }

    /**
     * 
     * @return
     *     The isLocal
     */
    public Boolean getIsLocal() {
        return isLocal;
    }

    /**
     * 
     * @param isLocal
     *     The is_local
     */
    public void setIsLocal(Boolean isLocal) {
        this.isLocal = isLocal;
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
