package com.henry2man.dailyselfie;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

public class DailySelfieAlarmReceiver extends BroadcastReceiver {

	private static final int NOTIFICATION_ID = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context)
				.setContentTitle(context.getText(R.string.take_selfie))
				.setSmallIcon(R.drawable.ic_camera_party_mode_grey600_36dp)
				.setContentText(context.getText(R.string.take_selfie_long))
				.setTicker(context.getText(R.string.take_selfie_long))
				.setSound(
						RingtoneManager
								.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.setAutoCancel(true);

		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, MainActivity.class);

		mBuilder.setContentIntent(PendingIntent.getActivity(context, 0,
				resultIntent, 0));

		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

		AlarmUtils.notifyAlarmFired(context);
		AlarmUtils.restartAlarms(context);
	}

}
