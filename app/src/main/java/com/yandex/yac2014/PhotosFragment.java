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
import com.yandex.yac2014.view.LoadOnScrollListener;
import com.yandex.yac2014.view.PhotoListItemView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class PhotosFragment extends ListFragment {

    Api500pxFacade api;
    PhotosAdapter  adapter;
    Observable<PhotosResponse> lastRequest;
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
            adapter = new PhotosAdapter();
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
        lastRequest.observeOn(AndroidSchedulers.mainThread())
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
