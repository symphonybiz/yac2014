package com.yandex.yac2014.api;

import com.yandex.yac2014.api.response.PhotosResponse;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by 7times6 on 17.10.14.
 */
public interface Api500px {

    @GET("/photos?image_size=4")
    Observable<PhotosResponse> photos(
            @Query("consumer_key") String key,
            @Query("feature")      String feature
    );

}
