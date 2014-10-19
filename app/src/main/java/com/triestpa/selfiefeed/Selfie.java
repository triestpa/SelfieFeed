package com.triestpa.selfiefeed;

/* Store the image URLs and details for each selfie */
public class Selfie {

    String filter;
    String id;
    Images images;

    // Three different sizes of images are returned, the standard is a good balance between size and quality.
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


    public Selfie () {}

}
