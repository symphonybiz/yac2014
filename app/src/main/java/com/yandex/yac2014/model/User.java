package com.yandex.yac2014.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by 7times6 on 17.10.14.
 */
public class User implements Serializable {

    public Long _id;

    public int id;

    public String username;

    public String fullname;

    @SerializedName("userpic_url")
    public String userpicUrl;
}
