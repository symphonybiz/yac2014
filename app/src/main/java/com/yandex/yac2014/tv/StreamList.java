package com.yandex.yac2014.tv;

import com.yandex.yac2014.api.Api500pxFacade;
import com.yandex.yac2014.api.response.PhotosResponse;

import rx.Observable;

public final class StreamList {

    public interface StreamItem {
        Observable<PhotosResponse> getSubscription();
        String getName();
    }

    public static final StreamItem STREAM_FEATURE[] = {
            new StreamItem() {

                @Override
                public Observable<PhotosResponse> getSubscription() {
                    return new Api500pxFacade().popularPhotos(0);
                }

                @Override
                public String getName() {
                    return "Popular";
                }
            },
            new StreamItem() {

                @Override
                public Observable<PhotosResponse> getSubscription() {
                    return new Api500pxFacade().highestRatedPhotos(0);
                }

                @Override
                public String getName() {
                    return "Highest Rated";
                }
            },
            new StreamItem() {

                @Override
                public Observable<PhotosResponse> getSubscription() {
                    return new Api500pxFacade().freshToday(0);
                }

                @Override
                public String getName() {
                    return "Fresh Today";
                }
            },
            new StreamItem() {

                @Override
                public Observable<PhotosResponse> getSubscription() {
                    return new Api500pxFacade().upcoming(0);
                }

                @Override
                public String getName() {
                    return "Upcoming";
                }
            }
    };
}
