package com.yandex.yac2014.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 7times6 on 17.10.14.
 */
public class Photo {

    public boolean liked;

    @SerializedName("id")
    public int _id;

    @SerializedName("user_id")
    public int userId;

    public String name;

    public float rating;

    public int width;

    public int height;

    @SerializedName("image_url")
    public String imageUrl;

    public User user;

    public List<Image> images;

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + _id +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", rating=" + rating +
                '}';
    }
}
