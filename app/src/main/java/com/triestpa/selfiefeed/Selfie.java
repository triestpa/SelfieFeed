package com.triestpa.selfiefeed;

/* Store the image URLs and details for each selfie */
public class Selfie {
    private String id;
    private Images images;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }


    // Three different sizes of images are returned, the standard is a good balance between size and quality.
    class Images {
        private Image low_resolution;
        private Image thumbnail;
        private Image standard_resolution;

        public Image getLow_resolution() {
            return low_resolution;
        }

        public void setLow_resolution(Image low_resolution) {
            this.low_resolution = low_resolution;
        }

        public Image getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(Image thumbnail) {
            this.thumbnail = thumbnail;
        }

        public Image getStandard_resolution() {
            return standard_resolution;
        }

        public void setStandard_resolution(Image standard_resolution) {
            this.standard_resolution = standard_resolution;
        }
    }

    // Store URL and dimens for each image size
    class Image {
        private String url;
        private String width;
        private String height;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }
    }


    public Selfie () {}

}
