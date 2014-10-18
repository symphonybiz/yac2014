package com.yandex.yac2014;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class PopularPhotosFragment extends ListFragment {

    Api500pxFacade api;
    PhotosAdapter adapter;

    Observable<PhotosResponse> lastRequest;
    Subscription subscription;

    int nextPage = 1;
    int maxPage  = 1;

    public static PopularPhotosFragment newInstance() {
        PopularPhotosFragment fragment = new PopularPhotosFragment();
        return fragment;
    }

    public PopularPhotosFragment() {
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


        if (adapter == null) {
            // first start
            adapter = new PhotosAdapter(this);
        }
        setListAdapter(adapter);
        setUpList();

        if (lastRequest != null) {
            // request in process
            subscribe();
        } else if (adapter.isEmpty()) {
            // last time didn't load anything
            lastRequest = makeRequest();
            subscribe();
        }
    }

    private void setUpList() {
        getListView().setDividerHeight(0);
        getListView().setOnScrollListener(new LoadOnScrollListener() {

            boolean barIsVisible = true;

            @Override
            public void onDataLoadRequest() {
                if (lastRequest == null && nextPage <= maxPage) {
                    lastRequest = makeRequest();
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

    private Observable<PhotosResponse> makeRequest() {
        return api.popularPhotos(nextPage)
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    private void subscribe() {
        setListShown(!adapter.isEmpty());
        subscription = lastRequest
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<PhotosResponse>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Completed");
                        onRequestCompletion();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Failed to load photos.");
                        onRequestCompletion();
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

    private void onRequestCompletion() {
        lastRequest = null;
        setListShown(true);
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
