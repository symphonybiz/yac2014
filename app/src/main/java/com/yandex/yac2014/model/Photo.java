package com.yandex.yac2014.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 7times6 on 17.10.14.
 */
public class Photo implements Serializable {

    @SerializedName("liked")
    public boolean liked;

    @SerializedName("_id")
    public Long _id;

    @SerializedName("id")
    public int serverId;

    @SerializedName("name")
    public String name;

    @SerializedName("rating")
    public float rating;

    @SerializedName("user")
    public User user;

    @SerializedName("images")
    public ArrayList<Image> images;

    public String getFirstImageUri() {
        return images.get(0).url;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + _id +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                '}';
    }
}
