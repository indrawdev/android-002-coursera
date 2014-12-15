package com.coursera.dailyselfie;

import java.text.DateFormat;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmNotificationReceiver extends BroadcastReceiver {
	// Notification ID to allow for future updates
	private static final int MY_NOTIFICATION_ID = 1;
	private static final String TAG = "DS_AlarmNotificationReceiver";

	// Notification Text Elements
	private final CharSequence tickerText = "Just a reminder";
	private final CharSequence contentTitle = "Daily Selfie";
	private final CharSequence contentText = "Time for another selfie.";

	// Notification Action Elements
	private Intent notificationIntent;
	private PendingIntent contentIntent;

	// Notification Sound and Vibration on Arrival
//	private final Uri soundURI = Uri.parse("android.resource://course.examples.Alarms.AlarmCreate/"	+ R.raw.alarm_rooster);
	private final long[] vibratePattern = { 0, 200, 200, 300 };

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "Receiving Intent");
		// The Intent to be used when the user clicks on the Notification View
		notificationIntent = new Intent(context, MainActivity.class);

		// The PendingIntent that wraps the underlying Intent
		contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

		Notification.Builder notificationBuilder = new Notification.Builder(context)
				.setTicker(tickerText)
				.setSmallIcon(android.R.drawable.ic_menu_camera)
				.setAutoCancel(true).setContentTitle(contentTitle)
				.setContentText(contentText).setContentIntent(contentIntent)
				.setVibrate(vibratePattern);

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(MY_NOTIFICATION_ID,	notificationBuilder.build());

		Log.i(TAG, "Sending notification at:" + DateFormat.getDateTimeInstance().format(new Date()));

	}
}
