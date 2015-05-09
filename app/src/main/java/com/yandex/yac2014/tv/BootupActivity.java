package com.yandex.yac2014.tv;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by soloist on 09/05/15.
 */
/*
 * This class extends BroadCastReceiver and publishes recommendations on bootup
 */
public class BootupActivity extends BroadcastReceiver {
	private static final String TAG = "BootupActivity";

	private static final long INITIAL_DELAY = 5000;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "BootupActivity initiated");
		if (intent.getAction().endsWith(Intent.ACTION_BOOT_COMPLETED)) {
			scheduleRecommendationUpdate(context);
		}
	}

	private void scheduleRecommendationUpdate(Context context) {
		Log.d(TAG, "Scheduling recommendations update");

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent recommendationIntent = new Intent(context, UpdateRecommendationsService.class);
		PendingIntent alarmIntent = PendingIntent.getService(context, 0, recommendationIntent, 0);

		alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				INITIAL_DELAY,
				AlarmManager.INTERVAL_HALF_HOUR,
				alarmIntent);
	}
}
