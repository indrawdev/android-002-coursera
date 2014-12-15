package com.henry2man.dailyselfie;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

public class AlarmUtils {

	// 2 minutes in milliseconds = 2 * 60 * 1000
	private static final int MINUTES = 2;
	private static final long TIMEOUT_IN_MILLIS = MINUTES * 60 * 1000;

	private static PendingIntent sPendingIntent;

	public static void restartAlarms(Context context) {
		if (sPendingIntent == null) {
			Toast.makeText(
					context,
					context.getString(R.string.alarm_start) + " " + MINUTES
							+ " " + context.getString(R.string.minutes),
					Toast.LENGTH_LONG).show();

			Intent alarmReceiverIntent = new Intent(context,
					DailySelfieAlarmReceiver.class);
			sPendingIntent = PendingIntent.getBroadcast(context, 0,
					alarmReceiverIntent, 0);

			AlarmManager alarmManager = (AlarmManager) context
					.getSystemService(Activity.ALARM_SERVICE);
			alarmManager.setInexactRepeating(
					AlarmManager.ELAPSED_REALTIME_WAKEUP,
					SystemClock.elapsedRealtime() + TIMEOUT_IN_MILLIS,
					TIMEOUT_IN_MILLIS, sPendingIntent);
		}
	}

	public static void notifyAlarmFired(Context context) {
		if (sPendingIntent != null) {
			sPendingIntent = null;
		}

	}
	// public static void pauseAlarm(Context context) {
	// if (sPendingIntent != null) {
	// Toast.makeText(context, context.getString(R.string.alarm_cancel),
	// Toast.LENGTH_LONG).show();
	// sPendingIntent.cancel();
	// sPendingIntent = null;
	// }
	// }
}
