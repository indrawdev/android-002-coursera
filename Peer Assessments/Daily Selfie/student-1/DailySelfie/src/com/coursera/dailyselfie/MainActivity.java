package com.coursera.dailyselfie;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.coursera.dailyselfie.adapter.PhotoInfoAdapter;
import com.coursera.dailyselfie.entity.PhotoInfo;

public class MainActivity extends ListActivity {
	private static final String TAG = "MainActivity";
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final long INITIAL_ALARM_DELAY = 2 * 60 * 1000L;
	public static final String GALLERY_PATH = Environment.DIRECTORY_PICTURES + File.separator + "DailySelfie";
	public static final String PHOTO_PATH = "com.coursera.dailyselfie.PHOTO_PATH";
	PhotoInfoAdapter photoInfoAdapter;
	String mCurrentPhotoPath;
	Handler uiHandler = new Handler();
	PhotoInfo photoInfoItem;
	AlarmManager alarmManager;
	Intent selfieReminderIntent;
	PendingIntent selfieReminderPendingIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		selfieReminderIntent = new Intent(MainActivity.this, AlarmNotificationReceiver.class);
		selfieReminderPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, selfieReminderIntent, 0);
		
		photoInfoAdapter = new PhotoInfoAdapter(this, new ArrayList<PhotoInfo>());
		setListAdapter(photoInfoAdapter);
		
		ListView lv = getListView();

		// Set an setOnItemClickListener on the ListView
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PhotoInfo photo = photoInfoAdapter.get(position);
				Intent photoIntent = new Intent(MainActivity.this, PictureActivity.class);
				photoIntent.putExtra(PHOTO_PATH, photo.getPath());
				startActivity(photoIntent);
			}
		});

//		new Thread(new LoadGalleryTask(GALLERY_PATH)).start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadGalleryImages();
	}

	public void setAlarm() {
		// Set repeating alarm
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY, INITIAL_ALARM_DELAY, selfieReminderPendingIntent);
		
		// Show Toast message
		Toast.makeText(getApplicationContext(), "Alarm Set", Toast.LENGTH_LONG).show();
	}
	
	private void eraseAlarms(){
		// Cancel all alarms using mNotificationReceiverPendingIntent
		alarmManager.cancel(selfieReminderPendingIntent);
		Toast.makeText(getApplicationContext(),	"Alarms Cancelled", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id){
		case R.id.action_camera:
			dispatchTakePictureIntent();
			return true;
		case R.id.action_set_alarm:
			setAlarm();
			return true;
		case R.id.action_erase_alarm:
			eraseAlarms();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File
				Toast.makeText(this, "Could not create image file.", Toast.LENGTH_LONG).show();
				Log.e("MainActivity", "Error creating image file.", ex);
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

			String photoPath = mCurrentPhotoPath.replace("file:", "");
			Bitmap thumbnail = decodeSampledBitmapFromFile(photoPath, 80, 80);
			PhotoInfo photoInfoItem = new PhotoInfo();
			photoInfoItem.setIcon(thumbnail);
			photoInfoItem.setPath(photoPath);

			String photoName = photoPath.replace(Environment.getExternalStoragePublicDirectory(GALLERY_PATH).toString() + File.separator, "");
			photoInfoItem.setPhotoLabel(String.valueOf(photoName));
			photoInfoAdapter.add(photoInfoItem);
			galleryAddPic();
		}
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(GALLERY_PATH);

		// If application directory does not exists, create it
		if (!storageDir.exists()) {
			storageDir.mkdirs();
			Log.d(TAG, "Folder created at: " + storageDir.toString());
		}

		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}

	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
	
	public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(pathName, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(pathName, options);
	}
	
	// TODO: ejecutar en un thread aparte
	private void loadGalleryImages(){
		photoInfoAdapter.clear();
		File f = Environment.getExternalStoragePublicDirectory(GALLERY_PATH);        
		File file[] = f.listFiles();
		Log.d("Files", "Size: "+ file.length);
		for (int i=0; i < file.length; i++)
		{
			Log.i("DEBUG", "getPath: " + file[i].getPath());
			Log.i("DEBUG", "getPath: " + file[i].getName());
			Log.i("DEBUG", "getPath: " + file[i].getAbsolutePath());
			
			Bitmap thumbnail = decodeSampledBitmapFromFile(file[i].getPath(), 80, 80);
			PhotoInfo photoInfoItem = new PhotoInfo();
			photoInfoItem.setIcon(thumbnail);
			photoInfoItem.setPhotoLabel(file[i].getName());
			photoInfoItem.setPath(file[i].getPath());
			photoInfoAdapter.add(photoInfoItem);
		    Log.d("Files", "FileName:" + file[i].getName());
		}
	}
	
//	private class LoadGalleryTask implements Runnable{
//		String galleryPath;
//		
//		public LoadGalleryTask(String galleryPath) {
//			this.galleryPath = galleryPath;
//		}
//		
//		@Override
//		public void run() {
//			File f = Environment.getExternalStoragePublicDirectory(galleryPath);   
//			File file[] = f.listFiles();
//			if(file != null){
//				for (int i=0; i < file.length; i++)
//				{
//					Log.i("DEBUG", "name: " + file[i].getName());
//					Log.i("DEBUG", "getPath: " + file[i].getPath());
//					Log.i("DEBUG", "getAbsolutePath: " + file[i].getAbsolutePath());
//					
//					Bitmap thumbnail = decodeSampledBitmapFromFile(file[i].getPath(), 80, 80);
//					photoInfoItem = new PhotoInfo();
//					photoInfoItem.setIcon(thumbnail);
//					photoInfoItem.setPhotoLabel(file[i].getName());
//					photoInfoItem.setPath(file[i].getPath());
//					
//					uiHandler.post(new Runnable() {
//						@Override
//						public void run() {
//							photoInfoAdapter.add(photoInfoItem);
//						}
//					});
//					
//					Log.d("Files", "FileName:" + file[i].getName());
//				}
//			}
//		}
//	}
}
