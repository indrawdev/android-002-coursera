package luis.mayorga.dailyselfie;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	static final int REQUEST_IMAGE_CAPTURE = 1;
	static final String IMAGE_PATH_KEY = "ImagePath";
	static final String TAG = "DailySelfie";
	private ListView mImagesListView = null;
	private PictureListAdapter mAdapter = null;
	private DialogFragment mDialog = null;
	private AlarmManager mAlarmManager;
	private Intent mNotificationReceiverIntent;
	private PendingIntent mNotificationReceiverPendingIntent;
	private static final long INITIAL_ALARM_DELAY = 2 * 60 * 1000L;
	private static final long ALARM_INTERVAL = 2 * 60 * 1000L;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		
		mImagesListView = (ListView) findViewById(R.id.picturesListView);
		mImagesListView.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
        		Object item = mImagesListView.getItemAtPosition(position);
            	PictureDetails currentImageDetails = (PictureDetails) item;
            	
            	Intent myIntent = new Intent(MainActivity.this, ImageViewer.class);
            	myIntent.putExtra(IMAGE_PATH_KEY, currentImageDetails.getPath());
            	startActivity(myIntent);
        	}  
        });
		
		mAdapter = new PictureListAdapter(MainActivity.this, new ArrayList<PictureDetails>());
		mImagesListView.setAdapter(mAdapter);
		new LoadPicturesTask().execute(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath());
				
		// Set repeating alarm
		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		// Create an Intent to broadcast to the AlarmNotificationReceiver
		mNotificationReceiverIntent = new Intent(MainActivity.this, AlarmNotificationReceiver.class);
		// Create an PendingIntent that holds the NotificationReceiverIntent
		mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, mNotificationReceiverIntent, 0);
		
		mAlarmManager.setRepeating(AlarmManager.RTC,
				System.currentTimeMillis() + INITIAL_ALARM_DELAY,
				ALARM_INTERVAL,
				mNotificationReceiverPendingIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_takePic) {
			takePicture();
			return true;
		} else if (id == R.id.action_deletePics) {
			// Create a new AlertDialogFragment
			mDialog = AlertDialogFragment.newInstance();

			// Show AlertDialogFragment
			mDialog.show(getFragmentManager(), "Are you sure?");
			return true;
		} else if (id == R.id.action_killAlarm) {
			// Cancel all alarms using mNotificationReceiverPendingIntent
			mAlarmManager.cancel(mNotificationReceiverPendingIntent);

			// Show Toast message
			Toast.makeText(getApplicationContext(), "Selfie reminders were cancelled until the app is launched again.", Toast.LENGTH_LONG).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void takePicture(){
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    // Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	    	// Create the File where the photo should go
	        File photoFile = null;
	        try {
	            photoFile = ImageHelper.createImageFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES));
	        } catch (IOException ex) {
	        	Log.i(TAG, "Error occurred while creating the initial image File");
	        }
	        // Continue only if the File was successfully created
	        if (photoFile != null) {
	        	takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
	        	startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	        	Log.i(TAG, "Take picture intent was started. The initial image file was put in the intent as extra info.");
	        }
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE) {
			if(resultCode == RESULT_OK){
				Log.i(TAG, "Take Picture intent has returned with RESULT_OK");
				PictureDetails newPic = new PictureDetails();
				newPic.setName(ImageHelper.getLastPictureName());
				newPic.setPath(ImageHelper.getLastPicturePath());
				mAdapter.add(newPic);
				ImageHelper.addImageToGallery(this);
			}else{
				ImageHelper.deleteInitialImage();
			}
	    }
	}
	
	// Abort or complete ShutDown based on value of shouldContinue
	private void deleteAllPics(boolean delete) {
		if (delete) {
			ImageHelper.deleteAllImages(getExternalFilesDir(Environment.DIRECTORY_PICTURES));
			mAdapter.clear();
		} else {
			mDialog.dismiss();
		}
	}
	
	public class LoadPicturesTask extends AsyncTask<String, Void, ArrayList<PictureDetails>> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected ArrayList<PictureDetails> doInBackground(String... imagesFolder) {
			ArrayList<PictureDetails> results = new ArrayList<PictureDetails>();
			if(imagesFolder != null && imagesFolder.length > 0){
				Log.i(TAG, "We're about to read the folder: " + imagesFolder[0]);
		    	File imagesFolderInfo = new File(imagesFolder[0]);        
		    	File filesList[] = imagesFolderInfo.listFiles();
		    	Log.i(TAG, "There are " + filesList.length + " files inside the folder");
		    	for (int i=0; i < filesList.length; i++)
		    	{
		    		String imageName = filesList[i].getName();
		    		String imagePath = filesList[i].getAbsolutePath();
		    		
		    		PictureDetails picDetails = new PictureDetails();
		    		picDetails.setName(imageName);
		    		picDetails.setPath(imagePath);
		        	results.add(picDetails);
		        	Log.i(TAG, "Image name: " + imageName + ", Image Path: " + imagePath);
		    	}
			}
	    	
	    	return results;
		}

		@Override
		protected void onPostExecute(ArrayList<PictureDetails> result) {
	        mAdapter.add(result);
		}
	}
	
	// Class that creates the AlertDialog
	public static class AlertDialogFragment extends DialogFragment {

		public static AlertDialogFragment newInstance() {
			return new AlertDialogFragment();
		}

		// Build AlertDialog using AlertDialog.Builder
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())
					.setMessage("Do you want to delete all your selfies?")
					
					// User cannot dismiss dialog by hitting back button
					.setCancelable(false)
					
					// Set up No Button
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									((MainActivity) getActivity()).deleteAllPics(false);
								}
							})
							
					// Set up Yes Button
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog, int id) {
									((MainActivity) getActivity()).deleteAllPics(true);
								}
							}).create();
		}
	}
}
