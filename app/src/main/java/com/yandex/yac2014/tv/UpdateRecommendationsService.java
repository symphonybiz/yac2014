package com.yandex.yac2014.tv;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.yandex.yac2014.R;
import com.yandex.yac2014.api.response.PhotosResponse;
import com.yandex.yac2014.model.Photo;

import java.util.List;
import java.util.concurrent.ExecutionException;

import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by soloist on 09/05/15.
 */
/*
 * This class builds up to MAX_RECOMMMENDATIONS of recommendations and defines what happens
 * when they're clicked from Recommendations section on Home screen
 */
public class UpdateRecommendationsService extends IntentService {
	private static final String TAG = "RecommendationsService";
	private static final int MAX_RECOMMENDATIONS = 3;

	private static final int CARD_WIDTH = 313;
	private static final int CARD_HEIGHT = 176;

	private NotificationManager mNotificationManager;

	public UpdateRecommendationsService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		if (mNotificationManager == null) {
			mNotificationManager = (NotificationManager) getApplicationContext()
					.getSystemService(Context.NOTIFICATION_SERVICE);
		}

		final RecommendationBuilder builder = new RecommendationBuilder()
				.setContext(getApplicationContext())
				.setSmallIcon(R.drawable.videos_by_google_icon);


		final StreamList.StreamItem streamItem = StreamList.STREAM_FEATURE[0];
		streamItem.getSubscription()
				.subscribeOn(Schedulers.immediate())
				.observeOn(Schedulers.immediate())
				.subscribe(new Action1<PhotosResponse>() {
					@Override
					public void call(PhotosResponse photosResponse) {
						int count = 0;
						List<Photo> list = photosResponse.photos;

						if (list == null)
							return;

						Photo photo;
						for (int i = 0; i < list.size() && i < MAX_RECOMMENDATIONS; i++) {
							photo = list.get(i);
							final RecommendationBuilder notificationBuilder = builder
									.setBackground(photo.getFirstImageUri())
									.setId(i + 1)
									.setPriority(MAX_RECOMMENDATIONS - i - 1)
									.setTitle(photo.name)
									.setDescription(photo.user.fullname)
									.setIntent(buildPendingIntent(photo));

							try {
								Bitmap bitmap = Glide.with(getApplicationContext())
										.load(photo.getFirstImageUri())
										.asBitmap()
										.into(CARD_WIDTH, CARD_HEIGHT) // Only use for synchronous .get()
										.get();
								notificationBuilder.setBitmap(bitmap);
								Notification notification = notificationBuilder.build();
								mNotificationManager.notify(i + 1, notification);
							} catch (InterruptedException | ExecutionException e) {
								Log.e(TAG, "Could not create recommendation: " + e);
							}
						}
					}
				});
	}

	private PendingIntent buildPendingIntent(Photo photo) {

		Intent detailsIntent = new Intent(this, DetailsActivity.class);
		detailsIntent.putExtra(DetailsActivity.PHOTO, photo);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(DetailsActivity.class);
		stackBuilder.addNextIntent(detailsIntent);
		// Ensure a unique PendingIntents, otherwise all recommendations end up with the same
		// PendingIntent
		detailsIntent.setAction(Long.toString(photo.hashCode()));

		PendingIntent intent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		return intent;
	}
}
