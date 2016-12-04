
package com.alisonjc.compmusicplayer.spotify.model.playlists;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class SpotifyUser {

    @SerializedName("birthdate")
    @Expose
    private String birthdate;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("external_urls")
    @Expose
    private ExternalUrls externalUrls;
    @SerializedName("followers")
    @Expose
    private Followers followers;
    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("images")
    @Expose
    private List<Image> images = new ArrayList<Image>();
    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("uri")
    @Expose
    private String uri;

    /**
     *
     * @return
     *     The birthdate
     */
    public String getBirthdate() {
        return birthdate;
    }

    /**
     *
     * @param birthdate
     *     The birthdate
     */
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    /**
     *
     * @return
     *     The country
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     *     The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return
     *     The displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     *
     * @param displayName
     *     The display_name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     *
     * @return
     *     The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     *     The email
     */
    public void setEmail(String email) {
        this.email = email;
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
     *     The followers
     */
    public Followers getFollowers() {
        return followers;
    }

    /**
     *
     * @param followers
     *     The followers
     */
    public void setFollowers(Followers followers) {
        this.followers = followers;
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
    public List<Image> getImages() {
        return images;
    }

    /**
     *
     * @param images
     *     The images
     */
    public void setImages(List<Image> images) {
        this.images = images;
    }

    /**
     *
     * @return
     *     The product
     */
    public String getProduct() {
        return product;
    }

    /**
     *
     * @param product
     *     The product
     */
    public void setProduct(String product) {
        this.product = product;
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
