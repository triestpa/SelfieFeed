package com.triestpa.selfiefeed;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/* Store the response results */
public class Response {
    Pagination pagination;
    Meta meta;

    @SerializedName("data")
    List<Selfie> selfies;


    //The url for the next page of results
    class Pagination {
        @SerializedName("next_url")
        String nextURL;
    }

    // The api response code, should be 200 if everything went well
    class Meta {
        String code;
    }

}
