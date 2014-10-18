package com.yandex.yac2014;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.yandex.yac2014.model.Photo;
import com.yandex.yac2014.storage.Storage;
import com.yandex.yac2014.view.PhotosAdapter;

import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
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

        if (adapter == null) {
            adapter = new PhotosAdapter(this);
            makeRequest();
            subscribe();
        } else if (lastRequest != null) {
            subscribe();
        }

        setListAdapter(adapter);
    }

    private void makeRequest() {
        lastRequest = Storage.get().photos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void subscribe() {
        subscription = lastRequest.subscribe(new Subscriber<List<Photo>>() {
            @Override
            public void onCompleted() {
                Timber.d("Completed");
                lastRequest = null;
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "Failed");
                lastRequest = null;
            }

            @Override
            public void onNext(List<Photo> photos) {
                adapter.addPhotos(photos);
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        final Photo photo = adapter.getItem(position);

        if (photo.liked) {
            Storage.get().deletePhoto(photo);
        } else {
            Storage.get().savePhoto(photo);
        }
        photo.liked = !photo.liked;
        adapter.notifyDataSetChanged();
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
