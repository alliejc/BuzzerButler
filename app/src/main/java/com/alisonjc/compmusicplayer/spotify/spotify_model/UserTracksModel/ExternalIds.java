
package com.alisonjc.compmusicplayer.spotify.spotify_model.UserTracksModel;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ExternalIds {

    @SerializedName("isrc")
    @Expose
    private String isrc;

    /**
     * 
     * @return
     *     The isrc
     */
    public String getIsrc() {
        return isrc;
    }

    /**
     * 
     * @param isrc
     *     The isrc
     */
    public void setIsrc(String isrc) {
        this.isrc = isrc;
    }

}
