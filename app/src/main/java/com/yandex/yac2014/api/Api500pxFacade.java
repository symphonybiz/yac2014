package com.yandex.yac2014.api;

import com.yandex.yac2014.api.response.PhotosResponse;

import retrofit.RestAdapter;
import rx.Notification;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by 7times6 on 17.10.14.
 */
public class Api500pxFacade {

    Api500px api;

    public Api500pxFacade() {
        api = new RestAdapter.Builder().
                setEndpoint(Const500px.ENDPOINT)
                .build()
                .create(Api500px.class);
    }

    public Observable<PhotosResponse> popularPhotos(int page) {
        return api.photos(Const500px.CONSUMER_KEY, Const500px.FEATURE_POPULAR, page, Const500px.MAX_IMAGE_SIZE);
    }
}
