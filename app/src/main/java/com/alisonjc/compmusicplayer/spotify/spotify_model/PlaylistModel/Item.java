
package com.alisonjc.compmusicplayer.spotify.spotify_model.PlaylistModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Item {

    @SerializedName("collaborative")
    @Expose
    private Boolean collaborative;
    @SerializedName("external_urls")
    @Expose
    private ExternalUrls externalUrls;
    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("images")
    @Expose
    private List<Object> images = new ArrayList<Object>();
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("owner")
    @Expose
    private Owner owner;
    @SerializedName("public")
    @Expose
    private Boolean _public;
    @SerializedName("snapshot_id")
    @Expose
    private String snapshotId;
    @SerializedName("tracks")
    @Expose
    private com.alisonjc.compmusicplayer.spotify.spotify_model.PlaylistModel.Tracks tracks;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("uri")
    @Expose
    private String uri;

    /**
     * 
     * @return
     *     The collaborative
     */
    public Boolean getCollaborative() {
        return collaborative;
    }

    /**
     * 
     * @param collaborative
     *     The collaborative
     */
    public void setCollaborative(Boolean collaborative) {
        this.collaborative = collaborative;
    }

    /**
     * 
     * @return
     *     The externalUrls
     */
    public ExternalUrls getExternalUrls() {
        return externalUrls;
    }

    /**
     * 
     * @param externalUrls
     *     The external_urls
     */
    public void setExternalUrls(ExternalUrls externalUrls) {
        this.externalUrls = externalUrls;
    }

    /**
     * 
     * @return
     *     The href
     */
    public String getHref() {
        return href;
    }

    /**
     * 
     * @param href
     *     The href
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The images
     */
    public List<Object> getImages() {
        return images;
    }

    /**
     * 
     * @param images
     *     The images
     */
    public void setImages(List<Object> images) {
        this.images = images;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The owner
     */
    public Owner getOwner() {
        return owner;
    }

    /**
     * 
     * @param owner
     *     The owner
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    /**
     * 
     * @return
     *     The _public
     */
    public Boolean getPublic() {
        return _public;
    }

    /**
     * 
     * @param _public
     *     The public
     */
    public void setPublic(Boolean _public) {
        this._public = _public;
    }

    /**
     * 
     * @return
     *     The snapshotId
     */
    public String getSnapshotId() {
        return snapshotId;
    }

    /**
     * 
     * @param snapshotId
     *     The snapshot_id
     */
    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    /**
     * 
     * @return
     *     The tracks
     */
    public Tracks getTracks() {
        return tracks;
    }

    /**
     * 
     * @param tracks
     *     The tracks
     */
    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * 
     * @param uri
     *     The uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

}
