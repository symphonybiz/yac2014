package com.yandex.yac2014;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.yandex.yac2014.api.Api500pxFacade;
import com.yandex.yac2014.api.response.PhotosResponse;
import com.yandex.yac2014.model.Photo;
import com.yandex.yac2014.storage.Storage;
import com.yandex.yac2014.view.LoadOnScrollListener;
import com.yandex.yac2014.view.PhotosAdapter;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class PhotosFragment extends ListFragment {

    Api500pxFacade api;
    PhotosAdapter adapter;

    Observable<PhotosResponse> lastRequest;
    Subscription subscription;
    
    int nextPage = 1;
    int maxPage  = 1;

    public static PhotosFragment newInstance() {
        PhotosFragment fragment = new PhotosFragment();
        return fragment;
    }

    public PhotosFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = new Api500pxFacade();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDividerHeight(0);

        if (adapter == null) {
            adapter = new PhotosAdapter(this);
            lastRequest = api.popularPhotos(nextPage);
            subscribe();
        } else if (lastRequest != null) {
            // if we have request in process
            subscribe();
        }
        setListAdapter(adapter);

        getListView().setOnScrollListener(new LoadOnScrollListener() {

            boolean barIsVisible = true;

            @Override
            public void onDataLoadRequest() {
                if (lastRequest == null && nextPage <= maxPage) {
                    lastRequest = api.popularPhotos(nextPage);
                    subscribe();
                }
            }

            @Override
            protected void onScrollUp() {
                if (!barIsVisible) {
                    barIsVisible = true;
                    getActivity().getActionBar().show();
                }
            }

            @Override
            protected void onScrollDown() {
                if (barIsVisible) {
                    barIsVisible = false;
                    getActivity().getActionBar().hide();
                }
            }
        });
    }

    private void subscribe() {
        subscription = lastRequest.observeOn(AndroidSchedulers.mainThread())
                .zipWith(Storage.get().photos(), new Func2<PhotosResponse, List<Photo>, PhotosResponse>() {
                    @Override
                    public PhotosResponse call(PhotosResponse photosResponse, List<Photo> likedPhotos) {
                        for (Photo likedPhoto : likedPhotos) {
                            for (Photo photo : photosResponse.photos) {
                                if (photo.id == likedPhoto.id) {
                                    photo.liked = true;
                                    photo._id = likedPhoto._id;
                                }
                            }
                        }
                        return photosResponse;
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<PhotosResponse>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Completed");
                        lastRequest = null;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Failed to load photos.");
                        lastRequest = null;
                    }

                    @Override
                    public void onNext(PhotosResponse photosResponse) {
                        Timber.d("Responded: %s", photosResponse);

                        adapter.addPhotos(photosResponse.photos);
                        maxPage = photosResponse.totalPages;
                        ++nextPage;
                    }
                });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        final Photo item = adapter.getItem(position);
        item.liked = !item.liked;
        adapter.notifyDataSetChanged();

        final Storage storage = Storage.get();
        if (item.liked) {
            storage.savePhoto(item);
        } else {
            storage.deletePhoto(item);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }
}
