package com.triestpa.selfiefeed;

import com.google.gson.annotations.SerializedName;

public class Selfie {

    String filter;
    String id;

    @SerializedName("images")
    Images images;

    class Images {
        Image low_resolution;
        Image thumbnail;
        Image standard_resolution;
    }

    class Image {
        String url;
        String width;
        String height;
    }
/*
    String thumbnailPictureURL;
    String lowResPictureURL;
    String fullResPictureURL;

    String createdTime;
    String caption;
    String user;
    String likes;
    */


    public Selfie () {}
/*
    public String getThumbnailPictureURL() {
        return thumbnailPictureURL;
    }

    public void setThumbnailPictureURL(String thumbnailPictureURL) {
        this.thumbnailPictureURL = thumbnailPictureURL;
    }

    public String getLowResPictureURL() {
        return lowResPictureURL;
    }

    public void setLowResPictureURL(String lowResPictureURL) {
        this.lowResPictureURL = lowResPictureURL;
    }

    public String getFullResPictureURL() {
        return fullResPictureURL;
    }

    public void setFullResPictureURL(String fullResPictureURL) {
        this.fullResPictureURL = fullResPictureURL;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
    */


}
