package com.yandex.yac2014.api.response;

import com.google.gson.annotations.SerializedName;
import com.yandex.yac2014.model.Photo;

import java.util.List;

/**
 * Created by 7times6 on 17.10.14.
 */
public class PhotosResponse {
    @SerializedName("current_page")
    public int currentPage;

    @SerializedName("total_pages")
    public int totalPages;

    @SerializedName("total_items")
    public int totalItems;

    public List<Photo> photos;

    @Override
    public String toString() {
        return "PhotosResponse{" +
                "currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                ", totalItems=" + totalItems +
                ", photos=" + photos +
                '}';
    }
}
