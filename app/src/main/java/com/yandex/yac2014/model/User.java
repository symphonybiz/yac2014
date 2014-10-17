package com.yandex.yac2014.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 7times6 on 17.10.14.
 */
public class User {

    @SerializedName("id")
    public int _id;

    public String username;

    public String fullname;

    @SerializedName("userpic_url")
    public String userpicUrl;
}
