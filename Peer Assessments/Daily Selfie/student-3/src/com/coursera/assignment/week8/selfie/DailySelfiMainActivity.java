package com.coursera.assignment.week8.selfie;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by viswaviv on 26/11/2014.
 */
public class DailySelfiMainActivity extends ListActivity {
    public static final String TAG = "DailySelfMainActivity";
    private SimpleCursorAdapter listAdapter;

    private SelfiDatabaseHelper selfiDatabaseHelper;
    private String currentPhotoPath;
    private String currentPhotoName;

    File selfiAlbumFolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selfiAlbumFolder = getSelfiAlbumDir();
        if (selfiAlbumFolder == null) {
            Log.e(TAG,"Selfi Album is null, since we may not have access to external storage or we are out of space, exiting application");
            Toast.makeText(this,"Unable to access Album folder for saving the pictures, terminating application",Toast.LENGTH_LONG)
                    .show();
            finish();
        }

        selfiDatabaseHelper = new SelfiDatabaseHelper(this);

        Cursor c = readSelfiDatabase();

        listAdapter = new SimpleCursorAdapter(this, R.layout.selfi_row_layout,
                c,
                new String[] {SelfiDatabaseHelper.IMAGE_URL_COL_NAME,SelfiDatabaseHelper.IMAGE_DESCRIPTION_COLNAME},
                new int[]{R.id.imageView, R.id.imageName},
                0);
        setListAdapter(listAdapter);

        createRepeatingAlarm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.daily_selfi_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private final int TAKE_PICTURE_REQUEST_CODE = 2;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.take_selfi_menu_item:
                Log.i(TAG, "Selected menu item to take a selfi");
                try {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    currentPhotoName = getImageName();
                    File imageFile = createImageFile(currentPhotoName);
                    currentPhotoPath = imageFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                    startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST_CODE);
                } catch (IOException e) {
                    Log.e(TAG, "Error in creating the image file", e);
                    currentPhotoName = null;
                    currentPhotoPath = null;
                }
                return true;
            case R.id.delete_all_selfi_menu_item:
                Log.i(TAG, "Deleting all selfi imges");
                deleteAllImages();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == TAKE_PICTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (currentPhotoName != null && currentPhotoPath != null) {
                Log.i(TAG, "Current Photo Name: " + currentPhotoName + ", Path: " + currentPhotoPath);
                addImageToList(currentPhotoPath, currentPhotoName);
            } else {
                Log.i(TAG, "The Current Photo Path or the Current Photo name is null cannot set the image on the list adapter");
            }
        } else {
            Log.i(TAG, "Invalid onActivityResult , requestCode " + requestCode + " resultCode :" + resultCode);
        }
    }

    private void addImageToList(String imagePath, String imageName) {
        ContentValues values = new ContentValues();

        values.put(SelfiDatabaseHelper.IMAGE_URL_COL_NAME, imagePath);
        values.put(SelfiDatabaseHelper.IMAGE_DESCRIPTION_COLNAME, imageName);
        selfiDatabaseHelper.getWritableDatabase().insert(SelfiDatabaseHelper.TABLE_NAME, null, values);
        listAdapter.getCursor().requery();
        listAdapter.notifyDataSetChanged();
    }

    private void deleteAllImages() {
        selfiDatabaseHelper.getWritableDatabase().delete(SelfiDatabaseHelper.TABLE_NAME,null,null);
        listAdapter.getCursor().requery();
        listAdapter.notifyDataSetChanged();
    }

    // Returns all artist records in the database
    private Cursor readSelfiDatabase() {
        return selfiDatabaseHelper.getWritableDatabase().query(SelfiDatabaseHelper.TABLE_NAME,
                SelfiDatabaseHelper.COLUMNS,
                null, new String[] {}, null, null,
                null);
    }

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private File createImageFile(String imageTimeStampName) throws IOException {
        // Create an image file name
        String imageFileName = JPEG_FILE_PREFIX + imageTimeStampName + "_";
        return File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, selfiAlbumFolder);
    }

    private String getImageName() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    private File getSelfiAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            final String picturesFolder = Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO ? Environment.DIRECTORY_PICTURES : "/dcim/";
            storageDir = new File(
                    Environment.getExternalStoragePublicDirectory(
                            picturesFolder
                    ), getString(R.string.selfi_album_name));

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d(TAG, "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    private static final long ALARM_DELAY = 2 * 60 * 1000L;
    private static final long INITAL_ALARM_DELAY = 30 * 1000L;

    private void createRepeatingAlarm() {
        // Create the repeating Alaram to notifiy regarding taking a snap
        // Get the AlarmManager Service
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // Set inexact repeating alarm

        Intent notificationReciverIntent = new Intent(this, SelfiAlarmNotificationReceiver.class);
        PendingIntent notificationPendingIntent = PendingIntent.getBroadcast(this, 0,
                notificationReciverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mAlarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + INITAL_ALARM_DELAY,
                ALARM_DELAY,
                notificationPendingIntent);

        Log.i(TAG, "Create the Alarm at:" + getDateStr(new Date()));

    }

    private String getDateStr(Date d) {
        return DateFormat.getDateTimeInstance().format(d);
    }
}
