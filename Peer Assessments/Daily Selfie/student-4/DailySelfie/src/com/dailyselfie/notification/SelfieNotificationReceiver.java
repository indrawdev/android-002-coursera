package com.dailyselfie.notification;

import com.dailyselfie.DailySelfieActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SelfieNotificationReceiver extends BroadcastReceiver {
	private static final int MY_NOTIFICATION_ID = 1;

	private final CharSequence contentTitle = "Daily Selfie";
	private final CharSequence contentText = "Time for another selfie";

	private Intent mNotificationIntent;
	private PendingIntent mContentIntent;

	private final long[] mVibratePattern = { 0, 200, 200, 300 };

	@Override
	public void onReceive(Context context, Intent intent) {

		mNotificationIntent = new Intent(context, DailySelfieActivity.class);
		mNotificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		mContentIntent = PendingIntent.getActivity(context, 0,
				mNotificationIntent, Intent.FLAG_ACTIVITY_NO_HISTORY);
		
		Notification.Builder notificationBuilder = new Notification.Builder(
				context).setSmallIcon(android.R.drawable.ic_menu_camera)
				.setAutoCancel(true).setContentTitle(contentTitle)
				.setContentText(contentText).setContentIntent(mContentIntent)
				.setVibrate(mVibratePattern);

		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		mNotificationManager.notify(MY_NOTIFICATION_ID,
				notificationBuilder.build());
	}
}
