package com.yandex.yac2014;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.yandex.yac2014.api.Api500pxFacade;
import com.yandex.yac2014.api.response.PhotosResponse;
import com.yandex.yac2014.model.Photo;
import com.yandex.yac2014.view.PhotoListItemView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class PhotosFragment extends ListFragment {

    Api500pxFacade api;
    PhotosAdapter  adapter;

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
            adapter = new PhotosAdapter();

            api.popularPhotos()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<PhotosResponse>() {
                        @Override
                        public void onCompleted() {
                            Timber.d("Completed");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e, "Failed to load photos.");
                        }

                        @Override
                        public void onNext(PhotosResponse photosResponse) {
                            adapter.addPhotos(photosResponse.photos);
                        }
                    });
        }
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    private class PhotosAdapter extends BaseAdapter {
        final List<Photo> photos = new ArrayList<Photo>();

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PhotoListItemView itemView = null;
            if (convertView == null) {
                itemView = new PhotoListItemView(getActivity());
            } else {
                itemView = (PhotoListItemView) convertView;
            }
            itemView.setPhoto(getItem(position));
            return itemView;
        }

        @Override
        public Photo getItem(int position) {
            return photos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addPhotos(Collection<Photo> newPhotos) {
            photos.addAll(newPhotos);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return photos.size();
        }
    }
}
