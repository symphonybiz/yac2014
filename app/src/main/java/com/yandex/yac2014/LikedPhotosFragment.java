package com.yandex.yac2014;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.yandex.yac2014.model.Photo;
import com.yandex.yac2014.storage.Storage;
import com.yandex.yac2014.view.PhotosAdapter;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by 7times6 on 18.10.14.
 */
public class LikedPhotosFragment extends ListFragment {

    public static LikedPhotosFragment newInstance() {
        return new LikedPhotosFragment();
    }

    public LikedPhotosFragment() {
        setRetainInstance(true);
    }

    PhotosAdapter adapter;
    Observable<List<Photo>> lastRequest;
    Subscription subscription;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDividerHeight(0);

        if (adapter == null) {
            adapter = new PhotosAdapter(this);
        }
        setListAdapter(adapter);

        if (lastRequest != null) {
            subscribe();
        } else if (adapter.isEmpty()) {
            makeRequest();
            subscribe();
        }
    }

    private void makeRequest() {
        lastRequest = Storage.get().photos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void subscribe() {
        setListShown(!adapter.isEmpty());
        subscription = lastRequest.subscribe(new Subscriber<List<Photo>>() {
            @Override
            public void onCompleted() {
                Timber.d("Completed");
                onRequestCompletion();
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "Failed");
                onRequestCompletion();
            }

            @Override
            public void onNext(List<Photo> photos) {
                adapter.addPhotos(photos);
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

        final Photo photo = adapter.getItem(position);

        Storage.get()
                .toggleLiked(photo)
                .subscribe(new Action1<Photo>() {
                    @Override
                    public void call(Photo photo) {
                        adapter.notifyDataSetChanged();

                    }
                });
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
