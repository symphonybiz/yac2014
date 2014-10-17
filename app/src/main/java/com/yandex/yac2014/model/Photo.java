package com.yandex.yac2014.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 7times6 on 17.10.14.
 */
public class Photo {
    public int id;

    public String name;

    public float rating;

    public int width;

    public int height;

    @SerializedName("image_url")
    public String imageUrl;

    public User user;
}
