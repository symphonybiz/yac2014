package com.yandex.yac2014.tv;

import android.graphics.Color;
import android.support.v17.leanback.app.SearchFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.yandex.yac2014.R;
import com.yandex.yac2014.api.Api500pxFacade;
import com.yandex.yac2014.api.response.PhotosResponse;
import com.yandex.yac2014.model.Photo;

import java.net.URI;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SearchActivityFragment
        extends SearchFragment
        implements SearchFragment.SearchResultProvider
{
    private static int CARD_WIDTH = 313;
    private static int CARD_HEIGHT = 176;

    private static final int SEARCH_DELAY_MS = 300;
    private ArrayObjectAdapter mRowsAdapter;

    private final Api500pxFacade mApi = new Api500pxFacade();

    private Subscription mSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRowsAdapter = new ArrayObjectAdapter(new SearchResultRow());
        setSearchResultProvider(this);
        setOnItemClickedListener(null);
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        return mRowsAdapter;
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        mRowsAdapter.clear();
        if (!TextUtils.isEmpty(newQuery)) {
            cancel();
            mApi.search(newQuery)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Action1<PhotosResponse>() {
                        @Override
                        public void call(PhotosResponse photosResponse) {
                            for (int i = 0; i < photosResponse.photos.size(); ++i) {
                                mRowsAdapter.add(i, photosResponse.photos.get(i));
                            }
                        }
                    });
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mRowsAdapter.clear();
        if (!TextUtils.isEmpty(query)) {
        }
        return true;
    }

    void cancel() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }


    class SearchVH extends PhotoPresenter.ViewHolder {

        public SearchVH(View view) {
            super(view);
        }

    }

    class SearchResultRow extends RowPresenter {

        @Override
        protected ViewHolder createRowViewHolder(ViewGroup parent) {
            ImageCardView cardView = new ImageCardView(parent.getContext());
            cardView.setFocusable(true);
            cardView.setFocusableInTouchMode(true);
            return new ViewHolder(cardView);
        }

        @Override
        protected void onBindRowViewHolder(ViewHolder viewholder, Object item) {
            Photo photo = (Photo) item;
            ImageCardView imageCardView = (ImageCardView) viewholder.view;
            Picasso.with(getActivity())
                    .load(photo.getFirstImageUri())
                    .placeholder(Color.BLACK)
                    .resize(Utils.convertDpToPixel(getActivity(), CARD_WIDTH),
                            Utils.convertDpToPixel(getActivity(), CARD_HEIGHT))
                    .into(imageCardView.getMainImageView());
        }
    }
}
