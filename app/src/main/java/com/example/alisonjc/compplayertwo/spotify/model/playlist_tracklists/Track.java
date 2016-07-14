
package com.example.alisonjc.compplayertwo.spotify.model.playlist_tracklists;

import com.example.alisonjc.compplayertwo.spotify.model.playlists.ExternalIds;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Track {

    @SerializedName("album")
    @Expose
    private com.example.alisonjc.compplayertwo.spotify.model.playlist_tracklists.Album album;
    @SerializedName("artists")
    @Expose
    private List<Artist> artists = new ArrayList<Artist>();
    @SerializedName("available_markets")
    @Expose
    private List<String> availableMarkets = new ArrayList<String>();
    @SerializedName("disc_number")
    @Expose
    private Integer discNumber;
    @SerializedName("duration_ms")
    @Expose
    private Integer durationMs;
    @SerializedName("explicit")
    @Expose
    private Boolean explicit;
    @SerializedName("external_ids")
    @Expose
    private ExternalIds externalIds;
    @SerializedName("external_urls")
    @Expose
    private ExternalUrls___ externalUrls;
    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("popularity")
    @Expose
    private Integer popularity;
    @SerializedName("preview_url")
    @Expose
    private String previewUrl;
    @SerializedName("track_number")
    @Expose
    private Integer trackNumber;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("uri")
    @Expose
    private String uri;

    /**
     * 
     * @return
     *     The album
     */
    public Album getAlbum() {
        return album;
    }

    /**
     * 
     * @param album
     *     The album
     */
    public void setAlbum(Album album) {
        this.album = album;
    }

    /**
     * 
     * @return
     *     The artists
     */
    public List<Artist> getArtists() {
        return artists;
    }

    /**
     * 
     * @param artists
     *     The artists
     */
    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    /**
     * 
     * @return
     *     The availableMarkets
     */
    public List<String> getAvailableMarkets() {
        return availableMarkets;
    }

    /**
     * 
     * @param availableMarkets
     *     The available_markets
     */
    public void setAvailableMarkets(List<String> availableMarkets) {
        this.availableMarkets = availableMarkets;
    }

    /**
     * 
     * @return
     *     The discNumber
     */
    public Integer getDiscNumber() {
        return discNumber;
    }

    /**
     * 
     * @param discNumber
     *     The disc_number
     */
    public void setDiscNumber(Integer discNumber) {
        this.discNumber = discNumber;
    }

    /**
     * 
     * @return
     *     The durationMs
     */
    public Integer getDurationMs() {
        return durationMs;
    }

    /**
     * 
     * @param durationMs
     *     The duration_ms
     */
    public void setDurationMs(Integer durationMs) {
        this.durationMs = durationMs;
    }

    /**
     * 
     * @return
     *     The explicit
     */
    public Boolean getExplicit() {
        return explicit;
    }

    /**
     * 
     * @param explicit
     *     The explicit
     */
    public void setExplicit(Boolean explicit) {
        this.explicit = explicit;
    }

    /**
     * 
     * @return
     *     The externalIds
     */
    public ExternalIds getExternalIds() {
        return externalIds;
    }

    /**
     * 
     * @param externalIds
     *     The external_ids
     */
    public void setExternalIds(ExternalIds externalIds) {
        this.externalIds = externalIds;
    }

    /**
     * 
     * @return
     *     The externalUrls
     */
    public ExternalUrls___ getExternalUrls() {
        return externalUrls;
    }

    /**
     * 
     * @param externalUrls
     *     The external_urls
     */
    public void setExternalUrls(ExternalUrls___ externalUrls) {
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
     *     The popularity
     */
    public Integer getPopularity() {
        return popularity;
    }

    /**
     * 
     * @param popularity
     *     The popularity
     */
    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    /**
     * 
     * @return
     *     The previewUrl
     */
    public String getPreviewUrl() {
        return previewUrl;
    }

    /**
     * 
     * @param previewUrl
     *     The preview_url
     */
    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    /**
     * 
     * @return
     *     The trackNumber
     */
    public Integer getTrackNumber() {
        return trackNumber;
    }

    /**
     * 
     * @param trackNumber
     *     The track_number
     */
    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
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
