package com.yandex.yac2014.tv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.SearchFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.RowPresenter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yandex.yac2014.R;
import com.yandex.yac2014.api.Api500pxFacade;
import com.yandex.yac2014.api.response.PhotosResponse;
import com.yandex.yac2014.model.Photo;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SearchActivityFragment
	extends SearchFragment
	implements SearchFragment.SearchResultProvider {

	private static int CARD_WIDTH = 200;
	private static int CARD_HEIGHT = 125;
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

	static class SearchViewHolder extends RowPresenter.ViewHolder {

		private ImageView mImageView;
		private TextView mNameTextView;
		private TextView mDescriptionTextView;
		private Context mContext;

		public SearchViewHolder(View itemView) {
			super(itemView);

			itemView.setFocusable(true);
			itemView.setFocusableInTouchMode(true);

			mContext = itemView.getContext();
			mImageView = (ImageView) itemView.findViewById(R.id.image);
			mNameTextView = (TextView) itemView.findViewById(R.id.name);
			mDescriptionTextView = (TextView) itemView.findViewById(R.id.description);
		}

		public static SearchViewHolder create(Context context) {
			return new SearchViewHolder(LayoutInflater.from(context).inflate(R.layout.view_search_result, null));
		}

		public ImageView getImageView() {
			return mImageView;
		}

		public TextView getNameTextView() {
			return mNameTextView;
		}

		public TextView getDescriptionTextView() {
			return mDescriptionTextView;
		}

		public Context getContext() {
			return mContext;
		}
	}

	class SearchResultRow extends RowPresenter {

		@Override
		protected ViewHolder createRowViewHolder(ViewGroup parent) {
			return SearchViewHolder.create(parent.getContext());
		}

		@Override
		protected void onBindRowViewHolder(final ViewHolder vh, Object item) {
			final Photo photo = (Photo) item;
			final SearchViewHolder viewHolder = (SearchViewHolder) vh;
			Picasso.with(getActivity())
				.load(photo.getFirstImageUri())
				.resize(CARD_WIDTH, CARD_HEIGHT)
				.into(viewHolder.getImageView());

			viewHolder.getNameTextView().setText(photo.name);
			viewHolder.getDescriptionTextView().setText(photo.user.fullname);

			((SearchViewHolder) vh).view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), DetailsActivity.class);
					intent.putExtra(DetailsActivity.PHOTO, photo);
					getActivity().startActivity(intent);
				}
			});
		}
	}
}
