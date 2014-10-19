package com.yandex.yac2014.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by 7times6 on 17.10.14.
 */
public class Image implements Serializable {

    @SerializedName("id")
    public int serverId;

    @SerializedName("url")
    public String url;

}
