package com.triestpa.selfiefeed;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/* Store the response results */
public class Response {
    private Pagination pagination;
    private Meta meta;

    @SerializedName("data")
    private List<Selfie> selfies;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Selfie> getSelfies() {
        return selfies;
    }

    public void setSelfies(List<Selfie> selfies) {
        this.selfies = selfies;
    }

    //The url for the next page of results
    class Pagination {
        @SerializedName("next_url")
        private String nextURL;

        public String getNextURL() {
            return nextURL;
        }

        public void setNextURL(String nextURL) {
            this.nextURL = nextURL;
        }
    }

    // The api response code, should be 200 if everything went well
    class Meta {
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

}
