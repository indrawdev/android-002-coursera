package com.coursera.assignment.week8.selfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import java.text.DateFormat;
import java.util.Date;

/**
 * Created by viswaviv on 27/11/2014.
 */
public class SelfiAlarmNotificationReceiver extends BroadcastReceiver {
    private final int NOTIFICATION_ID = 21;
    private static final String TAG = "SelfiAlarmNotificationReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        // Create the pending intend to launch the Daily Selfi program.
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, DailySelfiMainActivity.class), Intent.FLAG_ACTIVITY_NEW_TASK);


        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setTicker(context.getString(R.string.selfi_notification_ticker_text))
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.selfi_notification_title))
                .setContentText(context.getString(R.string.selfi_notification_text))
                .setContentIntent(notificationPendingIntent);

        // Get the NotificationManager
        NotificationManager mNotificationManager = (NotificationManager) context.
                getSystemService(Context.NOTIFICATION_SERVICE);

        // Pass the Notification to the NotificationManager:
        mNotificationManager.notify(NOTIFICATION_ID,
                notificationBuilder.build());
        // Log occurence of notify() call
        Log.i(TAG, "Sending notification at:"
                + DateFormat.getDateTimeInstance().format(new Date()));
    }
}