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

}
