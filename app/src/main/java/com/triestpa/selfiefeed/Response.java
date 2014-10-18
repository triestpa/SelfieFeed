package com.triestpa.selfiefeed;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {
    Pagination pagination;
    Meta meta;

    @SerializedName("data")
    List<Selfie> selfies;


    class Pagination {
        @SerializedName("next_url")
        String nextURL;
    }

    class Meta {
        String code;
    }

}
