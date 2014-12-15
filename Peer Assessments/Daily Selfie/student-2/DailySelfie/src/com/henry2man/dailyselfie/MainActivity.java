package com.henry2man.dailyselfie;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MainActivity extends Activity {

	private static final String DAILY_SELFIE = "DailySelfie";
	private static final String FILE_URI_STATE = "state.mFileUri";
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	private final static int REQUEST_CODE_CAMERA = 0;
	private final static String TAG = DAILY_SELFIE;

	private Uri mFileUri;

	private SelfiesDataSource mSelfiesDataSource;

	private static SelfieViewAdapter sSelfieViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			PlaceholderFragment placeholderFragment = new PlaceholderFragment();
			getFragmentManager().beginTransaction()
					.add(R.id.container, placeholderFragment).commit();
		}

		this.mSelfiesDataSource = new SelfiesDataSource(this);
		AlarmUtils.restartAlarms(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (this.mFileUri != null) {
			outState.putString(FILE_URI_STATE, mFileUri.toString());
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState.containsKey(FILE_URI_STATE)) {
			this.mFileUri = Uri.parse(savedInstanceState
					.getString(FILE_URI_STATE));
		}
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
		if (id == R.id.take_selfie) {
			// XXX Here comes the new Start Activity For Result
			Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			mFileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

			// set the image file name
			photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);

			startActivityForResult(photoIntent, REQUEST_CODE_CAMERA);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE_CAMERA:

			switch (resultCode) {
			case Activity.RESULT_OK:
				// FIXME Show a happy toast :>
				mSelfiesDataSource.createSelfie(mFileUri,
						(int) sSelfieViewAdapter.getCount());
				// Restart alarms to take another daily Selfie in XX minutes
				AlarmUtils.restartAlarms(this);
				// notify data changed
				sSelfieViewAdapter.notifyDataSetChanged();
				break;
			case Activity.RESULT_CANCELED:
				// FIXME show a very sad toast :((
				break;
			default:
				// User cancelled, error or similar..
				break;
			}

			break;
		default:
			Log.w(TAG, "Unknown requestCode...");
			break;
		}

	}

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				DAILY_SELFIE);
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(TAG, "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd-hhmmss")
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater,
				final ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);

			GridView gridview = (GridView) rootView
					.findViewById(R.id.selfies_grid_view);
			final Context context = container.getContext();
			sSelfieViewAdapter = new SelfieViewAdapter(context);
			gridview.setAdapter(sSelfieViewAdapter);

			gridview.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {

					Intent showSelfieIntent = new Intent(context,
							ViewSelfieActivity.class);

					showSelfieIntent.putExtra(ViewSelfieActivity.SELFIE_ID,
							mSelfiesDataSource.getSelfieByPosition(position)
									.getId());

					startActivity(showSelfieIntent);
				}
			});

			return rootView;
		}
	}
}
