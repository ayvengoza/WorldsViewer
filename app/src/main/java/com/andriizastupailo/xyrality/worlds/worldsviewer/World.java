package com.andriizastupailo.xyrality.worlds.worldsviewer;

/**
 * Data class for world detail
 */

public class World {
    private String mName;
    private String mMapUrl;
    private String mDescription;
    private String mCountry;
    private String mLanguage;
    private String mUrl;
    private int mId;
    private int mWorldId;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getMapUrl() {
        return mMapUrl;
    }

    public void setMapUrl(String mapUrl) {
        mMapUrl = mapUrl;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getWorldId() {
        return mWorldId;
    }

    public void setWorldId(int worldId) {
        mWorldId = worldId;
    }
}
