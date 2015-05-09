package com.yandex.yac2014.tv;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.widget.DetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.DisplayMetrics;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yandex.yac2014.model.Photo;

import java.net.URI;

public class VideoDetailsFragment extends DetailsFragment {
    private static final String TAG = "VideoDetailsFragment";

    private static final int ACTION_WATCH_TRAILER = 1;
    private static final int ACTION_RENT = 2;
    private static final int ACTION_BUY = 3;

    private static final int DETAIL_THUMB_WIDTH = 274;
    private static final int DETAIL_THUMB_HEIGHT = 274;

    private static final int NUM_COLS = 10;

    private static final String MOVIE = "Movie";

    private Photo mPhoto;

    private Drawable mDefaultBackground;
    private Target mBackgroundTarget;
    private DisplayMetrics mMetrics;
    private DetailsOverviewRowPresenter mDorPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        Log.i(TAG, "onCreate DetailsFragment");
//        super.onCreate(savedInstanceState);
//
//        mDorPresenter =
//                new DetailsOverviewRowPresenter(new DetailsDescriptionPresenter());
//
//        BackgroundManager backgroundManager = BackgroundManager.getInstance(getActivity());
//        backgroundManager.attach(getActivity().getWindow());
//        mBackgroundTarget = new PicassoBackgroundManagerTarget(backgroundManager);
//
//        mDefaultBackground = getResources().getDrawable(R.drawable.default_background);
//
//        mMetrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
//
//        mPhoto = (Movie) getActivity().getIntent().getSerializableExtra(MOVIE);
//        mDetailRowBuilderTask = (DetailRowBuilderTask) new DetailRowBuilderTask().execute(mPhoto);
//        mDorPresenter.setSharedElementEnterTransition(getActivity(),
//                DetailsActivity.SHARED_ELEMENT_NAME);
//
//        updateBackground(mPhoto.getBackgroundImageURI());
//        setOnItemViewClickedListener(new ItemViewClickedListener());

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

//            if (item instanceof Movie) {
//                Movie movie = (Movie) item;
//                Log.d(TAG, "Item: " + item.toString());
//                Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                intent.putExtra(DetailsActivity.MOVIE, movie);
//
//                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        getActivity(),
//                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
//                        DetailsActivity.SHARED_ELEMENT_NAME).toBundle();
//                getActivity().startActivity(intent, bundle);
//            }
        }
    }

    protected void updateBackground(URI uri) {
        Log.d(TAG, "uri" + uri);
        Log.d(TAG, "metrics" + mMetrics.toString());
        Picasso.with(getActivity())
                .load(uri.toString())
                .resize(mMetrics.widthPixels, mMetrics.heightPixels)
                .error(mDefaultBackground)
                .into(mBackgroundTarget);
    }

}
